# Snake and Ladder System UML

```mermaid
classDiagram

%% ====================
%% OBSERVER PATTERN
%% ====================

class IObserver {
    <<interface>>
    +update(msg : String)
}

class SnakeAndLadderConsoleNotifier {
    +update(msg : String)
}

IObserver <|.. SnakeAndLadderConsoleNotifier


%% ====================
%% DICE
%% ====================

class Dice {
    -faces : int

    +Dice(f : int)
    +roll() : int
}


%% ====================
%% BOARD ENTITIES
%% ====================

class BoardEntity {
    <<abstract>>
    #startPosition : int
    #endPosition : int

    +BoardEntity(start : int, end : int)
    +getStart() : int
    +getEnd() : int
    +display()
    +name() String
}

class Snake {
    +Snake(start : int, end : int)
    +display()
    +name() String
}

class Ladder {
    +Ladder(start : int, end : int)
    +display()
    +name() String
}

BoardEntity <|-- Snake
BoardEntity <|-- Ladder


%% ====================
%% BOARD
%% ====================

class Board {
    -size : int
    -snakesAndLadders : List~BoardEntity~
    -boardEntities : Map~Integer, BoardEntity~

    +Board(s : int)
    +canAddEntity(position : int) boolean
    +addBoardEntity(boardEntity : BoardEntity)
    +setupBoard(strategy : BoardSetupStrategy)
    +getEntity(position : int) BoardEntity
    +getBoardSize() int
    +display()
}

Board *-- BoardEntity
Board --> BoardSetupStrategy


%% ====================
%% BOARD SETUP STRATEGY
%% ====================

class BoardSetupStrategy {
    <<interface>>
    +setupBoard(board : Board)
}

class RandomBoardSetupStrategy {
    -difficulty : Difficulty

    +RandomBoardSetupStrategy(d : Difficulty)
    +setupBoard(board : Board)
}

class CustomCountBoardSetupStrategy {
    -numSnakes : int
    -numLadders : int
    -randomPositions : boolean
    -snakePositions : List
    -ladderPositions : List

    +CustomCountBoardSetupStrategy(snakes, ladders, random)
    +addSnakePosition(start, end)
    +addLadderPosition(start, end)
    +setupBoard(board : Board)
}

class StandardBoardSetupStrategy {
    +setupBoard(board : Board)
}

class Difficulty {
    <<enumeration>>
    EASY
    MEDIUM
    HARD
}

BoardSetupStrategy <|.. RandomBoardSetupStrategy
BoardSetupStrategy <|.. CustomCountBoardSetupStrategy
BoardSetupStrategy <|.. StandardBoardSetupStrategy

RandomBoardSetupStrategy --> Difficulty


%% ====================
%% PLAYER
%% ====================

class SnakeAndLadderPlayer {
    -playerId : int
    -name : String
    -position : int
    -score : int

    +SnakeAndLadderPlayer(playerId : int, name : String)
    +getName() String
    +getPosition() int
    +setPosition(pos : int)
    +getScore() int
    +incrementScore()
}


%% ====================
%% GAME RULES STRATEGY
%% ====================

class SnakeAndLadderRules {
    <<interface>>
    +isValidMove(currentPos, diceValue, boardSize) boolean
    +calculateNewPosition(currentPos, diceValue, board) int
    +checkWinCondition(position, boardSize) boolean
}

class StandardSnakeAndLadderRules {
    +isValidMove(currentPos, diceValue, boardSize) boolean
    +calculateNewPosition(currentPos, diceValue, board) int
    +checkWinCondition(position, boardSize) boolean
}

SnakeAndLadderRules <|.. StandardSnakeAndLadderRules

StandardSnakeAndLadderRules --> Board


%% ====================
%% GAME
%% ====================

class SnakeAndLadderGame {
    -board : Board
    -dice : Dice
    -players : Deque~SnakeAndLadderPlayer~
    -rules : SnakeAndLadderRules
    -observers : List~IObserver~
    -gameOver : boolean

    +SnakeAndLadderGame(board : Board, dice : Dice)
    +addPlayer(player : SnakeAndLadderPlayer)
    +addObserver(observer : IObserver)
    +notify(msg : String)
    +displayPlayerPositions()
    +play()
}

SnakeAndLadderGame --> Board
SnakeAndLadderGame --> Dice
SnakeAndLadderGame o-- SnakeAndLadderPlayer
SnakeAndLadderGame --> SnakeAndLadderRules
SnakeAndLadderGame o-- IObserver


%% ====================
%% FACTORY PATTERN
%% ====================

class SnakeAndLadderGameFactory {
    +createStandardGame() SnakeAndLadderGame
    +createRandomGame(boardSize, difficulty) SnakeAndLadderGame
    +createCustomGame(boardSize, strategy) SnakeAndLadderGame
}

SnakeAndLadderGameFactory ..> SnakeAndLadderGame
SnakeAndLadderGameFactory ..> Board
SnakeAndLadderGameFactory ..> Dice
SnakeAndLadderGameFactory ..> BoardSetupStrategy


%% ====================
%% MAIN
%% ====================

class SnakeAndLadder {
    +main(args : String[])
}

SnakeAndLadder ..> SnakeAndLadderGameFactory
SnakeAndLadder ..> SnakeAndLadderPlayer
SnakeAndLadder ..> SnakeAndLadderConsoleNotifier
SnakeAndLadder ..> IObserver
