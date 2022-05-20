# Othello Project
A fully-functioning Othello, where two users can play this turn-by-turn game and save their game state at any time. Built solely using Java and its Swing library. This was my final project in my *CIS 120: Programming Languages & Techniques* class.

![Screenshot of project](https://user-images.githubusercontent.com/36567631/169464193-4750ea9a-da18-421c-bbbe-a77567ebdee2.png)

## How to Run
1. Clone this repository.
2. Run [Game.java](Game.java).

## Project Layout
This project uses a Model-View Controller layout. 
![Visual description of MVC](https://developer.mozilla.org/en-US/docs/Glossary/MVC/model-view-controller-light-blue.png)
* Model: [Othello.java](Othello.java), which stores the internal game state using a 2D array.
* View: [RunOthello.java](RunOthello.java), which displays the game on the user's screen.
* Controller: [OGameBoard.java](OGameBoard.java), which sets listeners for when the user clicks a square or a button.

To save the game state, every legal move is recorded in [GameState.txt](GameState.txt). When the application is quit and then rerun, this file and its moves are read automatically by [FileLineIterator.java](FileLineIterator.java).