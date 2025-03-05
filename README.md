# firstAIProject
messing around with AI agents and npuzzles
this program works by generating a random (but solveable) 8 puzzle. after this it creates a search tree where each node is a move. Currently there are two search algorithms. Breadth first searches every node, one depth at a time. this finds the fastest route but is inefficient space wise. It also implements A* wich used the manhatten heuristic to choose what states of the nPuzzle are the most promsing, and develops those first to get to an answer. I also implemented a solver, so you can watch the AI solve the 8 puzzle in real time.

currently, the implementation works but only for 8 puzzles. In the next version, I will implement a 1D array system that shoud be easier on memory. I will also implement a better heuristic function. I am thinking this will be IDA* I saw someone use this to solve a 4x4 15 puzzle so obviously im gonna try too.
