from fractions import gcd
debug = True

#Task 3

#gcd stuff... equals to pow(x,y,mod) = x^y
# Prints every step
def getProd(number, exp, mod):
    print("repetitive squaring", number, exp, mod)
    prod = 1
    while exp != 1:
        if(exp % 2 != 0):
            exp -= 1
            prod = prod * number
            prod = prod % mod
            print("{} * {}^{}".format(prod, number, exp))
        exp = exp / 2
        number = number**2
        number = number % mod
        if (debug):
            print("{} * {}^{}".format(prod, number, exp))

    prod = prod * number
    prod = prod % mod
    return prod


#Task 6

def phi(p,q):
    return (p-1) * (q-1)

def extendedEuclid(a,b):
    if (debug):
        print("Starting euclid with params: {} {}".format(a,b))
    #b is the larger number
    b,a = max(a,b), min(a,b)
    # Format of euclidList is for back-substitution
    euclidList=[[b%a,1,b,-1*(b//a),a]]
    while b % a > 0:
        # C0 = C1 * C2 + R2
        # C2 = R2 * C3 + R3
        print("{} = {} * {} + {}".format(b, b//a, a, b % a))
        b,a = a, b % a
        euclidList.append([b % a, 1, b, -1 * (b // a), a])
    print("--- Extended: ")
    if len(euclidList) > 1:
        euclidList.pop()
        euclidList = euclidList[::-1]
        for i in range(1, len(euclidList)):
            euclidList[i][1] *= euclidList[i-1][3]
            euclidList[i][3] *= euclidList[i-1][3]
            euclidList[i][3] += euclidList[i-1][1]
            if (debug):
                expr=euclidList[i]
                strExpr=str(expr[1])+"*"+str(expr[2])+" + "+str(expr[3])+"*"+str(expr[4]) \
                    +" = "+str(euclidList[0][0])
                print(strExpr)


    expr = euclidList[len(euclidList) - 1]
    return expr

def getReverseNonPrime(e, p, q):
    public = phi(p,q)
    a = (extendedEuclid(e, public))[3] % public
    return a

def encrypt(message, key, public):
    return getProd(message, key, public)

print("=======================================================")

# public encryption key
e = 65537
#Alice
alice_p = 2000303
alice_q = 2000387
alice_public = alice_q * alice_p
alice_private = getReverseNonPrime(e, alice_p, alice_q)

#Bob
bob_p = 2000423
bob_q = 2000807
bob_public = bob_q * bob_p
bob_private = getReverseNonPrime(e, bob_p, bob_q)

message = 726310
signature = getProd(message, alice_private, alice_public)
print("s", signature)

menc = getProd(message, e, bob_pubfrom fractions import gcd
debug = True

#Task 3

#gcd stuff... equals to pow(x,y,mod) = x^y
# Prints every step
def getProd(number, exp, mod):
    print("repetitive squaring", number, exp, mod)
    prod = 1
    while exp != 1:
        if(exp % 2 != 0):
            exp -= 1
            prod = prod * number
            prod = prod % mod
            print("{} * {}^{}".format(prod, number, exp))
        exp = exp / 2
        number = number**2
        number = number % mod
        if (debug):
            print("{} * {}^{}".format(prod, number, exp))

    prod = prod * number
    prod = prod % mod
    return prod


#Task 6

def phi(p,q):
    return (p-1) * (q-1)

def extendedEuclid(a,b):
    if (debug):
        print("Starting euclid with params: {} {}".format(a,b))
    #b is the larger number
    b,a = max(a,b), min(a,b)
    # Format of euclidList is for back-substitution
    euclidList=[[b%a,1,b,-1*(b//a),a]]
    while b % a > 0:
        # C0 = C1 * C2 + R2
        # C2 = R2 * C3 + R3
        print("{} = {} * {} + {}".format(b, b//a, a, b % a))
        b,a = a, b % a
        euclidList.append([b % a, 1, b, -1 * (b // a), a])
    print("--- Extended: ")
    if len(euclidList) > 1:
        euclidList.pop()
        euclidList = euclidList[::-1]
        for i in range(1, len(euclidList)):
            euclidList[i][1] *= euclidList[i-1][3]
            euclidList[i][3] *= euclidList[i-1][3]
            euclidList[i][3] += euclidList[i-1][1]
            if (debug):
                expr=euclidList[i]
                strExpr=str(expr[1])+"*"+str(expr[2])+" + "+str(expr[3])+"*"+str(expr[4]) \
                    +" = "+str(euclidList[0][0])
                print(strExpr)


    expr = euclidList[len(euclidList) - 1]
    return expr

def getReverseNonPrime(e, p, q):
    public = phi(p,q)
    a = (extendedEuclid(e, public))[3] % public
    return a

#encrypts a message
def encrypt(message, key, public):
    return getProd(message, key, public)

print("=======================================================")

# public encryption key
e = 65537
#Alice
alice_p = 2000303
alice_q = 2000387
#alice_public = alice_q * alice_p
#alice_private = getReverseNonPrime(e, alice_p, alice_q)

#Bob
bob_p = 2000423
bob_q = 2000807
#bob_public = bob_q * bob_p
#bob_private = getReverseNonPrime(e, bob_p, bob_q)

message = 726310
#signature = getProd(message, alice_private, alice_public)
#print("s", signature)

#menc = getProd(message, e, bob_public)
#print("m,encrypted", menc)
#senc = getProd(signature, e, bob_public)
#print("s,encrypted", senc)

#mdec = getProd(menc, bob_private, bob_public)
#print("m,decrypted", mdec)
#sdec = getProd(senc, bob_private, bob_public)
#print("s,decrypted", sdec)

#print("verified", getProd(sdec, e, alice_public))

#Task 7
friendly_p = 53
friendly_q = 67
friendly_public_e = 1565
friendly_n = 3551
friendly_private_d = getReverseNonPrime(friendly_public_e, friendly_p, friendly_q)
print(friendly_private_d)
lic)
print("m,encrypted", menc)
senc = getProd(signature, e, bob_public)
print("s,encrypted", senc)

mdec = getProd(menc, bob_private, bob_public)
print("m,decrypted", mdec)
sdec = getProd(senc, bob_private, bob_public)
print("s,decrypted", sdec)

print("verified", getProd(sdec, e, alice_public))


#task 8

last = 1
for i in range (1, 1373):
    last = last * 2 % 1373
    print(i, last)
    if (last == 974):
        print("--------------------------------- found it")
