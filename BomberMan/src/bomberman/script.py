import random

xMax = 15
yMax = 13

# numero totale di bush 130

posSbagliate = [
    [0, 0], [0,1], [0,2], [1,0], [2, 0],
    [14, 12], [14, 11], [14, 10], [13, 12], [12, 12]
]

positions = []

def isValid(pos):
    if pos == [-1, -1]:
        return False

    # controllo se esiste gia in questa posizione
    if positions.count(pos) != 0:
        return False

    # controllo che non sia la posizione dei blocchi indistruttibili 
    if pos[0]%2 != 0 and pos[1]%2 != 0:
        return False
    
    if posSbagliate.count(pos) != 0:
        return False

    return True

while len(positions) < 120:

    valid = False
    pos = [-1, -1]

    while not isValid(pos):
        x = random.randint(0, 14)
        y = random.randint(0, 12)
        pos = [x, y]
        
    positions.append([x, y])
    

print(positions)

with open('map.txt','w') as f:    
    for pos in positions:
        f.write(str(pos[0]*64)+"-"+str(pos[1]*64)+"\n")