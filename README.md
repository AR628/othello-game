# Othello Project
A fully-functioning Othello, where a user can play Othello against an AI agent using the minimax algorithm and alpha-beta pruning. Built solely using Java and its Swing library. An earlier version of this project was built for my *CIS 120: Programming Languages & Techniques* class; I then added the AI mode afterwards. 

<img width="795" alt="image" src="https://user-images.githubusercontent.com/36567631/180250212-01fe762a-092a-49c8-a846-376816c3ebb2.png">

## How to Run
1. Clone this repository.
2. Run [Game.java](Game.java).

## Project Layout
This project uses a Model-View Controller layout. 
![Visual description of MVC](https://developer.mozilla.org/en-US/docs/Glossary/MVC/model-view-controller-light-blue.png)
* Model: [Othello.java](Othello.java), which stores the internal game state using a 2D array, and has the AI agent.
* View: [RunOthello.java](RunOthello.java), which displays the game on the user's screen.
* Controller: [OGameBoard.java](OGameBoard.java), which sets listeners for when the user clicks a square or a button.

To save the game state, every legal move is recorded in [GameState.txt](GameState.txt). When the application is quit and then rerun, this file and its moves are read automatically by [FileLineIterator.java](FileLineIterator.java).