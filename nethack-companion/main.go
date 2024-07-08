package main

import (
	"fmt"
	"os"
	"strconv"

	"github.com/charmbracelet/bubbles/textinput"
	tea "github.com/charmbracelet/bubbletea"
	"github.com/charmbracelet/lipgloss"
)

var baseStyle = lipgloss.NewStyle().
	BorderStyle(lipgloss.NormalBorder()).
	BorderForeground(lipgloss.Color("40"))

type model struct {
	selectedCharisma SelectedCharisma
	selectedTable    SelectedTable
	price            textinput.Model
}

func (m model) Init() tea.Cmd { return nil }

func (m model) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
	var cmd tea.Cmd
	switch msg := msg.(type) {
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
	table = RenderTable(rizz, price, int(m.selectedTable))

	return fmt.Sprintf(`%s   %s

%s
%s

%s
`,
		RenderCharisma(m.selectedCharisma),
		m.price.View(),
		RenderTabs(m.selectedTable),
		table,
		RenderHelp(),
	)
}

func main() {
	var price = textinput.New()
	price.Focus()
	price.Placeholder = "XXXX"
	price.CharLimit = 4
	price.Width = 4
	price.Prompt = "Price: "
	price.PromptStyle = lipgloss.NewStyle().Foreground(lipgloss.Color("3"))
	// price.TextStyle = lipgloss.NewStyle().Foreground(lipgloss.Color("3"))
	// price.Validate = expValidator

	m := model{CH_UNDER_10, TABLE_SCROLLS, price}
	if _, err := tea.NewProgram(m, tea.WithMouseCellMotion(), tea.WithAltScreen()).Run(); err != nil {
		fmt.Println("Error running program:", err)
		os.Exit(1)
	}
}
