package main

import (
	"context"
	"flag"
	"fmt"
	"os"
	"strconv"

	"errors"
	"github.com/charmbracelet/bubbles/textinput"
	tea "github.com/charmbracelet/bubbletea"
	"github.com/charmbracelet/lipgloss"
	"net"
	"os/signal"
	"syscall"
	"time"

	"github.com/charmbracelet/log"
	"github.com/charmbracelet/ssh"
	"github.com/charmbracelet/wish"
	"github.com/charmbracelet/wish/activeterm"
	"github.com/charmbracelet/wish/bubbletea"
	"github.com/charmbracelet/wish/logging"
)

type model struct {
	// data
	selectedCharisma SelectedCharisma
	selectedTable    SelectedTable
	price            textinput.Model

	// terminal stuff
	term      string
	width     int
	height    int
	bg        string
	renderer  *lipgloss.Renderer
	txtStyle  lipgloss.Style
	quitStyle lipgloss.Style
}

func (m model) Init() tea.Cmd { return nil }

func (m model) Update(msg tea.Msg) (tea.Model, tea.Cmd) {

	var cmd tea.Cmd
	switch msg := msg.(type) {
	case tea.WindowSizeMsg:
		m.height = msg.Height
		m.width = msg.Width
	case tea.KeyMsg:
		switch msg.String() {
		case "q", "ctrl+c":
			return m, tea.Quit
		case "left":
			m.selectedTable = (NUM_TABLE + m.selectedTable - 1) % NUM_TABLE
		case "right":
			m.selectedTable = (m.selectedTable + 1) % NUM_TABLE
		case "down":
			m.selectedCharisma = (NUM_CH + m.selectedCharisma - 1) % NUM_CH
		case "up":
			m.selectedCharisma = (m.selectedCharisma + 1) % NUM_CH
		default:
			m.price, cmd = m.price.Update(msg)
		}
	case tea.MouseMsg:
		switch msg.String() {
		case "left press":
			switch {
			case msg.Y == 2 && msg.X < 16:
				m.selectedTable = TABLE_SCROLLS
			case msg.Y == 2 && msg.X < 32:
				m.selectedTable = TABLE_POTIONS
			case msg.Y == 2 && msg.X < 48:
				m.selectedTable = TABLE_RINGS
			case msg.Y == 2 && msg.X < 64:
				m.selectedTable = TABLE_WANDS
			case msg.Y == 2 && msg.X < 80:
				m.selectedTable = TABLE_SPELLBOOKS
			case msg.Y == 0 && msg.X >= 10 && msg.X <= 16:
				m.selectedCharisma = (m.selectedCharisma + 1) % NUM_CH
			}
		}
	}

	return m, cmd
}

func (m model) View() string {
	var table string
	rizz := RizzValue(m.selectedCharisma)
	price, priceErr := strconv.Atoi(m.price.Value())
	if priceErr != nil {
		price = -1
	}
	table = RenderTable(m.renderer, m.width, rizz, price, int(m.selectedTable))

	return fmt.Sprintf(`%s   %s

%s
%s

%s
`,
		RenderCharisma(m.renderer, m.selectedCharisma),
		m.price.View(),
		RenderTabs(m.renderer, m.selectedTable),
		table,
		RenderHelp(m.renderer),
	)
}

func main() {
	var host string
	var port int
	flag.StringVar(&host, "host", "127.0.0.1", "Host address for SSH server to listen")
	flag.IntVar(&port, "port", 22, "Port for SSH server to listen")

	flag.Parse()

	s, err := wish.NewServer(
		wish.WithAddress(net.JoinHostPort(host, strconv.Itoa(port))),
		wish.WithHostKeyPath(".ssh/id_ed25519"),
		wish.WithMiddleware(
			bubbletea.Middleware(teaHandler),
			activeterm.Middleware(), // Bubble Tea apps usually require a PTY.
			logging.Middleware(),
		),
	)
	if err != nil {
		log.Error("Could not start server", "error", err)
	}

	done := make(chan os.Signal, 1)
	signal.Notify(done, os.Interrupt, syscall.SIGINT, syscall.SIGTERM)
	log.Info("Starting SSH server", "host", host, "port", port)
	go func() {
		if err = s.ListenAndServe(); err != nil && !errors.Is(err, ssh.ErrServerClosed) {
			log.Error("Could not start server", "error", err)
			done <- nil
		}
	}()

	<-done
	log.Info("Stopping SSH server")
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer func() { cancel() }()
	if err := s.Shutdown(ctx); err != nil && !errors.Is(err, ssh.ErrServerClosed) {
		log.Error("Could not stop server", "error", err)
	}
}

// You can wire any Bubble Tea model up to the middleware with a function that
// handles the incoming ssh.Session. Here we just grab the terminal info and
// pass it to the new model. You can also return tea.ProgramOptions (such as
// tea.WithAltScreen) on a session by session basis.
func teaHandler(s ssh.Session) (tea.Model, []tea.ProgramOption) {
	// This should never fail, as we are using the activeterm middleware.
	pty, _, _ := s.Pty()

	// When running a Bubble Tea app over SSH, you shouldn't use the default
	// lipgloss.NewStyle function.
	// That function will use the color profile from the os.Stdin, which is the
	// server, not the client.
	// We provide a MakeRenderer function in the bubbletea middleware package,
	// so you can easily get the correct renderer for the current session, and
	// use it to create the styles.
	// The recommended way to use these styles is to then pass them down to
	// your Bubble Tea model.
	renderer := bubbletea.MakeRenderer(s)
	txtStyle := renderer.NewStyle().Foreground(lipgloss.Color("10"))
	quitStyle := renderer.NewStyle().Foreground(lipgloss.Color("8"))

	bg := "light"
	if renderer.HasDarkBackground() {
		bg = "dark"
	}

	var price = textinput.New()
	price.Focus()
	price.Placeholder = "XXXX"
	price.CharLimit = 4
	price.Width = 4
	price.Prompt = "Price: "
	price.PromptStyle = renderer.NewStyle().Foreground(lipgloss.Color("3"))
	price.Cursor.Style = renderer.NewStyle()

	m := model{
		selectedCharisma: CH_UNDER_10,
		selectedTable:    TABLE_SCROLLS,
		price:            price,
		term:             pty.Term,
		width:            pty.Window.Width,
		height:           pty.Window.Height,
		bg:               bg,
		renderer:         renderer,
		txtStyle:         txtStyle,
		quitStyle:        quitStyle,
	}
	return m, []tea.ProgramOption{tea.WithAltScreen(), tea.WithMouseCellMotion()}
}
