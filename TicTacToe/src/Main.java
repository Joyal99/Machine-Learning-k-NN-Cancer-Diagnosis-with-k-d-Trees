import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain;

        do {
            // Get player names
            System.out.print("Enter the name of player 1 (X): ");
            String player1 = scanner.next();
            System.out.print("Enter the name of player 2 (O): ");
            String player2 = scanner.next();

            // Ask user for the size of the board
            System.out.print("Enter the number of rows: ");
            int rows = getValidIntegerInput(scanner);
            System.out.print("Enter the number of columns: ");
            int cols = getValidIntegerInput(scanner);

            // Create and initialize the board
            char[][] board = new char[rows][cols];
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    board[row][col] = ' ';
                }
            }

            char player = 'X';
            boolean gameOver = false;

            while (!gameOver) {
                printBoard(board);
                String currentPlayer = (player == 'X') ? player1 : player2;
                System.out.print(currentPlayer + " (" + player + ") enter row and column: ");
                int row = getValidIntegerInput(scanner);
                int col = getValidIntegerInput(scanner);

                if (row >= 0 && row < rows && col >= 0 && col < cols && board[row][col] == ' ') {
                    board[row][col] = player; // Place the element
                    gameOver = haveWon(board, player);
                    if (gameOver) {
                        printBoard(board);
                        System.out.println("Congratulations " + currentPlayer + "! You have won!");
                    } else if (isBoardFull(board)) {
                        printBoard(board);
                        System.out.println("It's a draw!");
                        gameOver = true;
                    } else {
                        player = (player == 'X') ? 'O' : 'X';
                    }
                } else {
                    System.out.println("Invalid move. Try again!");
                }
            }

            System.out.print("Do you want to play again? (yes/no): ");
            playAgain = scanner.next().equalsIgnoreCase("yes");
        } while (playAgain);

        System.out.println("Thank you for playing!");
        scanner.close();
    }

    public static int getValidIntegerInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter an integer: ");
            scanner.next(); // Discard invalid input
        }
        return scanner.nextInt();
    }

    public static boolean haveWon(char[][] board, char player) {
        int rows = board.length;
        int cols = board[0].length;

        // Check the rows
        for (int row = 0; row < rows; row++) {
            boolean win = true;
            for (int col = 0; col < cols; col++) {
                if (board[row][col] != player) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }

        // Check the columns
        for (int col = 0; col < cols; col++) {
            boolean win = true;
            for (int row = 0; row < rows; row++) {
                if (board[row][col] != player) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }

        // Check the main diagonal
        boolean win = true;
        for (int i = 0; i < Math.min(rows, cols); i++) {
            if (board[i][i] != player) {
                win = false;
                break;
            }
        }
        if (win) return true;

        // Check the anti-diagonal
        win = true;
        for (int i = 0; i < Math.min(rows, cols); i++) {
            if (board[i][cols - 1 - i] != player) {
                win = false;
                break;
            }
        }
        if (win) return true;

        return false;
    }

    public static boolean isBoardFull(char[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public static void printBoard(char[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                System.out.print(board[row][col]);
                if (col < board[row].length - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
            if (row < board.length - 1) {
                for (int col = 0; col < board[row].length; col++) {
                    if (col < board[row].length - 1) {
                        System.out.print("--+--");
                    } else {
                        System.out.print("---");
                    }
                }
                System.out.println();
            }
        }
    }
}
