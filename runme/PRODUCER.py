import sys
import os
import math


DrawList=[]
ProjectX=[-1.5, -.5, .5, 1.5]
CellDimension=5

def GetMapString(s):
    result = ""
    for i in xrange(0,16):
        result = result + "\\\"" + s[i] + "\\\""
        if (i != 15):
            result = result + ","
    return result

def StrL_to_IntL(s):
    intermediate = s.split()
    result = []
    for a in intermediate:
        result.append(int(a))
    return result

def getX(n):
    return ProjectX[n % 4]

def getY(n):
    result = -1.5

    if (n <= 11):
        result =-.5
    if (n <= 7):
        result =.5
    if (n <= 3):
        result =1.5
    return result

def decideString(n):
    if (n == 1):
        return "\\drawone"
    if (n == 3):
        return "\\drawthree"
    if (n == 4):
        return "\\drawfour"
    if (n == 5):
        return "\\drawfive"
    if (n == -1):
        return "\\drawnegone"
    if (n == -3):
        return "\\drawnegthree"
    if (n == -4):
        return "\\drawnegfour"
    if (n == -5):
        return "\\drawnegfive"

def CreateDrawArrows(l):
    result = ""
    length = len(l)
    for i in xrange(0, length-1):
        diff = l[i+1] - l[i]
        startx=getX(l[i])
        starty=getY(l[i]) 
        endx=getX(l[i+1])
        endy=getY(l[i+1])

        midx = (startx+endx)/2 * CellDimension
        midy = (starty+endy)/2 * CellDimension

        cmdstring = decideString(diff) + "{" + str(midx) + "}{" + \
        str(midy) + "}"

        result = result + cmdstring

        if (i == 0):
            result = "\\drawinitialcell{" + str(startx*CellDimension) \
                     + "}{" + str(starty*CellDimension) + "}" + result
        if (i == length-2):
            result = result + "\\drawfinalcell{" + str(endx*CellDimension) \
                     + "}{" + str(endy*CellDimension) + "}"

    return "\\def\\drawarrows{" + result + "}"

f = open(sys.argv[1], 'r')

mylines = f.readlines();
numlines = len(mylines)
BoardString = "\\def\BOARD{{" + GetMapString(mylines[0]) + "}}"

for j in xrange(1,numlines):
    DrawList.append(CreateDrawArrows(StrL_to_IntL(mylines[j])))

numArrowLines=len(DrawList)
produceDocumentString = "\\def\\PRODUCEDOCUMENT{"

for j in xrange(0, numArrowLines):
    produceDocumentString = produceDocumentString + DrawList[j] \
                            + "\\PRODUCEPAGE"

produceDocumentString = produceDocumentString + "}"

cmdstring = BoardString + produceDocumentString + "\\input{boilerplate}"
#print cmdstring

os.system("pdflatex \"" + cmdstring + "\"")
os.system("evince --presentation boilerplate.pdf")
exit()


