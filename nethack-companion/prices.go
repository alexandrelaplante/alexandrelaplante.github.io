package main

import "fmt"

type BuyPrice struct {
	main    int
	sucker  int
	suckest int
}

func (p BuyPrice) String() string {
	return fmt.Sprintf("%v (%v/%v)", p.main, p.sucker, p.suckest)
}

type SellPrice struct {
	main   int
	sucker int
}

func (p SellPrice) String() string {
	return fmt.Sprintf("%v (%v)", p.main, p.sucker)
}

func GetBuyPrice(base int, charisma int) BuyPrice {
	if base < 5 {
		base = 5
	}
	var price float64
	if charisma <= 5 {
		price = float64(base) * 2
	} else if charisma <= 7 {
		price = float64(base) * 3 / 2
	} else if charisma <= 10 {
		price = float64(base) * 4 / 3
	} else if charisma <= 15 {
		price = float64(base)
	} else if charisma <= 17 {
		price = float64(base) * 3 / 4
	} else if charisma <= 18 {
		price = float64(base) * 2 / 3
	} else if charisma >= 19 {
		price = float64(base) / 2
	}
	return BuyPrice{
		int(price + .5),
		int(price*4/3 + .5),
		int(price*16/9 + .5),
	}
}

func GetSellPrice(base int) SellPrice {
	return SellPrice{
		int(float64(base)/2 + .5),
		int(float64(base)*3/8 + .5),
	}
}
