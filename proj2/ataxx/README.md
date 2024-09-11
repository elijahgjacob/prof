Ataxx Game
This project is the main program for the Ataxx game, implemented in Java. It provides both a graphical user interface (GUI) and a text-based interface for playing the game. Ataxx is a strategy board game where players attempt to take over the board by capturing their opponent's pieces.

Features
Graphical User Interface (GUI): A visual interface for an enhanced gameplay experience.
Text-Based Interface: A command-line interface for playing the game without a GUI.
Multiple Options:
--display: Enables the GUI for gameplay.
--timing: Displays the thinking time for AI players.
--version: Prints the version number of the game and exits.
--log: Logs game commands.
--strict: Enables strict mode, where player errors result in the program exiting with an error.
--debug: Sets the level of debugging information.
Requirements
Java Development Kit (JDK) version 8 or higher.
A terminal or command prompt to run the game.
How to Run
Clone the Repository:

sh
Copy code
git clone https://github.com/your-username/ataxx.git
cd ataxx
Compile the Program:

sh
Copy code
javac ataxx/Main.java
Run the Game: You can run the game using different options:

With GUI:
sh
Copy code
java ataxx.Main --display
Without GUI (Text Mode):
sh
Copy code
java ataxx.Main
Optional Parameters: You can add additional parameters based on your preferences:

sh
Copy code
java ataxx.Main --log --strict --debug=2
Usage
Command Line Options
--display: Launches the game with a GUI.
--timing: Displays the AI players' thinking time during the game.
--version: Prints the current version of the game and exits.
--log: Logs all commands and moves during gameplay.
--strict: Enables strict mode where any player error results in an immediate program exit.
--debug=<level>: Sets the level of debug information (e.g., --debug=2).
Input Files
You can specify input files for the game using trailing arguments:

sh
Copy code
java ataxx.Main file1.txt file2.txt
If no input file is provided, the program reads from the standard input.

Developer Information
Author: Paul N. Hilfinger
University: Regents of the University of California
Contributing
Please do not distribute or create derivative works without permission.

License
This code is proprietary. Do not distribute this or any derivative work without explicit permission from the author.

Contact
For questions or suggestions, contact the developer team.

