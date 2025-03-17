# firstAIProject
messing around with AI agents and npuzzles

currently have not implemented a better search function, but I did change the 2D string array into a 1D string array. this cut down on memory signicantly. the next step is to change the representation again to be an 1D int array with the open space represented as a -1. This cuts down on the number of String.valueOf and Integer.valueOf function calls wich are inefficient compared to simple computing of grabbing ints.

created weights for the A* to get different variant. this way you can search the state space with different search strategies.