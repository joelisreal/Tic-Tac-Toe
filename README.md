# Personal Project: Tic-tac-toe

## Project Proposal

This is a game of tic-tac-toe. Users will be able to customize the size of the game, whether a 3 by 3, 5 by 5 and so on.
Anyone who has an interest in playing games will be able to use it. This project is  of interest to me because making 
this game will allow me to think **creatively** and have fun while doing so. Tic-tac-toe is a game that I enjoyed a lot
growing up me and perhaps while making it I'll develop it in ways I didn't otherwise think of, by making adjustments and
additions to the original gameplay.

## User Stories

* As a user, I want to be able to instantiate Xs and Os and add them onto the board
* As a user, I want to be able to win by getting n of Xs and Os in a row where n is the length/width of the board
* As a user, I want to be able to choose to start with either Xs or Os
* As a user, I want to be able to choose what location to add my Xs or Os on to
* As a user, I want to be able to save my tic-tac-toe game to file
* As a user, I want to be able to load my tic-tac-toe game from file


*Note*: Code for persistence was largely borrowed from 
https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

### Phase 4: Task 2
Fri Nov 26 10:57:06 PST 2021
Added an X at row 0 and column 1


Fri Nov 26 10:57:07 PST 2021
Added an O at row 0 and column 2


Fri Nov 26 10:57:08 PST 2021
Added an X at row 1 and column 2


Fri Nov 26 10:57:10 PST 2021
Added an O at row 0 and column 0

### Phase 4: Task 3
Since 'MainMenuFrame' and 'TicTacToeFrame' both call on 'Board', it would make sense to perhaps associate 'Board' with
just one of them, then pass it in the constructor when calling one or the other since both are mutually dependent on
each other. 
