package main

import (
	"github.com/charmbracelet/lipgloss"
	"github.com/charmbracelet/lipgloss/table"
	"github.com/mitchellh/go-wordwrap"
	"strconv"
)

type SelectedTable int

const (
	TABLE_SCROLLS = iota
	TABLE_POTIONS
	TABLE_RINGS
	TABLE_WANDS
	TABLE_SPELLBOOKS
	// TABLE_ARMOR
	NUM_TABLE
)

type data struct {
	base int
	text string
}

var border = lipgloss.Border{
	Top:          "█",
	Bottom:       "─",
	Left:         "│",
	Right:        "│",
	TopLeft:      "█",
	TopRight:     "█",
	BottomLeft:   "╰",
	BottomRight:  "╯",
	MiddleLeft:   "├",
	MiddleRight:  "┤",
	Middle:       "█",
	MiddleTop:    "█",
	MiddleBottom: "┴",
}

var scrolls = []data{
	{20, "identify"},
	{50, "light"},
	{60, "blank paper (\"unlabeled scroll\"), enchant weapon"},
	{80, "enchant armor, remove curse"},
	{100, "confuse, destroy armor, fire, food detect, gold detect, mm, scare, teleport"},
	{200, "amnesia, create monster, earth, taming"},
	{300, "charging, genocide, punishment, stinking cloud"},
}

var potions = []data{
	{0, "water (uncursed)"},
	{50, "booze, fruit juice, see invisible, sickness"},
	{100, "confusion, extra healing, hallucination, healing, holy water (\"blessed clear potion\"), unholy water (\"cursed clear potion\"), restore ability, sleeping"},
	{150, "blindness, gain energy, invisibility, monster detection, object detection"},
	{200, "enlightenment, full healing, levitation, polymorph, speed"},
	{250, "acid, oil"},
	{300, "gain ability, gain level, paralysis"},
}

var rings = []data{
	{100, "adornment, hunger*, protection, protection from shape changers, stealth, sustain ability, warning"},
	{150, "aggravate monster*, cold resistance, gain constitution, gain strength, increase accuracy, increase damage, invisibility, poison resistance, see invisible, shock resistance"},
	{200, "fire resistance, free action, levitation, regeneration, searching, slow digestion, teleportation*"},
	{300, "conflict, polymorph*, polymorph control, teleport control"},
}

var wands = []data{
	{100, "light, nothing"},
	{150, "digging, enlightenment, locking, magic missile, make invisible, opening, probing, secret door detection, slow monster, speed monster, striking, undead turning"},
	{175, "cold, fire, lightning, sleep"},
	{200, "cancellation, create monster, polymorph, teleportation"},
	{500, "death, wishing"},
}

var spellbooks = []data{
	{100, "Level 1 books: force bolt, protection, detect monsters, light, sleep, jumping, healing, knock"},
	{200, "Level 2 books: magic missile, drain life, create monster, detect food, confuse monster, slow monster, cure blindness, wizard lock"},
	{300, "Level 3 books: remove curse, clairvoyance, detect unseen, identify, cause fear, charm monster, haste self, cure sickness, extra healing, stone to flesh"},
	{400, "Level 4 books: cone of cold, fireball, detect treasure, invisibility, levitation, restore ability"},
	{500, "Level 5 books: magic mapping, dig"},
	{600, "Level 6 books: create familiar, turn undead, teleport away, polymorph"},
	{700, "Level 7 books: finger of death, cancellation"},
}

func RenderTable(charisma int, price int, tableType int) string {
	var selection []data
	switch tableType {
	case TABLE_SCROLLS:
		selection = scrolls
	case TABLE_POTIONS:
		selection = potions
	case TABLE_RINGS:
		selection = rings
	case TABLE_WANDS:
		selection = wands
	case TABLE_SPELLBOOKS:
		selection = spellbooks
	}
	if selection == nil {
		return ""
	}

	var highlighted = [][2]int{}
	t := table.New().
		Border(lipgloss.RoundedBorder()).
		BorderStyle(lipgloss.NewStyle().Foreground(lipgloss.Color("3"))).
		StyleFunc(func(row, col int) lipgloss.Style {
			style := lipgloss.NewStyle()
			if row%2 == 0 {
				style = style.Foreground(lipgloss.Color("11"))
			}
			if col == 3 {
				style = style.MaxWidth(80)
			}
			if row != len(selection) {
				style = style.MarginBottom(1)
			}
			for _, h := range highlighted {
				if h[0] == row && h[1] == col {
					style = style.
						Background(lipgloss.Color("3")).
						Foreground(lipgloss.Color("0"))
				}
			}
			return style
		}).
		Headers("BASE", "BUY", "SELL", "TYPE")

	for i, d := range selection {
		buyPrice := GetBuyPrice(d.base, charisma)
		sellPrice := GetSellPrice(d.base)

		newCol2Style := lipgloss.NewStyle().Width(17)
		newCol3Style := lipgloss.NewStyle().Width(13)

		if buyPrice.main == price || buyPrice.sucker == price || buyPrice.suckest == price {
			highlighted = append(highlighted, [2]int{i + 1, 1})
		}
		if sellPrice.main == price || sellPrice.sucker == price {
			highlighted = append(highlighted, [2]int{i + 1, 2})
		}

		t.Row(
			strconv.Itoa(d.base),
			newCol2Style.Render(buyPrice.String()),
			newCol3Style.Render(sellPrice.String()),
			wordwrap.WrapString(d.text, 80),
		)
	}
	return t.Render() + "\nAn asterisk (*) indicates that this ring is generated cursed 90% of the time."
}
