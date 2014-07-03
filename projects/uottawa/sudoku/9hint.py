import sudoku
import sys
import signal

#sys.setrecursionlimit(1500)

class TimeoutException(Exception): 
	pass

def check(bb):

        board = sudoku.DLXsudoku(bb, 3)
        solCount = 0
        it = board.solve()
        try:
                next(it)
                solCount += 1
                next(it)
                solCount += 1
        except :
                pass
        if solCount == 0:
                o.write("No solutions!\n")
        elif solCount == 1:
                o.write("Unique solution!\n")
        else:
                o.write("Ambiguous solutions!\n")

def invalid(bb):
        # sebastian's code doesn't like sudokus with invalid hints.
        # a sudoku is invalid if it has the same symbol in any column, row, or block
        # 9 is the repeated symbol. the two nines are invalid if
        #
        i1 = 0
        j1 = 0
        i2 = 0
        j2 = 0
        foundFirst=False
        for i in range(9):
             for j in range(9):
                     if (bb[j*9 + i] == '9'):
                             if(not foundFirst):
                                     i1 = i
                                     j1 = j
                                     foundFirst = True
                             else:
                                     i2=i
                                     j2=j
        #now (i1,j1) and (i2,j2) are the positions of the two nines
        if (i1 == i2): #same row
                return True
        elif(j1 == j2): #same col
                return True
        elif(i1/3 == i2/3 and j1/3 == j2/3): #same block
                return True
        else:
                return False
                            
             
                    


f = open('newinput9.txt', 'r')
o = open('newoutput9.txt', 'w')
boards = f.readlines()


progress = 0
for b in boards:
	progress = progress + 1
	print str( progress) + ' / 15935'
	one = 1 #these are the ones that are repeated
	two = 2
	done = False
	while not(done):
                if not(one==8 and two==9):
                        count = 1
                        number = 1
                        bb = ""
                        for n in range(len(b) -2):
                                if b[n] == '0':
                                        bb += '0'
                                if b[n] != '0':
                                        if count==one or count==two:
                                                bb += str(9)
                                        else:
                                                bb += str(number)
                                                number = number + 1
                                        count = count + 1
                        o.write(bb + '\n')
                        #print bb
                        if not(invalid(bb)):
                                check(bb)
                        else:
                                o.write('Invalid hints: no solutions \n')
                                #print 'Invalid hints: no solutions'
                        if two<9:
                                two = two + 1
                        elif two==9:
                                one = one + 1
                                two = one + 1
                else: #this is the last one, no pairs this time
                        done=True
                        count = 1
                        bb = ""
                        for n in range(len(b) -2):
                                if b[n] == '0':
                                        bb += '0'
                                if b[n] != '0':
                                        bb += str(count)
                                        count = count + 1
                        o.write(bb + '\n')
                        #print bb
                        check(bb) #couldn't possibly be invalid

f.close()
o.close()
