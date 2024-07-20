package main

import (
	"fmt"
	"github.com/charmbracelet/lipgloss"
)

var help = [][2]string{
	{"↑/↓", "change charisma"},
	{"←/→", "change item type"},
	{"q/ctrl+c", "quit"},
	{"type", "search by price"},
	{"left click", "navigate"},
}

func RenderHelp(renderer *lipgloss.Renderer) string {
	var keyStyle = renderer.NewStyle().Foreground(lipgloss.Color("7"))
	var textStyle = renderer.NewStyle().Foreground(lipgloss.Color("8"))
	var itemStyle = renderer.NewStyle().Width(26)

	text := ""

	for i, row := range help {
		text += itemStyle.Render(fmt.Sprintf(
			"%s %s",
			keyStyle.Render(row[0]),
			textStyle.Render(row[1]),
		))
		if i%3 == 2 {
			text += "\n"
		}
	}
	return text
}
