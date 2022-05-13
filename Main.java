package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        playGame();
    }

    private static void playGame() {
        /* Method with gameplay logic */

        // variables
        int secretCodeLength;
        int numberOfUniqueSymbols;
        int turn = 1; // starting turn of user guesses
        String userGuess = ""; // variable to store current user guess

        // ask user to enter the length of the secret code
        System.out.println("Please, enter the secret code's length:");

        // check on validity of code length entry and finish the game if improper input received
        try {
            secretCodeLength = sc.nextInt();
        } catch (RuntimeException e) {
            System.out.println("Error! You have to enter the number.");
            return;
        }

        if (secretCodeLength > 36) {
            System.out.println("Error. You can't have more than 36 symbols in a secret number.");
            return;
        } else if (secretCodeLength == 0) {
            System.out.println("Error: Length of secret code should be greater than 0.");
            return;
        }

        // ask user for number of possible unique symbols in code
        System.out.println("Input the number of possible symbols in the code:");

        // check the validity of input and stop the game if invalid input is provided
        try {
            numberOfUniqueSymbols = sc.nextInt();
        } catch (RuntimeException e) {
            System.out.println("Error. You have to enter the number.");
            return;
        }

        if (numberOfUniqueSymbols < secretCodeLength) {
            System.out.printf("Error. It's impossible to generate the code with a length of %d with %d uniques symbols.", secretCodeLength, numberOfUniqueSymbols);
            return;
        } else if (numberOfUniqueSymbols > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z)");
            return;
        }


        // generate secret code and print what characters have been used in code generation
        String secretCode = generateSecretNumber(secretCodeLength, numberOfUniqueSymbols);

        System.out.print("The secret is prepared: ");

        for (int i = 0; i < secretCodeLength; i++) { // print asterisks for each symbol
            System.out.print("*");
        }

        System.out.print(" ");

        // print used characters
        printUsedCharacters(numberOfUniqueSymbols);
        System.out.println();

        // Start the game
        System.out.println("Okay, let's start a game!");

        // Ask for input until the secret code is guessed
        while (!userGuess.equals(secretCode)) {
            System.out.printf("Turn %d:", turn);
            System.out.println();
            userGuess = sc.next().toLowerCase();

            int bulls = getBulls(secretCode, userGuess, secretCodeLength);
            int cows = getCows(secretCode, userGuess);

            printResults(bulls, cows);

            turn++;
        }

        System.out.println("\nCongratulations! You guessed the secret code.");
    }

    private static String generateSecretNumber(int length, int numberOfUniqueSymbols) {
        /* method takes in desired length of secret code and number of unique symbols to generate and returns random secret code */

        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        String charPool = "0123456789abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < length; i++) {
            int randomIndex = rand.nextInt(numberOfUniqueSymbols);
            char randomChar = charPool.charAt(randomIndex);
            if (sb.indexOf(String.valueOf(randomChar)) >= 0) {
                i = i - 1;
                continue;
            }
            sb.append(randomChar);
        }

        return sb.toString();
    }


    private static int getBulls(String secretCode, String userGuess, int length) {
        /* method calculates number of bulls for user guess. Bull is when the symbol is guessed correctly in terms of place and value */

        if (secretCode.equals(userGuess)) {
            return length;
        } else {
            int count = 0;
            for (int i = 0; i < userGuess.length(); i++) {
                if (secretCode.charAt(i) == userGuess.charAt(i)) {
                    count++;
                }
            }
            return count;
        }
    }

    private static int getCows(String secretCode, String userGuess) {
        /* method calculates number of cows for user guess. Cow is when the symbol is guessed correctly in terms of value but not in terms of place */

        int count = 0;
        for (int i = 0; i < userGuess.length(); i++) {
            if (secretCode.contains(String.valueOf(userGuess.charAt(i))) && userGuess.charAt(i) != secretCode.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    private static void printResults(int bulls, int cows) {
        /* helper method that takes in number of bulls and cows and prints the results of user guess (number of bulls and cows, if any) */

        String cowsText;
        String bullsText;
        if (bulls > 1) {
            bullsText = "bulls";
        } else {
            bullsText = "bull";
        }
        if (cows > 1) {
            cowsText = "cows";
        } else {
            cowsText = "cow";
        }
        if (bulls > 0 && cows > 0) {
            System.out.printf("Grade: %d %s and %d %s", bulls, bullsText, cows, cowsText);
        } else if (bulls == 0 && cows > 0) {
            System.out.printf("Grade: %d %s", cows, cowsText);
        } else if (bulls > 0 && cows == 0) {
            System.out.printf("Grade: %d %s", bulls, bullsText);
        } else if (bulls == 0 && cows == 0) {
            System.out.println("Grade: None");
        } else {
            System.out.println("Strange results");
        }
        System.out.println();
    }

    private static void printUsedCharacters(int usedChars) {
        /* helper method to print what characters have been used in secret code. Takes in the number of used chars and prints formatted output */
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        if (usedChars <= 10) {
            System.out.printf("(0-%d).", usedChars - 1);
        } else if (usedChars == 11){
            System.out.print("(0-9, a).");
        } else {
            int letterChars = usedChars - 10 + 1;
            String subAlphabet = alphabet.substring(0, letterChars - 1);
            System.out.printf("(0-9, a-%s).", subAlphabet.charAt(subAlphabet.length() - 1));
        }
    }
}
