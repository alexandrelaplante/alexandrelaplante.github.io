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

type SelectedInput int

const (
	INPUT_CHARISMA = iota
	INPUT_PRICE
	NUM_INPUT
)

type model struct {
	selectedInput SelectedInput
	selectedTable SelectedTable
	charisma      textinput.Model
	price         textinput.Model
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
		case "tab", "enter", " ":
			m.selectedInput = (m.selectedInput + 1) % NUM_INPUT
			switch m.selectedInput {
			case INPUT_PRICE:
				cmd = m.price.Focus()
				m.charisma.Blur()
			case INPUT_CHARISMA:
				cmd = m.charisma.Focus()
				m.price.Blur()
			}
		default:
			switch m.selectedInput {
			case INPUT_PRICE:
				m.price, cmd = m.price.Update(msg)
			case INPUT_CHARISMA:
				m.charisma, cmd = m.charisma.Update(msg)
			}
		}
	}

	return m, cmd
}

func TableName(s SelectedTable) string {
	switch s {
	// case TABLE_ARMOR:
	// 	return "Armor"
	case TABLE_SCROLLS:
		return "Scrolls"
	case TABLE_POTIONS:
		return "Potions"
	case TABLE_RINGS:
		return "Rings"
	case TABLE_WANDS:
		return "Wands"
	case TABLE_SPELLBOOKS:
		return "Spellbooks"
	}
	return ""
}

func (m model) View() string {
	var table string
	rizz, rizzErr := strconv.Atoi(m.charisma.Value())
	price, priceErr := strconv.Atoi(m.price.Value())
	if rizzErr != nil {
		table = "Enter a valid charisma"
	} else if priceErr != nil {
		table = RenderTable(rizz, -1, int(m.selectedTable))
	} else {
		table = RenderTable(rizz, price, int(m.selectedTable))
	}
	return fmt.Sprintf(`%s   %s
%s
%s
`,
		m.charisma.View(),
		m.price.View(),
		TableName(m.selectedTable),
		table,
	)
}

func main() {
	var charisma = textinput.New()
	charisma.Placeholder = "XX"
	charisma.Focus()
	charisma.CharLimit = 2
	charisma.Width = 2
	charisma.Prompt = "Charisma: "
	// charisma.Validate = ccnValidator

	var price = textinput.New()
	price.Placeholder = "XXX"
	price.CharLimit = 3
	price.Width = 3
	price.Prompt = "Price: "
	// price.Validate = expValidator

	m := model{INPUT_CHARISMA, TABLE_SCROLLS, charisma, price}
	if _, err := tea.NewProgram(m).Run(); err != nil {
		fmt.Println("Error running program:", err)
		os.Exit(1)
	}
}
