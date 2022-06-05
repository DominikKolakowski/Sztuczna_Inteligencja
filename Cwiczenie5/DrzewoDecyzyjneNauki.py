import numpy
from sklearn import tree
import matplotlib.pyplot as plt
import math

def entropy(S):
    p = len(S)
    p_pozytywny = 0
    p_negatywny = 0
    for x in S:
        if x:
            p_pozytywny = p_pozytywny + 1
        else:
            p_negatywny = p_negatywny + 1
    p_pozytywny = p_pozytywny/p
    p_negatywny = p_negatywny/p

    if p_pozytywny == 0 or p_negatywny == 0 or p_pozytywny == 1 or p_negatywny == 1:
        return 0

    return round(-p_pozytywny * math.log2(p_pozytywny) - (p_negatywny * math.log2(p_negatywny)), 3)

def zdobyte_informacje(S, S1, S0):
    wynik = round(entropy(S) - (len(S1)/len(S)) * entropy(S1) - (len(S0)/len(S)) * entropy(S0), 3)

    print(entropy(S), "-", len(S1), "/", len(S), "*", entropy(S1), "-", len(S0), "/", len(S), "*", entropy(S0), "=", wynik)

    return wynik

if __name__ == "__main__":
    V = numpy.array([[1, 0, 0],
        [1, 0, 1],
        [0, 1, 0],
        [1, 1, 1],
        [1, 1, 0]])

    c = [0, 0, 0, 1, 1]

    for a in range(len(V[0])):
        S1 = [c[x] for x in range(len(c)) if V[:, a][x] == 1]
        S0 = [c[x] for x in range(len(c)) if V[:, a][x] == 0]

        print("Obliczanie dla atrybutu a{0}:".format(a+1))

        print("Entropia dla zestawu:", entropy(c), end="")
        print("   Entropia dla podzbioru dodatniego:", entropy(S1), end="")
        print("   Entropia dla zbioru ujemnego:", entropy(S0))

        print("Zdobyte informacje:")
        zdobyte_informacje(c, S1, S0)
        print("")

    print("Atrybut a2 nalezy wybrac, poniewaz jego zysk informacyjny jest najwiekszy.")
    print("")
    print("Obliczanie nastepnego poziomu:")
    c1 = [c[x] for x in range(len(c)) if V[:, 1][x] == 1]

    for a in [0, 2]:
        S1 = [c1[x] for x in range(len(c1)) if V[2:, a][x] == 1]
        S0 = [c1[x] for x in range(len(c1)) if V[2:, a][x] == 0]

        print("Obliczanie dla atrybutu a{0}:".format(a+1))

        print("Entropia dla zestawu:", entropy(c1), end="")
        print("   Entropia dla podzbioru dodatniego:", entropy(S1), end="")
        print("   Entropia dla zbioru ujemnego:", entropy(S0))

        print("Zdobyte informacje:")
        zdobyte_informacje(c1, S1, S0)
        print("")

    print("Atrybut a1 nalezy wybrac, poniewaz jego zysk informacyjny jest najwiekszy.")
    print("")

    clf = tree.DecisionTreeClassifier(criterion = 'entropy')
    clf = clf.fit(V, c)

    #tekst repres
    text = tree.export_text(clf, feature_names = ["a1", "a2", "a3"])
    print("Reprezentacja tekstowa drzewa:")
    print(text)

    #wykres drzewa
    tree.plot_tree(clf, feature_names = ["a1", "a2", "a3"], filled=True)

    plt.show()