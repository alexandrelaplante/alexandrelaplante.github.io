package main

import (
	"github.com/charmbracelet/lipgloss"
	"strings"
)

func TableName(s SelectedTable) string {
	switch s {
	// case TABLE_ARMOR:
	// 	return "Armor"
	case TABLE_SCROLLS:
		return "?Scrolls?"
	case TABLE_POTIONS:
		return "!Potions!"
	case TABLE_RINGS:
		return "=Rings="
	case TABLE_WANDS:
		return "/Wands/"
	case TABLE_SPELLBOOKS:
		return "+Spellbooks+"
	}
	return ""
}

func RenderTabs(renderer *lipgloss.Renderer, selected SelectedTable) string {
	var style = renderer.NewStyle().
		Width(16).
		Align(lipgloss.Center).
		Foreground(lipgloss.Color("8"))
	var selectedStyle = style.Copy().
		Foreground(lipgloss.Color("0")).
		Background(lipgloss.Color("3"))

	var tabs = []string{}
	for i := range NUM_TABLE {
		tab := TableName(SelectedTable(i))
		if SelectedTable(i) == selected {
			tab = selectedStyle.Render(tab)
		} else {
			tab = style.Render(tab)
		}
		tabs = append(tabs, tab)
	}

	return strings.Join(tabs, "")
}
