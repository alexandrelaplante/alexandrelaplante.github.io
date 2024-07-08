package main

import (
	"fmt"
	"github.com/charmbracelet/lipgloss"
)

type SelectedCharisma int

const (
	CH_UNDER_5 = iota
	CH_UNDER_7
	CH_UNDER_10
	CH_UNDER_15
	CH_UNDER_17
	CH_UNDER_18
	CH_OVER_18
	NUM_CH
)

func RizzName(s SelectedCharisma) string {
	switch s {
	case CH_UNDER_5:
		return "<=5"
	case CH_UNDER_7:
		return "6-7"
	case CH_UNDER_10:
		return "8-10"
	case CH_UNDER_15:
		return "11-15"
	case CH_UNDER_17:
		return "16-17"
	case CH_UNDER_18:
		return "18"
	case CH_OVER_18:
		return ">=19"
	}
	return ""
}

func RizzValue(s SelectedCharisma) int {
	switch s {
	case CH_UNDER_5:
		return 0
	case CH_UNDER_7:
		return 6
	case CH_UNDER_10:
		return 8
	case CH_UNDER_15:
		return 11
	case CH_UNDER_17:
		return 16
	case CH_UNDER_18:
		return 18
	case CH_OVER_18:
		return 19
	}
	return 0
}

var rizzStyle = lipgloss.NewStyle().Foreground(lipgloss.Color("3"))
var numStyle = lipgloss.NewStyle().
	Background(lipgloss.Color("3")).
	Foreground(lipgloss.Color("0")).
	Width(6)
var arrowStyle = lipgloss.NewStyle().
	Background(lipgloss.Color("3")).
	Foreground(lipgloss.Color("0")).
	Width(1)

func RenderCharisma(selected SelectedCharisma) string {
	return fmt.Sprintf("%s %s%s",
		rizzStyle.Render("Charisma:"),
		numStyle.Render(RizzName(selected)),
		arrowStyle.Render("↕"),
	)
}
