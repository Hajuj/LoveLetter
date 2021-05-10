# Welcome!
This is our University project for the card game Love Letter, it is a game of risk, deduction, and luck.

## Game Rules
The game can be played with 2-4 players, your goal is to get your love letter into Princess Annette's hands while deflecting the letters from competing suitors.

From a deck with only sixteen cards, each player starts with only one card in hand; one card is removed from play. On a turn, you draw one card, and play one card, trying to expose others and knock them from the game. Powerful cards lead to early gains, but make you a target. Rely on weaker cards for too long, however, and your letter may be tossed in the fire!

To get more familiar with the game rules, just visit the official website here: http://alderac.com/wp-content/uploads/2017/11/Love-Letter-Premium_Rulebook.pdf

### Start the game
First, please clone or download the project to your local machine. Then, in order to start the game, just run first the server (server.server class) and then run the Client Application (chat.ClientApplication class).
* Note that you have to run ClientApplication class more than once in order to connect more clients to the server.

Once you are connected you can start chatting with other clients in the server, or you can use the Bot commands to start a game and play with others. But you need to start the BotClient first.

To start LoveLetter in Chat:
1. You can press the button play, to activate the Bot Client. He welcomes all in the chat.
2. You should send "@bot play", the Bot Client will set you in the waiting list.
3. After two Players in the waiting list, the Bot Client will inform, how they can start the game. If there are four players on the waiting list, the bot client starts the game automatically.

* You can always call the Bot commands by writing @Bot Help.
* You can leave the chat with the command "bye" at any time.

The BotClient follows the rules precisely -- so there is no room for cheating. (Often, if you don't have a choice what card to play, the bot will just play it for you.)

#### Requirements 
In order to run the project without problems, you'll need to install the following first:
* Java 16
* JavaFX 16

