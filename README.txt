=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D array:
  I used 2d array to store the board information. I implemented a 2d array of Square(s),
  which have a visibility and value field. It is an appropriate use of 2d array because
  it represents the grid nature of the board, where each square has adjacent squares
  horizontally, vertically, and diagonally. I also limited the use of 2d arrays by
  creating the Square class, which groups the coordinate, value, and visibility fields
  into one object.

  2. File I/O:
  I used File I/O to save the board information when the user wants to or when they quit
  the game. I simply saved all the game information like bomb count, uncovered count, and
  each board state into one text file. It is an appropriate use of the concept because users
  may want to save their game for later return, and saving the game information to a text
  file seems easiest.

  3. Recursion:
  I used recursion to implement the propagating feature when a user clicks an empty square.
  In game sweeper, when an empty square is revealed, all adjacent squares to it are also
  revealed, and if the adjacent squares are also empty, this process repeats

  4. JUnit testable component:
  The Minesweeper class contains all the isolated logic needed for minesweeper and does not
  rely on GUI components. I have also tried to separate the game logic into separate parts
  so that each part is small enough to be testable. For instance, for the solving algorithm,
  I separate the updateProbability methods from the getAdjacentBombProbability so that
  each is testable.


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
There are four classes in my game. RunMinesweeper is similar to the view in the MVC model.
It creates a frame, the buttons, statuses, and organize them on the screen. It also
adds event listeners to the buttons, but the logic of what each button does is delegated
to GameBoard. GameBoard is similar to the controller in the MVC model. First, it
handles the interactions between RunMinesweeper and the models (Minesweeper and Square).
For instance, when a square is clicked, the GameBoard calls the update functions of the
models to update the game's inner state. Not only that, it can make update the status
messages, and handles saving/loading game state from file. Lastly, my models are Square
and Minesweeper. These two models maintain the inner state of the game. Minesweeper stores
information like whether the game is over, the bomb count, flag count, the number of
uncovered squares, and a 2d-array of Squares. Each Square instance stores the coordinate
of the square (i for row and j for column), the value of the square (whether it is a bomb,
or the number of bombs adjacent to it if it is not a bomb), and whether the square has
been shown/flagged or not.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
A significant stumbling block for me was organizing the whole code, so that the solving
algorithms do not repeat much code. At one point, I had separate solving algorithms in
Minesweeper, but I realized solveLikePro and solveLikeAmateur had pretty similar structure,
just their updateProbability was different. I had the same problem in RunMinesweeper
with the solving feature - where they largely shared the same structure.
As such, I refractored the code so that the overlapping structure is eliminated by using
functional programming.
Another stumbling block I had was how to represent the information of the board.
At one point, I had 2 different 2d-arrays to store visibility and value, but I figured
to create a new model Square to capture all of this information. This helped my implementation
speed a lot because I don't have to think about accessing arrays all the time. Once I
had the Square instance, I know all values related to it.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
I think my design has good separation of functionality, and it clearly exemplifies the
MVC model. I think the private states are encapsulated thoroughly because all class states
were made private, but they all had getters and setters to them.
However, if given the chance, I would rethink my code in minesweeper a bit more because
there is a lot of functionalities that require checking all 8 squares around a square,
and right now I just have different methods that each have 8 if statements. Instead,
with functional programming, I can probably condense the code so that only method will
have the 8 if statements, and it can take in a function to perform.
Overall, I think my design separates functionality quite clearly and is easy to understand.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
I used images from the following links to help build my game:
https://appsftw.com/app/minesweeper-i-my-mine
https://commons.wikimedia.org/wiki/File:Minesweeper_unopened_square.svg
https://www.pngwing.com/en/search?q=minesweeper
Also, I stumbled across the idea of using probability to solve minesweeper through:
https://stackoverflow.com/questions/1738128/minesweeper-solving-algorithm
However, I implemented all three algorithms on my own.
