import math

class node():
  def __init__(self, value=None, parent=None):
    self.value = value
    self.parent = parent
    self.kid = []
    self.alpha = -math.inf
    self.beta = math.inf
    if parent != None:
      parent.kid.append(self)

def minimum(node):
  for kid in node.kid:
    if kid.value > node.alpha:
      node.alpha = kid.value
      if node.alpha > node.parent.beta:
        return
      node.parent.alpha = node.alpha
  node.beta = node.alpha
  node.parent.beta = node.beta

def maximumminimum(root):
  for kid in root.kid:
    minimum(kid)

if __name__ == "__main__":
  A = node(None, None)
  B = node(None, A)
  C = node(None, A)
  D = node(None, A)

  E = node(3, B)
  F = node(13, B)
  G = node(9, B)

  H = node(2, C)
  I = node(4, C)
  J = node(8, C)

  K = node(18, D)
  L = node(8, D)
  M = node(4, D)

  maximumminimum(A)
  print ("A: [{0}, {1}]".format(A.alpha, A.beta))
  print ("B: [{0}, {1}]".format(B.alpha, B.beta))
  print ("C: [{0}, {1}]".format(C.alpha, C.beta))
  print ("D: [{0}, {1}]".format(D.alpha, D.beta))