import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Pisti {

    // Additional variable
    private static Stack pistiDect, poolDect;
    private static Card[] playerDect, computerDect;

    // The enigma console from main class
    enigma.console.Console cn = Columns.cn;

    // Necessary variables to use keyboard
    private int keypr;
    private int rkey;
    private KeyListener klis;

    // Scores and other stuff
    private int playerScore = 0, computerScore = 0, turn = 1;
    private Boolean isSelected1, isSelected2;
    private Card tempCard;

    Pisti() throws InterruptedException{

        // To add keyboard listener
        listener();

        // To fill boxses
        fillPistiBoxs();

        // To print game area
        displayPisti();

        // A temp variable to store 'Selected card'
        tempCard = null;
        isSelected1 = isSelected2 = false;

        // Booleans for loops and if statements
        keypr = 0;

        while(true){

            // If key pressed
            if(keypr == 1){
                if(((rkey == KeyEvent.VK_1 || rkey == KeyEvent.VK_NUMPAD1) && playerDect[0] != null)
                        || ((rkey == KeyEvent.VK_2 || rkey == KeyEvent.VK_NUMPAD2) && playerDect[1] != null)
                        || ((rkey == KeyEvent.VK_3 || rkey == KeyEvent.VK_NUMPAD3) && playerDect[2] != null)
                        || ((rkey == KeyEvent.VK_4 || rkey == KeyEvent.VK_NUMPAD4) && playerDect[3] != null)){

                    // This part selects the card and removes it from player's cards
                    if(rkey == KeyEvent.VK_1 || rkey == KeyEvent.VK_NUMPAD1){
                        tempCard = playerDect[0];
                        playerDect[0] = null;
                    }
                    else if(rkey == KeyEvent.VK_2 || rkey == KeyEvent.VK_NUMPAD2){
                        tempCard = playerDect[1];
                        playerDect[1] = null;
                    }
                    else if(rkey == KeyEvent.VK_3 || rkey == KeyEvent.VK_NUMPAD3){
                        tempCard = playerDect[2];
                        playerDect[2] = null;
                    }
                    else{
                        tempCard = playerDect[3];
                        playerDect[3] = null;
                    }

                    // Checks if there is a match
                    if(tempCard.getNumber().equalsIgnoreCase("Joker")){
                        poolDect.push(tempCard);

                        while(!poolDect.isEmpty()){
                            addPLayerScore();
                            poolDect.pop();
                        }
                    }
                    else if(tempCard.getNumber().equalsIgnoreCase(((Card)poolDect.peek()).getNumber())){
                        poolDect.push(tempCard);

                        if(poolDect.size() == 2){
                            playerScore += 10;
                        }

                        while(!poolDect.isEmpty()){
                            addPLayerScore();
                            poolDect.pop();
                        }
                    }
                    else
                        poolDect.push(tempCard);

                    // Resets temp variable to use again for computer
                    displayPisti();
                    Thread.sleep(1000);

                    // Checks if computer has any Joker card
                    for (int i = 0; i < 4; i++) {
                        if(!poolDect.isEmpty() && computerDect[i] != null && computerDect[i].getNumber().equalsIgnoreCase("Joker")){
                            poolDect.push(computerDect[i]);
                            computerDect[i] = null;

                            while(!poolDect.isEmpty()){
                                addComputerScore();
                                poolDect.pop();
                            }
                            isSelected1 = true;
                            break;
                        }
                    }

                    // If the card is not selected it checks if there is a matched number
                    if(!isSelected1){
                        for (int i = 0; i < 4; i++) {
                            if(computerDect[i] != null && ((Card)poolDect.peek()).getNumber().equalsIgnoreCase(computerDect[i].getNumber())){
                                poolDect.push(computerDect[i]);
                                computerDect[i] = null;

                                if(poolDect.size() == 2){
                                    computerScore += 10;
                                }

                                while(!poolDect.isEmpty()){
                                    addComputerScore();
                                    poolDect.pop();
                                }

                                isSelected2 = true;
                                break;
                            }
                        }

                        // If the card is still not slected it selects a random card to play
                        if(!isSelected2){
                            for (int i = 0; i < 4; i++) {
                                if(computerDect[i] != null){
                                    poolDect.push(computerDect[i]);
                                    computerDect[i] = null;
                                    break;
                                }
                            }
                        }
                    }

                    displayPisti();
                    Thread.sleep(1000);

                    // If the turn reaches 25 it means game has been ended
                    turn++;
                    if(turn == 25){
                        endGame();
                        break;
                    }

                    if(turn != 1 && (turn - 1) % 4 == 0){
                        for (int i = 0; i < 4; i++) {
                            playerDect[i] = (Card)pistiDect.pop();
                            computerDect[i] = (Card)pistiDect.pop();
                        }
                    }

                    // Resetting necessary variables
                    isSelected1 = isSelected2 = false;
                    tempCard = null;

                    displayPisti();
                }
                else if(rkey == KeyEvent.VK_ESCAPE)
                    break;
                keypr = 0;
            }

            // Thread sleep to make code more efficient
            Thread.sleep(100);
        }
    }

    // This functions calculates the score that is going to be added
    private void addPLayerScore(){
        if(((Card)poolDect.peek()).getNumber().equalsIgnoreCase("Joker"))
            playerScore++;
        else if(((Card)poolDect.peek()).getNumber().equalsIgnoreCase("1"))
            playerScore++;
        else if(((Card)poolDect.peek()).getNumber().equalsIgnoreCase("2")
                && ((Card)poolDect.peek()).getType().equalsIgnoreCase("C"))
            playerScore += 2;
        else if(((Card)poolDect.peek()).getNumber().equalsIgnoreCase("10")
                && ((Card)poolDect.peek()).getType().equalsIgnoreCase("C"))
            playerScore += 3;
    }

    private void addComputerScore(){
        if(((Card)poolDect.peek()).getNumber().equalsIgnoreCase("Joker"))
            computerScore++;
        else if(((Card)poolDect.peek()).getNumber().equalsIgnoreCase("1"))
            computerScore++;
        else if(((Card)poolDect.peek()).getNumber().equalsIgnoreCase("2")
                && ((Card)poolDect.peek()).getType().equalsIgnoreCase("C"))
            computerScore += 2;
        else if(((Card)poolDect.peek()).getNumber().equalsIgnoreCase("10")
                && ((Card)poolDect.peek()).getType().equalsIgnoreCase("C"))
            computerScore += 3;
    }

    // Key listener for game
    private void listener() {
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

    // This one prints the game area
    private void displayPisti(){
        Columns.consoleClear();
        cn.getTextWindow().setCursorPosition(67,3);
        System.out.print("Turn: " + turn + "  ");
        cn.getTextWindow().setCursorPosition(35, 11);
        System.out.print("+---------+");
        cn.getTextWindow().setCursorPosition(35, 12);
        System.out.print(String.format("|%-6s  ", ((Card)poolDect.peek()).getNumber()) +  ((Card)poolDect.peek()).getType() + "|");
        cn.getTextWindow().setCursorPosition(35, 13);
        System.out.print("+---------+");


        cn.getTextWindow().setCursorPosition(12, 25);
        for (int i = 0; i < 4; i++)
            if(playerDect[i] != null)
                System.out.printf("+----%s----+\t", i + 1);

        cn.getTextWindow().setCursorPosition(12, 26);
        for (int i = 0; i < 4; i++)
            if(playerDect[i] != null)
                System.out.print(String.format("|%-6s  ", playerDect[i].getNumber())  +  playerDect[i].getType() + "|\t");

    }

    // This one fills the boxeses
    private void fillPistiBoxs() {
        pistiDect = new Stack(52);
        poolDect = new Stack(52);
        playerDect = new Card[4];
        computerDect = new Card[4];

        // Numberts 1 to 10
        for(int i = 1; i <= 10; i++) {
            pistiDect.push(new Card(Integer.toString(i), "♠"));
            pistiDect.push(new Card(Integer.toString(i), "♣"));
            pistiDect.push(new Card(Integer.toString(i), "♦"));
            pistiDect.push(new Card(Integer.toString(i), "♥"));
        }

        // Joker
        pistiDect.push(new Card("Joker", "♠"));
        pistiDect.push(new Card("Joker", "♣"));
        pistiDect.push(new Card("Joker", "♦"));
        pistiDect.push(new Card("Joker", "♥"));

        // Queen
        pistiDect.push(new Card("Queen", "♠"));
        pistiDect.push(new Card("Queen", "♣"));
        pistiDect.push(new Card("Queen", "♦"));
        pistiDect.push(new Card("Queen", "♥"));

        // King
        pistiDect.push(new Card("King", "♠"));
        pistiDect.push(new Card("King", "♣"));
        pistiDect.push(new Card("King", "♦"));
        pistiDect.push(new Card("King", "♥"));

        pistiDect.shuffle();

        // Filling the dects
        for(int i = 0; i < 4; i++){
            poolDect.push(pistiDect.pop());
            playerDect[i] = (Card)pistiDect.pop();
            computerDect[i] = (Card)pistiDect.pop();
        }

        // If the first element of dect is joker the cards are being distributed again.
        if(((Card)poolDect.peek()).getNumber().equalsIgnoreCase("Joker"))
            fillPistiBoxs();
    }

    // If the game is ended this function is being called, and it prints the winner
    private void endGame() throws InterruptedException{
        Columns.consoleClear();
        cn.getTextWindow().setCursorPosition(20, 10);

        if(playerScore > computerScore){
            System.out.print("Player wins with score: " + (playerScore));
        }
        else if(playerScore < computerScore){
            System.out.print("Computer wins with score: " + (computerScore));
        }
        else{
            System.out.println("        No one wins!");
        }

        cn.getTextWindow().setCursorPosition(0, 25);
        System.out.print("Please press ESC to return menu");

        keypr = 0;
        while(true){
            if(keypr == 1){
                keypr = 0;
                if(rkey == KeyEvent.VK_ESCAPE){
                    break;
                }
            }
            Thread.sleep(500);
        }
    }
}
