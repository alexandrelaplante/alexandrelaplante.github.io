import sudoku

f = open('input9.txt', 'r')
boards = f.readlines()

for b in boards:
        count = 1
        bb = ""
        for n in range(len(b)):
                if b[n] == '0':
                        bb += '0'
                if b[n] != '0':
                        bb += str(count)
                        count = count + 1
        print(bb)
        board = sudoku.DLXsudoku(bb, 3)
        solCount = 0
        it = board.solve()
        try:
                next(it)
                solCount += 1
                next(it)
                solCount += 1
        except:
                pass
        if solCount == 0:
                print("No solutions!")
        elif solCount == 1:
                print("Unique solution!")
        else:
                print("Ambiguous solutions!")



f.close()
