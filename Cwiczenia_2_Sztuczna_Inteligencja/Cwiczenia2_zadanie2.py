import numpy as np

class Node:
    def __init__(self, array, parent=None):
        
        self.array = array
        self.parent = parent
        self.g = 0
        self.h = self.heuristic()
        self.f = self.g + self.h

    def print_array(self):
        print(self.array)

    def heuristic(self):
        goal_array = np.array([[1, 2, 3],
                               [4, 5, 6],
                               [7, 8, 0]])
        result = 0
        for x in range(1, 9):
            end_x, end_y = np.where(goal_array == x)
            current_x, current_y = np.where(self.array == x)
            result = result + abs(end_x - current_x) + abs(end_y - current_y)
        return int(result)

    def update_heuristic(self):
        self.h = self.array.heuristic()
        self.f = self.g + self.h

    def __lt__(self, other):
        return self.f < other.f

def add_to_open(open_list, node):
    for x in open_list:
        if np.all(node.array == x.array) and node.f >= x.f:
            return False
    return True

def astar(initial_array):

    goal_array = np.array([[1, 2, 3],
                           [4, 5, 6],
                           [7, 8, 0]])
    start_node = Node(initial_array)
    end_node = Node(goal_array)
    open_list = []
    closed_list = []
    open_list.append(start_node)
    while len(open_list) > 0:
        open_list.sort()
        current_node = open_list.pop(0)
        closed_list.append(current_node)
        if np.all(current_node.array == end_node.array):
            path = []
            while current_node != start_node:
                path.append(current_node)
                current_node = current_node.parent
            return path

        neighbors = []
        blank_x, blank_y = np.where(current_node.array == 0)
        blank_x = int(blank_x)
        blank_y = int(blank_y)
        if 0 <= blank_y - 1 <= 2:
            left_array = current_node.array.copy()
            left_array[blank_x][blank_y] = current_node.array[blank_x][blank_y-1]
            left_array[blank_x][blank_y - 1] = 0
            neighbors.append(Node(left_array, current_node))
        
        if 0 <= blank_x + 1 <= 2:
            top_array = current_node.array.copy()
            top_array[blank_x][blank_y] = current_node.array[blank_x + 1][blank_y]
            top_array[blank_x + 1][blank_y] = 0
            neighbors.append(Node(top_array, current_node))

        if 0 <= blank_y + 1 <= 2:
            right_array = current_node.array.copy()
            right_array[blank_x][blank_y] = current_node.array[blank_x][blank_y + 1]
            right_array[blank_x][blank_y + 1] = 0
            neighbors.append(Node(right_array, current_node))

        if 0 <= blank_x - 1 <= 2:
            bottom_array = current_node.array.copy()
            bottom_array[blank_x][blank_y] = current_node.array[blank_x - 1][blank_y]
            bottom_array[blank_x - 1][blank_y] = 0
            neighbors.append(Node(bottom_array, current_node))

        for neighbors in neighbors:
            for nodes in closed_list:
                if np.all(nodes.array == neighbors.array):
                    continue
            if add_to_open(open_list, neighbors):
                open_list.append(neighbors)

if __name__ == '__main__':

    initial_array = np.array([[0, 1, 3],
                              [4, 2, 5],
                              [7, 8, 6]])
    final_path = astar(initial_array)
    path_length = len(final_path)
    final_path = reversed(final_path)
    print(initial_array,"\n")
    for node in final_path:
        print(node.array,"\n")
    print("Path is {0} steps long".format(path_length))