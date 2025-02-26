
---
geometry: margin=1in
---
# PROJECT Design Documentation

## Team Information
* Team name: Please Stand By
* Team members
  * Morgan Kreifels
  * Max Shenk
  * Aiden Green
  * Zach Montgomery
  * Steven Feldman

## Executive Summary

WebCheckers is a browser game meant to simulate checkers online.
Users sign in with their name and choose an opponent to play against from a list of players that are online and not in a game.
Any two of the players can win the game by normal means or their opponent resigning.
After the game ends and a player wins, both players will be returned to the home page where they can play another game or sign out.

### Purpose

> This project will allow online players to play games with other
> online opponents. Our project focuses on the players that will be playing games.
> The most important user goals are for the user 
> to log in to start a game, start a game with any player, and 
> then play that game until a player wins.

### Glossary and Acronyms

| Term | Definition |
|------|------------|
| VO | Value Object |
| MVP |Minimal Viable Product|
|Enhancments|Additional terms to the MVP|


## Requirements

### Definition of MVP
>The Minimal Viable Product is the software that will fill the basic 
> objectives that will delight the user. This product can fully 
> function on its own with no further enhancments. The MVP for this
> product is for the user to be able to sign in, start a game with online
> players, play a game, and win or lose games.
>  
### MVP Features
> Every player will be able to sign-in before playing a game and
> then sign-out whenever the player is finished playing games.

> Two players that are online will be able to play a game of checkers
> with each other based off the American Rules. These players choose
> one another from a list of online players on the home page

>These two players will then see a checkers board that is updated with
> every move. The game will continue until a player resigns, when the
> opposing player has no more pieces, or the opposing player has
> no more valid moves.

>Either player will be able to resign at any point, which will make the
> opposing player the winner of that game.

>After a player wins, both players will then be directed back to
> the list of online players.

### Roadmap of Enhancements
> The first feature that we will implement is the ability for the players
> to play in a checkers tournament. Player's statistics will be tracked
> so that players will start games with players that have around the same
> statistics. The player that starts the tournament can choose players
> to join the game and other players can join the game as well.

>The second feature will implement an AI player that any online player
> can play against. This game can be compared to playing with a 
> human player. 

 
## Application Domain

![The WebCheckers Domain Model](DomainModel.png)

> Checkers game will be the manager of the application of checkers and 
> then translated into the online server. 
 
> BoardView will control the way the players will see the board and how 
> they will interact with the board. This board will contain 64 spaces.
> This spaces will be occupied with pieces that the player will place down.
 
> The player will control up to 12 checker pieces per game. These pieces will 
> be able to change in to king pieces in the game. 

>The AI player is based off of a player and this AI can be played with
> players.

>The checkers game can than be played in a tournament mode, where 
> players will be able to play in a tournament based game play.


## Architecture and Design

The Program is a web application with UI, Application, and Model tier methods that handle a straightforward game of
online checkers. Our Model tier handles everything to do with the checkers game's logic, such as the position of the
pieces and each move. It also stores all the data of the game, every nook and cranny. The UI tier handles everything to
do with user interface (obviously), menus, the webapp, connections with other players, and essentially everything not a
part of the core checkers game. The Application tier is like a controller between the Model and the UI, translating the
inputs of the player into effects on the program, and returning the responses the system produces back to the player.


### Summary

The following Tiers/Layers model shows a high-level view of the webapp's architecture.

![The Tiers & Layers of the Architecture](architecture-tiers-and-layers.png)

As a web application, the user interacts with the system using a
browser.  The client-side of the UI is composed of HTML pages with
some minimal CSS for styling the page.  There is also some JavaScript
that has been provided to the team by the architect.

The server-side tiers include the UI Tier that is composed of UI Controllers and Views.
Controllers are built using the Spark framework and View are built using the FreeMarker framework.
The Application and Model tiers are built using plain-old Java objects (POJOs).

Details of the components within these tiers are supplied below.


### Overview of User Interface

This section describes the web interface flow; this is how the user views and interacts
with the WebCheckers application.

![The WebCheckers Web Interface Statechart](StateDiagram.jpg)

> First, all users will start at the HomePage this page will provide
> a link to sign in. If the player is not signed in the player will be brought 
> to a sign-in page. If the player does not type in a valid sign-in 
> name then the player will stay on the sign-in page until a valid name is
> inputted. After a valid name is entered the user will be taken to the 
> home page.

> Once the player is signed in, they will be brought to the home page,
> with the list of online players. From here a player can click on 
> players to play a game with if that player is not in a game. Then both 
> players will be brought to a view of the game to start playing. If
> the player is not available the user will stay on the home page.

> When a game is over or a player resigns, both players will be brought
> back to the home page with the list of players. If the player signs out
> in the middle of the game, then the player will be returned to the 
> home page without the list of players. The player still signed in then
> they will be returned to the home page with players.


### UI Tier

As the user first connects to the WebCheckers webpage the server will display the homepage
by accessing the GetHomeRoute, this will display the homepage that only
displays the number of players online and the available link to log in.

When the user clicks on the sign-on link, this will make the server connect to 
the GetSignInRoute. This route will display the SignInPage to the user. This will 
show a text box that the user can type in a username. Once the user types in a name and 
presses the sign-in button, the server will then connect to the PostSignInRoute.
Here is where the name will be validated through the player lobby, and the
player lobby will return true if the user typed in a valid user name, if the 
player lobby returned false then the server will return the sign-in page 
with an error message.Once the user has a correct, the server will redirect
to the homepage.

