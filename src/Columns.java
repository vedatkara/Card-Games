import enigma.console.TextAttributes;
import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Columns {

    public static enigma.console.Console cn = Enigma.getConsole("Columns", 80, 35, 20, 2);

    // Klis
    private TextMouseListener tmlis;
    private KeyListener klis;

    // Variables for game play
    private int mousepr, mouserl, keypr, rkey, mousex, mousey;
    private int keyX = 4, keyY = 3;
    private int columnsX =1, columnsY = 0;
    private int prevColumnsX = -1, prevColumnsY = -1;
    private int finishedSets;
    public static int transfer = 0;
    private static Boolean firstPress = false, secondPress = false, isZPressed = false;

    // Box and High Score
    private static SingleLinkedList box;
    private static DoubleLinkedList highScoreList;
    public static Player player;

    // Columns
    private static MultiLinkedList columns;

    Columns() throws InterruptedException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        consoleClear();// A function to clear console
        // Key listener for mouse and keyboard
        listener();

        // Main game loop
        while (true) {
            consoleClear();
            printMenu();

            // If key pressed
            if (keypr == 1) {

                if (rkey == KeyEvent.VK_1 || rkey == KeyEvent.VK_NUMPAD1) {

                    rkey = keypr = 0;
                    consoleClear();
                    System.out.println("\n\n\n\n");
                    System.out.println("                        Please select a game mode");
                    System.out.println("\n\n");
                    System.out.println("                               1-Classic");
                    System.out.println("                               2-Pisti");

                    // A loop to be sure that user selected a game mode
                    do {
                        if (keypr == 1) {
                            keypr = 0;
                        }
                        Thread.sleep(50);

                    } while (rkey != KeyEvent.VK_1 && rkey != KeyEvent.VK_NUMPAD1 //If user tries to press a key
                            && rkey != KeyEvent.VK_2 && rkey != KeyEvent.VK_NUMPAD2);//different from 1 or 2 do not continue.

                    // If player choose to play classic mode this part starts to work
                    if (rkey == KeyEvent.VK_1 || rkey == KeyEvent.VK_NUMPAD1) {
                        transfer = keypr = 0;
                        generateBox();
                        createInitialColumns();
                        consoleClear();

                        // Player operations
                        Scanner scan = new Scanner(System.in);
                        String name;
                        do {
                            consoleClear();
                            System.out.print("Please enter your name and surname: ");
                            name = scan.nextLine();

                        } while (name.trim().split(" ").length != 2);

                        player = new Player(name, 0);
                        scan.close();

                        printGameArea();
                        TextAttributes coloredNumber = new TextAttributes(Color.GRAY, Color.CYAN);
                        cn.getTextWindow().setCursorPosition(keyX - 1, keyY);
                        cn.getTextWindow().output(">", coloredNumber);
                        cn.getTextWindow().setCursorPosition(keyX + 2, keyY);
                        cn.getTextWindow().output("<", coloredNumber);

                        keypr = rkey = 0;

                        // Main game loop
                        while (true) {
                            keyListeners();//for players prefer keyboard
                            mouseListeners();//for players prefer mouse
                            Thread.sleep(50);

                            if (rkey == KeyEvent.VK_ESCAPE || player.getScore() == 5000 || rkey == KeyEvent.VK_E) {
                                generateHighScoreTable();
                                cn.getTextWindow().setCursorPosition(22, 25);
                                System.out.print("Please press ESC to return menu");
                                keypr = rkey = 0;

                                while (rkey != KeyEvent.VK_ESCAPE) {
                                    Thread.sleep(200);
                                }
                                rkey = 0;
                                break;
                            }
                        }
                    }
                    else // If player choose to play additional mode called Pisti
                        new Pisti();


                } else if (rkey == KeyEvent.VK_2 || rkey == KeyEvent.VK_NUMPAD2) {
                    consoleClear();
                    keypr = rkey = 0;
                    cn.getTextWindow().setCursorPosition(26,15);
                    System.out.println("1-How to Play Classic");
                    cn.getTextWindow().setCursorPosition(26,16);
                    System.out.println("2-How to Play Pisti");

                    do {
                        keypr = rkey = 0;
                        Thread.sleep(50);

                    } while (rkey != KeyEvent.VK_1 && rkey != KeyEvent.VK_NUMPAD1
                            && rkey != KeyEvent.VK_2 && rkey != KeyEvent.VK_NUMPAD2);

                    consoleClear();
                    // If player choose to play classic mode this part starts to work
                    if (rkey == KeyEvent.VK_1 || rkey == KeyEvent.VK_NUMPAD1)
                        howToPlayClassic();
                    else
                        howToPlayPisti();

                    keypr = rkey = 0;
                    while (true) {
                        if (keypr == 1) {
                            if (rkey == KeyEvent.VK_ESCAPE) {
                                break;
                            }
                            keypr = 0;
                        }
                        Thread.sleep(50);
                    }
                } else if (rkey == KeyEvent.VK_3 || rkey == KeyEvent.VK_NUMPAD3) {
                    consoleClear();
                    exitMessage();
                    Thread.sleep(3000);
                    System.exit(0);
                }
            }
            keypr = 0;
            Thread.sleep(50);
        }
    }

    private void createInitialColumns() { // adds 5 random numbers to each column.
        columns = new MultiLinkedList();
        columns.addColumn(1);
        columns.addColumn(2);
        columns.addColumn(3);
        columns.addColumn(4);
        columns.addColumn(5);

        for (int i = 0; i < 6; i++) {
            columns.addNumber(1, (Integer) box.pop());
            columns.addNumber(2, (Integer) box.pop());
            columns.addNumber(3, (Integer) box.pop());
            columns.addNumber(4, (Integer) box.pop());
            columns.addNumber(5, (Integer) box.pop());
        }
    }

    private void listener() {
        tmlis = new TextMouseListener() {
            public void mouseClicked(TextMouseEvent arg0) {
            }
            public void mousePressed(TextMouseEvent arg0) {
                if (mousepr == 0) {
                    mousepr = 1;
                    mousex = arg0.getX();
                    mousey = arg0.getY();
                }
            }
            public void mouseReleased(TextMouseEvent arg0) {
                if (mouserl == 0) {
                    mouserl = 1;
                    mousex = arg0.getX();
                    mousey = arg0.getY();
                }
            }
        };
        cn.getTextWindow().addTextMouseListener(tmlis);

        klis = new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }
            public void keyPressed(KeyEvent e) {
                if (keypr == 0) {
                    keypr = 1;
                    rkey = e.getKeyCode();
                }
            }
            public void keyReleased(KeyEvent e) {
            }
        };
        cn.getTextWindow().addKeyListener(klis);
    }

    public static void printMenu() {

        cn.getTextWindow().setCursorPosition(25, 11);
        for (int i = 0; i < 4; i++) {
            cn.getTextWindow().output("♥ ", new TextAttributes(Color.RED));
            cn.getTextWindow().output("♦ ", new TextAttributes(Color.BLUE));
            cn.getTextWindow().output("♣ ");
            cn.getTextWindow().output("♠ ", new TextAttributes(Color.GREEN));
        }

        cn.getTextWindow().setCursorPosition(38, 14);
        System.out.println(" MENU ");
        cn.getTextWindow().setCursorPosition(34, 15);
        System.out.println("1. Start");
        cn.getTextWindow().setCursorPosition(34, 16);
        System.out.println("2. How to Play?");
        cn.getTextWindow().setCursorPosition(34, 17);
        System.out.println("3. Exit");

        cn.getTextWindow().setCursorPosition(25, 20);
        for (int i = 0; i < 4; i++) {
            cn.getTextWindow().output("♥ ", new TextAttributes(Color.RED));
            cn.getTextWindow().output("♦ ", new TextAttributes(Color.BLUE));
            cn.getTextWindow().output("♣ ");
            cn.getTextWindow().output("♠ ", new TextAttributes(Color.GREEN));
        }
    }

    public static void exitMessage() {
        cn.getTextWindow().setCursorPosition(22, 12);
        System.out.println("    _/_/_/    _/      _/  _/_/_/_/  ");
        cn.getTextWindow().setCursorPosition(22, 13);
        System.out.println("   _/    _/    _/  _/    _/         ");
        cn.getTextWindow().setCursorPosition(22, 14);
        System.out.println("  _/_/_/        _/      _/_/_/      ");
        cn.getTextWindow().setCursorPosition(22, 15);
        System.out.println(" _/    _/      _/      _/           ");
        cn.getTextWindow().setCursorPosition(22, 16);
        System.out.println("_/_/_/        _/      _/_/_/_/      ");
    }

    // Clear console
    public static void consoleClear() {
        cn.getTextWindow().setCursorPosition(0, 0);
        for (int i = 0; i < cn.getTextWindow().getRows(); i++) {
            for (int j = 0; j < cn.getTextWindow().getColumns() - 1; j++)
                System.out.print(" ");
            if (i != cn.getTextWindow().getRows() - 1)
                System.out.println();
        }
        cn.getTextWindow().setCursorPosition(0, 0);
    }

    public static void printGameArea() {
        consoleClear();

        // This function creates playing area by using for loops
        for (int i = 1; i < 6; i++) {
            cn.getTextWindow().setCursorPosition(8 * i - 5, 0);
            System.out.print("+--+");
            cn.getTextWindow().setCursorPosition(8 * i - 5, 1);
            System.out.print("|C" + i + "|");
            cn.getTextWindow().setCursorPosition(8 * i - 5, 2);
            System.out.print("+--+");
        }

        cn.getTextWindow().setCursorPosition(46, 1);
        System.out.print("Transfer: " + transfer + "   ");

        cn.getTextWindow().setCursorPosition(46, 2);
        System.out.print("Score   : " + player.getScore());

        cn.getTextWindow().setCursorPosition(48, 8);
        System.out.print(" BOX ");

        cn.getTextWindow().setCursorPosition(48, 9);
        System.out.print("+---+");

        cn.getTextWindow().setCursorPosition(48, 10);
        if (!box.isEmpty() && (firstPress || secondPress) && (int) box.peek() != 10)
            System.out.print("| " + box.peek() + " |");
        else if (!box.isEmpty() && (int) box.peek() == 10 && (firstPress || secondPress))
            System.out.print("|" + box.peek() + " |");
        else
            System.out.print("| " + " " + " |");

        cn.getTextWindow().setCursorPosition(48, 11);
        System.out.print("+---+");

        // Printing initial columns on the screen
        columns.printColumns(cn);

        cn.getTextWindow().setCursorPosition(0, 0);
    }

    private int createRandomNumber(int minValue, int maxValue) {
        Random random = new Random();
        return random.nextInt((maxValue - minValue) + 1) + minValue;
    }

    private void generateBox() { // Fills the box with 50 random numbers from 1-10
        box = new SingleLinkedList();
        while (box.size() < 50) {
            int randomNumber = createRandomNumber(1, 10); // Generates random numbers between 1 and 10 [1,10]
            if (!box.isNumberCountFull(randomNumber)) { // if the same number is added to the box 5 times, it will not be added again
                box.add(randomNumber);
            }
        }
    }

    private void generateHighScoreTable() { // Creates a highscore table and writes it ti the highscore.txt
        consoleClear();

        finishedSets = (int) player.getScore() / 1000;

        if (transfer == 0)
            player.setScore(0);
        else
            player.setScore(100 * finishedSets + (player.getScore() / transfer));

        try {
            FileReader file = new FileReader("highscore.txt"); // Read the highscore.txt
            Scanner sc = new Scanner(file); // Scanner for the highscore.txt
            highScoreList = new DoubleLinkedList();
            highScoreList.add(player); // Add the player to the highscore


            while (sc.hasNext()) { // Read the highscore.txt
                highScoreList.add(new Player(sc.next() + " " + sc.next(), Double.parseDouble(sc.next()))); // Add the players to the highscore
            }
            sc.close(); // Close the scanner

            cn.getTextWindow().setCursorPosition(28,1);
            System.out.print("HIGH SCORE TABLE");
            highScoreList.display(cn); // Display the highscore
            PrintWriter pw = new PrintWriter("highscore.txt"); // open a print writer for the highscore

            for (int i = 1; i < highScoreList.size() + 1; i++) {
                Player tempPlayer = (Player) highScoreList.getElement(i);
                pw.print(tempPlayer.getName() + " " + String.format ("%.2f",(tempPlayer.getScore())) + "\n"); // print the highscore line by line
            }

            pw.close(); // close the print writer
        } catch (Exception e) { // catch exception
            System.out.println("There is not file such as highscore.txt");
        }
    }

    private void keyListeners() {
        if (rkey == KeyEvent.VK_B) { // If the B key is pressed, the element at the top of the box appears on the screen

            if (firstPress) {
                secondPress = true;
                firstPress = false;
            }
            if (!secondPress)
                firstPress = true;


            printGameArea();
            rkey = 0;
            keypr = 0;
        }
        if (rkey == KeyEvent.VK_UP) {
            if (keyY > 3) {
                keyY = keyY - 1;
                columnsY = columnsY - 1;
                TextAttributes coloredNumber = new TextAttributes(Color.GRAY, Color.CYAN);
                printGameArea();
                cn.getTextWindow().setCursorPosition(keyX - 1, keyY);
                cn.getTextWindow().output(">", coloredNumber);
                cn.getTextWindow().setCursorPosition(keyX + 2, keyY);
                cn.getTextWindow().output("<", coloredNumber);
            }
            rkey = 0;
            keypr = 0;
        }
        if (rkey == KeyEvent.VK_DOWN) {
            if (keyY > 2 && keyY <= columns.sizeOfColumn(columnsX) + 1) {
                keyY = keyY + 1;
                columnsY = columnsY + 1;
                TextAttributes coloredNumber = new TextAttributes(Color.GRAY, Color.CYAN);
                printGameArea();
                cn.getTextWindow().setCursorPosition(keyX - 1, keyY);
                cn.getTextWindow().output(">", coloredNumber);
                cn.getTextWindow().setCursorPosition(keyX + 2, keyY);
                cn.getTextWindow().output("<", coloredNumber);
            }
            rkey = 0;
            keypr = 0;
        }
        if (rkey == KeyEvent.VK_RIGHT) {
            if (keyX < 35 && columnsX < 5) {
                keyX = keyX + 8;
                columnsX++;
                TextAttributes coloredNumber = new TextAttributes(Color.GRAY, Color.CYAN);
                printGameArea();
                cn.getTextWindow().setCursorPosition(keyX - 1, keyY);
                cn.getTextWindow().output(">", coloredNumber);
                cn.getTextWindow().setCursorPosition(keyX + 2, keyY);
                cn.getTextWindow().output("<", coloredNumber);
            }
            rkey = 0;
            keypr = 0;
        }
        if (rkey == KeyEvent.VK_LEFT) {
            if (keyX > 3 && columnsX > 1) {
                keyX = keyX - 8;
                columnsX--;
                TextAttributes coloredNumber = new TextAttributes(Color.GRAY, Color.CYAN);
                printGameArea();
                cn.getTextWindow().setCursorPosition(keyX - 1, keyY);
                cn.getTextWindow().output(">", coloredNumber);
                cn.getTextWindow().setCursorPosition(keyX + 2, keyY);
                cn.getTextWindow().output("<", coloredNumber);
            }
            rkey = 0;
            keypr = 0;
        }
        if (rkey == KeyEvent.VK_Z) {
            columns.printeColoredColumn(cn, columnsX, columnsY);
            prevColumnsX = columnsX;
            prevColumnsY = columnsY;
            isZPressed = true;
            secondPress = false;
            rkey = 0;
            keypr = 0;
        }
        if (rkey == KeyEvent.VK_X) {
            if (prevColumnsX != -1 && prevColumnsY != -1 && isZPressed) {
                columns.transfer(prevColumnsX, columnsX, prevColumnsY);
                columns.checkMatching(prevColumnsX, columnsX);
                isZPressed = false;

            }
            if (secondPress && !box.isEmpty() && (Math.abs((int) box.peek() - columns.getLastNumber(columnsX)) <= 1 ||
                    (columns.getLastNumber(columnsX) == -1 && ((int)box.peek() == 1 || (int)box.peek() == 10))))
            {
                columns.addNumber(columnsX, (Integer) box.pop());
                secondPress = false;
                firstPress = false;
                columns.checkMatching(columnsX);
                transfer++;
                printGameArea();
            }
            rkey = 0;
            keypr = 0;
        } else {
            rkey = 0;
            keypr = 0;
        }
    }

    private void mouseListeners() throws InterruptedException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (mousepr == 1) {
            clickSound();//make sound everytime user clicks.

            keypr = mousepr = 0;
            boolean isNumberSelected = false, isNumberOfBoxWanted = false;
            int firstX = mousex, firstY = mousey;

            if (firstY - 3 >= 0) {//To be sure that the selected point on the column.
                if ((firstX + 4) % 8 == 0 && (firstX + 4) / 8 <= 5 && columns.sizeOfColumn((firstX + 4) / 8) > firstY - 3) {
                    isNumberSelected = true;
                    columns.printeColoredColumn(cn, (firstX + 4) / 8, firstY - 3);
                }
                if (!box.isEmpty() && (firstX + 4) % 8 == 0 && (firstX + 4) / 8 <= 5 && firstY == columns.sizeOfColumn(
                        (firstX + 4) / 8) + 3 && (Math.abs((int) box.peek() - columns.getLastNumber((firstX + 4) / 8)) <= 1 ||
                        (columns.getLastNumber((firstX + 4) / 8) == -1) && ((int)box.peek() == 1 || (int)box.peek() == 10))) {
                    isNumberOfBoxWanted = true;
                }
            }

            while (mouserl == 0)
                Thread.sleep(50);

            if (isNumberSelected) {//If number selected,
                if ((mousex + 4) % 8 == 0) {//and the cursor on a column.
                    columns.transfer((firstX + 4) / 8, (mousex + 4) / 8, firstY - 3);
                    columns.checkMatching((firstX + 4) / 8, (mousex + 4) / 8);
                }
                else
                    printGameArea();
            }

            if (isNumberOfBoxWanted) {//if number in the box selected enter here.
                if (!box.isEmpty()) {
                    columns.addNumber((firstX + 4) / 8, (Integer) box.pop());
                    columns.checkMatching((firstX + 4) / 8);
                    firstPress = false;
                    secondPress = false;
                    transfer++;
                    printGameArea();
                }
            }
            mouserl = 0;
        }
    }

    public static void clickSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File("click.wav");
        javax.sound.sampled.AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }

    public static void howToPlayClassic() {
        cn.getTextWindow().setCursorPosition(28, 0);
        System.out.println("COLUMNS");
        System.out.println("   Columns is a card game that play with 50 cards-5 number set 1-10.Goal of\nthe game is " +
                "reaching highest score by collecting number of sets.");
        System.out.println("\nTRANSFER");
        System.out.println("a: Selected number in from-column (or drawn number from box).\nb: The last number in to-column." +
                "\nDifference between a and b must be 0 or 1 or -1.\nIf to-column is empty, the top number" +
                " of the transferred numbers must be \n1 or 10.");
        System.out.println("\nMouse");
        System.out.println("-----");
        System.out.println("Click and hold for select numbers and release for transfer.");
        System.out.println("Press 'B' to select drawn number from box and click on the target column.");
        System.out.println("\nKeyboard");
        System.out.println("----------");
        System.out.println("Move your cursor with arrow keys.");
        System.out.println("Press 'Z' to select number to transfer from column or press 'B' to select\ndrawn number" +
                " from box and press 'X' when your cursor on the target column.");
        System.out.println("\nSCORE");
        System.out.println("   If you able to form ordered set you earn 1000 points. At the end of the\ngame your score " +
                "will be calculated by the formula\nEnd-Game Score  =  100*Finished_ordered_sets  +  ( Score /Transfer_number )");

        System.out.println("Press 'ESC' to return the menu");
    }

    public void howToPlayPisti() {
        cn.getTextWindow().setCursorPosition(28, 0);
        System.out.println("PISTI");
        System.out.println("\n  The pisti is a card game that play with a deck which includes 52 cards. The\naim of the game" +
                "is beating your opponent(Computer) by matching cards.");
        System.out.println("\nGAMEPLAY");
        System.out.println("If rank of the played card matches the rank of the previous card on\nthe pile, the player " +
                "captures the whole pile. The captured cards are stored.\nPlaying a jack also captures the whole pile," +
                " no matter what card is\non top of it. ");
        System.out.println("If the played card is not a jack and is not equal to the previous top card\nof the pile, " +
                "the played card is simply added to the top of the pile.");
        System.out.println("If the pile consists of just one card and the next player captures it by\nplaying a matching" +
                " card (not a jack) capturing player earns 10 points\nfor pisti.");
        System.out.println("Press 1,2,3 or 4 to play your cards.");
        System.out.println("\nSCORE");
        System.out.println("Each jack           1 point\n" +
                "Each ace            1 point\n" +
                "club2               2 points\n" +
                "diamond10           3 points\n" +
                "Majority of cards   3 points\n" +
                "Each pisti         10 points\n");

        System.out.println("Press 'ESC' to return the menu");
    }

}
