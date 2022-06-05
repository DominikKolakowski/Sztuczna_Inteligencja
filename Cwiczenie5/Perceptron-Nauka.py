def perceptronNauka(percep, przyklady, alpha):
  while (True):
    poprawnePrzyklady = [0, 0, 0, 0]
    for przyklad in range(len(przyklady)):
      z = percep[0]
      for i in range(len(percep)-1):
        z += przyklady[przyklad][i] * percep[i+1]
      if (z < 0):
        z = 0
      else:
        z = 1

      if (przyklady[przyklad][2] != z):
        percep[0] = percep[0] + alpha * (przyklady[przyklad][2] - z) * 1
        for i in range (1, 3):
          percep[i] = percep[i] + alpha * (przyklady[przyklad][2] - z) * przyklady[przyklad][i-1]
      else:
        poprawnePrzyklady[przyklad] = 1

    if 0 not in poprawnePrzyklady:
      break

  for i in range (len(percep)):
    percep[i] = round(percep[i], 2)
  return percep

if __name__ == '__main__':
  x = [(0, 0, 0), (0, 1, 0), (1, 0, 1), (1, 1, 0)]

  wskaznikNauczania = 0.1
  wagi = [0.5, 0.5, 0.5]

  print("x1   x2    (x1 ^ ~x2)")
  print("0    0         0")
  print("0    1         0")
  print("1    0         1")
  print("1    1         0\n")
  
  print("w0 + w1 * 0 + w2 * 0 < 0  => w0 < 0")
  print("w0 + w1 * 0 + w2 * 1 < 0  => w2 < -w0")
  print("w0 + w1 * 1 + w2 * 0 >= 0 => w1 >= -w0")
  print("w0 + w1 * 1 + w2 * 1 < 0  => w1 + w2 < -w0")

  print("\nWagi startowe:\n", wagi)
  print("Szybkość nauki wynosi: ", wskaznikNauczania)
  wagi2 = perceptronNauka(wagi, x, wskaznikNauczania)
  print("Wagi według algorytmu perceptronu:\n", wagi2)
