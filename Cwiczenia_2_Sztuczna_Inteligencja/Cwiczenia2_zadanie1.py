class Room:
    def __init__ (self, name, isDirt=1):
        self.name = name
        self.isDirt = isDirt

class Robot:
    def __init__ (self):
        self.room_left = Room("A")
        self.room_right = Room("B")
        self.room_current = self.room_left

    def go_left(self):
        if self.room_current == self.room_right:
            self.room_current = self.room_left

    def go_right(self):
        if self.room_current == self.room_left:
            self.room_current = self.room_right

    def clean(self):
        self.room_current.isDirt = 0

    def auto(self):
        counter = 0
        while self.room_left.isDirt == 1 or self.room_right.isDirt == 1:
            if self.room_current.isDirt:
                self.clean()
            elif self.room_current == self.room_left:
                self.go_right()
            elif self.room_current == self.room_right:
                self.go_left
            counter = counter + 1
            print("Step: {0}".format(counter))
            print(self)
        return counter

    def __str__(self):
        return "Obecny Room: {0}, {1} dirty: {2}, {3} dirty: {4}".format(self.room_current.name,
                                                                        self.room_left.name, self.room_left.isDirt,
                                                                        self.room_right.name, self.room_right.isDirt)
class State:
    def __init__ (self, room_current, dirt_on_left, dirt_on_right):
        self.room_current = room_current
        self.dirt_on_left = dirt_on_left
        self.dirt_on_right = dirt_on_right
    def __str__(self):
        return "Robot in Room: {0}, dirty w A: {1}, dirty w B: {2}".format(self.room_current, self.dirt_on_left, self.dirt_on_right)

class Graph:
    
    def __init__ (self):
        self.A = State("A", 1, 1)
        self.B = State("B", 1, 1)
        self.C = State("A", 0, 1)
        self.D = State("B", 1, 0)
        self.E = State("B", 0, 1)
        self.F = State("A", 1, 0)
        self.G = State("B", 0, 0)
        self.H = State("A", 0, 0)

        self.graph = {
            self.A: [self.B, self.C],
            self.B: [self.A, self.D],
            self.C: [self.A, self.E],
            self.D: [self.B, self.F],
            self.E: [self.C, self.G],
            self.F: [self.D, self.H],
            self.G: [self.H],
            self.H: [self.G]
        }
        self.visited = []
        self.queue = []

    def bfs(self):
        self.visited.append(self.A)
        self.queue.append(self.A)
        while self.queue:
            s = self.queue.pop(0)
            print(s)
            for neighbour in self.graph[s]:
                if neighbour not in self.visited:
                    self.visited.append(neighbour)
                    self.queue.append(neighbour)

if __name__ == '__main__':
    g = Graph()
    g.bfs()