The homepage displayed to the signed in user will contain the list of players
that are signed. It will show if the player is avaliable to play a game or is 
currently in a game. From this homepage the players can start games with eachother
or sign out.

When a user clicks on a user to start a game, the server will access the
GetGameViewRoute. If the player is available to play a game, then both players 
will be displayed the checkers game. If the player selected is alreading in a game,
then the one who selected will be returned back to the home page with an error.
As the game is able to be played mulitple routes can be selected from the server.

After each move, the server will PostValidateRoute which will validate the move
to check to see if the move made by the player follows the American Checkers rules.

When the player is in the process of deciding which move to sumbit, the player can redo 
their moves to return to the last sumbited move. This move can retrieved when the server access 
PostBackuproute.

Once the player has decided which move to sumbit, they will click the sumbit button and the server
will save the move for further usages by going to PostSumbitTurnRoute. The server will also keep
tracking who's turn is next, by accessing PostCheckTurnRoute.

The POST /checkTurn route is automatically requested every 5 seconds by the browser. When it is invoked, the program simply retrieves the player, accesses the game that player is in, and uses a helping Model function to return the Color of the players whose turn it is. The handling method then compares the given color to the color of the player who called the route. If the color is the same(its their turn), then the "true" string is put into an INFO Message and sent back via response. In a case where it is not their turn(Colors do not match), the Message returned is instead an ERROR, and the "false" string is attached.

When the players are done playing, then the users will be returned to the homepage by the serving accessing GetHomeRoute
If a another player wants to play with one of the players, then that player will be 
returned to another game board. 

The game can also be done when a player resgins. The server will access
the PostResignRoute will process the views that each player will recieve.

If the POST /signout route is accessed, the session is retrieved from the associated HTTP request, and then the Application level playerLobby is called to handle the true "signing out" of the player(player object being removed from collection, freeing username, etc).







### Application Tier
> Same instruction as UI

When the sign-out method is called, given the players session, it will remove the mapping between the users Session and the Player object (session.attribute()), and will also remove the object from the playerLobby's List collection. The program now holds no player data based on that players session.

### Model Tier

A Move object is a tool to hold start and end Position objects, and is needed for processing moves from the player. It also contains a getFlipped() method which is called from the UI when the player who attempted the move was WHITE, since they have a reversed board, and the request returns coordinates relative to their board. 

When a simple move is attempted by the user, the given Move object is used within the player.move(Move move) method. The method performs a number of checks to see if the move is valid. In the case of a simple move, the method checks if the start and end positions are no further than 1 space away in both directions, and if the move was attempted backwards(relative to them). In case of a valid move, an INFO Message class is returned with a "valid move" String attached. Otherwise an ERROR message class is returned with the respective error attached. 

When a jump move is performed, it is also handled in the move method and it runs checks to see if the jump is valid. The jump is valid if the player moves 2 spaces in both directions with an opponents piece in the space between. ERROR/INFO returns are same as the simple move.

Multi-jumps are handled just as the previous types of moves. The move method will check to see if the move they had done before the current move was a jump or not. If it was a jump, then a new jump will be valid, but ONLY a new jump. 

In the case of a backup called by the UI, the backupMove() method will be called, and the last move will be undone. The player class contains a list of INVERTED moves that the player has done in their current turn, in order. This list is scrapped once they submit their turn, and a new list is created on their next turn. When a backup is called, the latest move is removed from the list, and then performed on the board, and since the move is an inverted version, it will have simply undone their move. It can be done as long as there are moves in the inverted list.

The CheckersGame class has a getActiveColor() method to help determine the color whose turn it is. This is used for the UI /checkTurn route to see if it matches their color.

### Design Improvements

One improvement would to further our polymorphism implementation. This could help our code
be more precise and efficient. In an area that polymorphism could benefit would be the GET and POST
route classes. Many functions of these classes are similar and they all could share the same
parent class and inherit similar classes. 
## Testing
> _This section will provide information about the testing performed
> and the results of the testing._

### Acceptance Testing

There have been a total of 11 user stories which created 42 acceptance criterion to be tested.
All acceptance criteria tests showed that the code is fully implemented and correctly
runs a checkers game based on the American rules.
### Unit Testing and Code Coverage

The way we decided to cover our code in tests, was the importance of that class towards
the implementation of the program. In the model tier, the BoardView add the most classes
because it is vital that the players can correctly see and interact with the board. Testing
all the classes was not our first priority, because some classes were small enough
to easily fix if there were any bugs.
![Model Test Coverage](ModelTest.png)

In the application tier, the Game Center had most of the priority because that is where 
the game is initialized and started. Player Lobby had fundamental functions to the implementation of 
the game, but that class had simpler functions to following along with
![Application Test Coverage](ApplicationTest.png)

For the UI tier it was difficult to create tests that would be able to simulate the 
server asking for the different types of routes. We could have tested for the vm after
each route to make sure the correct values and keys are being passed to each rendering page.
For future testing we will create more concise tests for all the layers, especially
the UI tier since this is the layer that will have the most incorrect user input.
![UI Test Coverage](UITest.png)