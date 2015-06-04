# five-in-a-row
One or two-player GUI game where players try to place five tiles in a row.

Gameplay.

Set-up menu opens first.
  User chooses number of human players (AI vs AI, human vs AI or human vs human).
  Players enter names and choose colors.
  User chooses AI play style (aggressive, active or defensive).
  Screen representing the match-up is displayed.
  
Game board.
  10 by 10 grid is displayed with a textbox below showing the game report.
  Players take turns selecting squares on the grid.
  Game ends when one player has placed 5 tiles in a row (horizontal, vertical or diagonal) or the board is filled (tie).
  User can choose to play again or change settings.


Code.
Main class: set up for GUI menu, GUI game board and gameplay.
Player class: stores players' names, colors and whether player is human or AI.
Click class: used for button actions and placing colors on board.

AI class.
  Determines wins and ties.
  Chooses moves for AI player(s).
  Method for choosing best moves in best().
    Win if possible.
    Block other player from winning next turn if possible.
    Create 2 lines of 4 tiles if possible (win next turn).
    Prevent other player from creating 2 lines of 4 tiles.
    If none of above apply, evaluate score for each square.
      Uses score() method, which employs freq() to evaluate all lines of 5 squares before and after a possible move.
      Modified by the play style, which determines prioritization of offensive versus defensive moves.
    Choose from the reduced list of moves at random and play it.
  Code also included for recursive move prediction, but not used because it is inefficient and slow.
