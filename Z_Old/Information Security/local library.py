######## Functions
# Turns a letter into a base-26 number.
ALPHABET="ABCDEFGHIJKLMNOPQRSTUVWXYZ"

exec(open("./letterfrequency.py").read())
# ASSUMES VALID, ALL-CAPS LETTERS
def charOrderBase26(letter):
	return (ord(letter) - 65) % 26

# Converts a number back to a character.
def numberToCharBase26(number):
	# 65 is 'A' in ASCII
	return chr(number + 65)

# Turns the text into a list of values.
def textToValues(text):
	return list(map(charOrderBase26, list(text)))

def valuesToText(numberList):
	return ''.join(list(map(numberToCharBase26, numberList)))

def encrypt(key, text):
	encrypted = []
	# Convert to numbers.
	key  = textToValues(key)
	text = textToValues(text)
	keyLength = len(key)
	for index,letter in enumerate(text):
		# Calculate the new letter's value
		encryptedValue = (letter + key[index % keyLength]) % 26
		encrypted.append(encryptedValue)
	# Get back the letters!
	return valuesToText(encrypted)

# Decrypt text by given key
def decrypt(key, encrypted_text):
	decrypted = []
	# Convert to numbers.
	key = textToValues(key)
	encrypted_text = textToValues(encrypted_text)
	keyLength = len(key)
	for index,letter in enumerate(encrypted_text):
		# Calculate the new letter's value
		decryptedValue = ((letter - key[index % keyLength]) + 26 ) % 26
		decrypted.append(decryptedValue)
	# Get back the letters!
	return valuesToText(decrypted)

# Gets the relative letter frequency in a given text.
def getLetterFrequencies(text):
	textLength = len(text)
	# Initialize all values to 0.
	frequencies = {}
	for letter in list(ALPHABET):
		frequencies[letter] = 0
	# Count letters.
	for letter in list(text):
		frequencies[letter] += 1
	# Get the relative frequency

	for letter in frequencies.keys():
		frequencies[letter] = (float(frequencies[letter]) / textLength)
	return frequencies

# Calculates the possiblity (X) that a letter solves the given
# text's encryption according to formula:
# X = SUM[letter in Alphbet] f_a * n_a
# Where f_a is a letter's global frequency and
# n_a is the letter frequency in the text
def possiblityOfKeyLetter(cypher, letter):
	plaintext = decrypt(letter, cypher)
	local_frequency = getLetterFrequencies(plaintext)

	possiblity = 0
	for alphaletter in LETTERFREQUENCY.keys():
		possiblity += LETTERFREQUENCY[alphaletter] * local_frequency[alphaletter]

	return possiblity

# Find the most probable 1-letter solution to a cypher using letter frequencies.
def findPossibleKeyLetterMatches(cypher):
	possibilites = {}
	for letter in list(ALPHABET):
		possibilites[letter] = possiblityOfKeyLetter(cypher, letter)
	# Sort by value.
	return sorted(possibilites, key=possibilites.__getitem__, reverse=True)

def breakEncryption(cypher, keyLength):
	# Create cypher columns by key length
	groups = []
	for index in range(0, keyLength):
		groups.append([])

	for index, letter in (enumerate(cypher)):
	    groups[index % keyLength] += letter

	best_guess= ""
	all_options = []
	for index, group in enumerate(groups):
	    # Take the first guess
		possibilites = findPossibleKeyLetterMatches(group)
		best_guess += possibilites[0]
		all_options.append([possibilites[0],  possibilites[1]])

	print(best_guess)
	# run over all the possiblites' permutations
	return decrypt(best_guess, cypher)



def len_of_key(ciphertext):
    icTable = {}
    for num in range(1,20):
        ic=0
        for x in range(1,num+1):
            ic= ic + Index_of_Coincidence(x,ciphertext)
        icTable[num] = ic/num

    ans= -1
    cur=0
    pre=10000
    for x in icTable:
	print(icTable[x])
        cur=icTable[x]-1.73
        if(abs(cur)<abs(pre)):
            ans=x
            pre=cur

    return ans


def Index_of_Coincidence(num, ciphertext):
    index = 1
    table = []
    for letter in ciphertext:
        if(index%num==0):
            table.append(letter)
        index+=1

    n = len(table)
    frequencyTable = frequency(table)

    freqs = 0
    for letter in frequencyTable:
        freqs = freqs + frequencyTable[letter]*(frequencyTable[letter]-1)
	ic = float(freqs)/(n*(n-1)/26)

    return (ic)

def frequency(ciphertext):
    letterCount={}
    for letter in ALPHABET :
        letterCount[letter]=0
    for letter in ciphertext:
        letterCount[letter]+=1
    return letterCount
