package main

import (
	"fmt"
	"github.com/charmbracelet/lipgloss"
	"strings"
)

var help = [][2]string{
	{"↑/↓", "change charisma"},
	{"←/→", "change item type"},
	{"q/ctrl+c", "quit"},
}

var keyStyle = lipgloss.NewStyle().Foreground(lipgloss.Color("7"))
var textStyle = lipgloss.NewStyle().Foreground(lipgloss.Color("8"))

func RenderHelp() string {
	var rows = []string{}
	for _, row := range help {
		rows = append(rows, fmt.Sprintf(
			"%s %s",
			keyStyle.Render(row[0]),
			textStyle.Render(row[1]),
		))
	}
	return strings.Join(rows, "   ")
}
