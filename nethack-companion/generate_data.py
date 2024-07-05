base_prices = [20, 50, 60, 80, 100, 200, 300]

def buy_price(charisma: int, base: int) -> tuple[int, int, int]:
    if charisma <= 5:
        price = base*2
    if 6 <= charisma <= 7:
        price = base*3/2
    if 8 <= charisma <= 10:
        price = base*4/3
    if 11 <= charisma <= 15:
        price = base
    if 16 <= charisma <= 17:
        price = base*3/4
    if 18 <= charisma <= 18:
        price = base*2/3
    if 19 <= charisma:
        price = base/2
    return (int(.5 + price), int(.5 + price*4/3), int(.5 + price*16/9))


def sell_price(base: int) -> tuple[int, int]:
    return (int(.5 + base/2), int(.5 + base*3/8))


for base in base_prices:
    a, b = sell_price(base=base)
    print(f'{a} ({b})')
