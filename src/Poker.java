import java.util.*;
public class Poker {
    private final String[] SUITS = { "C", "D", "H", "S" };
    private final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K" };

    private int chips = 25;
    private int wager;
    private int cardsChosen;
    private int remove = -1;
    private final Player player;
    private List<Card> deck;
    private final Scanner in;

    public Poker() {
        this.player = new Player();
        this.in = new Scanner(System.in);
    }

    public void play() {
        wager();
        clearHand();
        clearInfo();
        shuffleAndDeal();
        showHand();
        takeTurn();
        endRound();
    }

    private void wager() {
        do {
            System.out.println("You have " + chips + "chips.");
            System.out.println("Keeping in mind that you cannot wager less than 1 or more than 25, how many chips would you like to wager?");
            wager = in.nextInt();
            if (wager > chips) {
                System.out.println("Sorry, you only have " + chips + " chips remaining, but you wagered " + wager + " chips. Please wager a valid amount!");
            }
        } while (wager < 1 || wager > 25 || wager > chips);
        in.nextLine();
    }

    public void shuffleAndDeal() {
        if (deck == null) {
            initializeDeck();
        }
        Collections.shuffle(deck);
        while (player.getHand().size() < 5) {
            player.takeCard(deck.remove(0));
        }
    }

    private void initializeDeck() {
        deck = new ArrayList<>(52);

        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(new Card(rank, suit));     // adds 52 cards to the deck (13 ranks, 4 suits)
            }
        }
    }

    private void takeTurn() {
        cardsChosen = -1;
        do {
            System.out.println("Keeping in mind that a minimum of 0 cards and a maximum of 3 cards can be switched, how many would you like to switch?");
            cardsChosen = in.nextInt();
        } while (cardsChosen <= -1 || cardsChosen > 3);
        for (int k = 0; k < cardsChosen; k++) {
            do {
                System.out.println("Keeping in mind that the first card has an index of zero, choose the index of the card you would like to remove.");
                remove = in.nextInt();
            } while (remove <= -1 || remove > player.hand.size());
            player.hand.remove(remove);
            player.handRanks.remove(remove);
            player.handSuits.remove(remove);
            showHand();
        }
        shuffleAndDeal();
        showHand();

    }

    private void endRound() {
        player.calculateHandType();

        switch (player.handType) {
            case "RoyalFlush":
                chips += wager * 100;
                System.out.println("Congratulations, you have just won: Royal Flush!");
                break;
            case "StraightFlush":
                chips += wager * 50;
                System.out.println("Congratulations, you have just won: Straight Flush!");
                break;
            case "FourOfAKind":
                chips += wager * 25;
                System.out.println("Congratulations, you have just won: Four of a Kind!");
                break;
            case "FullHouse":
                chips += wager * 15;
                System.out.println("Congratulations, you have just won: Full House!");
                break;
            case "Flush":
                chips += wager * 10;
                System.out.println("Congratulations, you have just won: Flush!");
                break;
            case "Straight":
                chips += wager * 5;
                System.out.println("Congratulations, you have just won: Straight!");
                break;
            case "ThreeOfAKind":
                chips += wager * 3;
                System.out.println("Congratulations, you have just won: Three of a Kind!");
                break;
            case "TwoPair":
                chips += wager * 2;
                System.out.println("Congratulations, you have just won: Two Pair!");
                break;
            case "HighPair":
                chips += wager * 1;
                System.out.println("Congratulations, you have just won: Pair!");
                break;
            default:
                chips -= wager;
                System.out.println("You lost. Better luck next time.");
                break;
        }

        if (chips <= 0) {
            endGame();
        }
        in.nextLine();
        String answer = "";
        do {
            System.out.println("Would you like to end(E) or continue(c)?");
            answer = in.nextLine().toUpperCase();
        } while (answer.equals("E") == false && answer.equals("C") == false);
        if (answer.equals("E")) {
            endGame();
        } else {
            play();
        }

    }

    private void showHand() {
        System.out.println("\nPLAYER hand: " + player.getHand());
    }

    private void clearHand() {
        player.clearHand();
    }

    private void clearInfo() {
        player.clearInfo();
    }

    private void endGame() {
        if (chips <= 0) {
            System.out.println("You ran out of chips and lost.");
        } else {
            System.out.println("You ended up with " + chips + " chips. Good job!");
        }
        System.exit(0);
    }



    public static void main(String[] args) {
        System.out.println("POKER");
        new Poker().play();
    }
}
