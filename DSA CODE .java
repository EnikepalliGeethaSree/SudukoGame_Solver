import java.util.*;

public class Sudokulevel{
    private static final int SIZE = 9;
    private static final int EMPTY = 0;
    private static int[][] board;
    private static long startTime;
    private static long endTime;

    private static void removeNumbers(int[][] board, int count) {
        Random random = new Random();
        while (count > 0) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if (board[row][col]!= EMPTY) {
                board[row][col] = EMPTY;
                count--;
            }
        }
    }

    private static void generateSudoku(int count) {
        board = new int[SIZE][SIZE];
        solveSudoku(board);
        removeNumbers(board, count); // Adjust the second argument to change the difficulty level
    }

    private static boolean isSolved(int[][] board) {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isInRow(int[][] board, int row, int num) {
        for (int col = 0; col < SIZE; col++) {
            if (board[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInCol(int[][] board, int col, int num) {
        for (int row = 0; row < SIZE; row++) {
            if (board[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInBox(int[][] board, int startRow, int startCol, int num) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row + startRow][col + startCol] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isValidMove(int[][] board, int row, int col, int num) {
        return !isInRow(board, row, num) && !isInCol(board, col, num) && !isInBox(board, row - row % 3, col - col % 3, num);
    }

    private static boolean solveSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == EMPTY) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValidMove(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku(board)) {
                                return true;
                            } else {
                                board[row][col] = EMPTY;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static void printSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }

    private static void showSolution() {
        int[][] solvedBoard = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, solvedBoard[i], 0, SIZE);
        }
        if (!solveSudoku(solvedBoard)) {
            System.out.println("No solution exists.");
        } else {
            System.out.println("Here is the solution:");
            printSudoku(solvedBoard);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to solve another Sudoku puzzle? (yes/no): ");
        String choice = scanner.next().toLowerCase();
        while (!choice.equals("yes") && !choice.equals("no")) {
            System.out.print("Please enter 'yes' or 'no': ");
            choice = scanner.next().toLowerCase();
        }
        if (choice.equals("no")) {
        	System.out.println("ThankYou for Playing!!");
        	System.exit(0);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Sudoku Solver!");

        System.out.print("Select difficulty level (1-5): ");
        int difficultyLevel = scanner.nextInt();

        int count;
        switch (difficultyLevel) {
            case 1:
                count = 30; // Easy
                break;
            case 2:
                count = 40; // Medium
                break;
            case 3:
                count = 50; // Hard
                break;
            case 4:
                count = 60; // Expert
                break;
            case 5:
                count = 65; // Extreme
                break;
            default:
                System.out.println("Invalid difficulty level. Defaulting to Medium.");
                count = 40;
        }

        boolean playAgain = true;

        while (playAgain) {
            generateSudoku(count);
            System.out.println("Here is the initial Sudoku puzzle:");
            printSudoku(board);
            System.out.println("Let's solve the puzzle. Enter row and column (1-9) separated by space, then the number (1-9) to fill in the cell, or enter '0 0 0' to show the solution:");

            startTime = System.currentTimeMillis(); // start the timer

            while (!isSolved(board)) {
                System.out.print("Enter your move: ");
                int row = scanner.nextInt() - 1;
                int col = scanner.nextInt() - 1;

                if (row == -1 && col == -1) {
                    showSolution();
                    break;
                }

                if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
                    System.out.println("Invalid input. Row and column numbers should be between 1 and 9.");
                    continue;
                }

                if (board[row][col]!= EMPTY) {
                    System.out.println("Invalid move. Cell is already filled.");
                    continue;
                }

                int num = scanner.nextInt();

                if (num < 1 || num > 9) {
                    System.out.println("Invalid number. Please enter a number between 1 and 9.");
                    continue;
                }

                if (!isValidMove(board, row, col, num)) {
                    System.out.println("Invalid move. Number already exists in row, column, or box.");
                    continue;
                }

                board[row][col] = num;
                System.out.println("Updated Sudoku puzzle:");
                printSudoku(board);
            }

            endTime = System.currentTimeMillis(); // stop the timer

            if (isSolved(board)) {
                System.out.println("Congratulations! You solved the Sudoku puzzle in " + (endTime - startTime) / 1000 + " seconds.");
                System.out.print("Do you want to solve another Sudoku puzzle? (yes/no): ");
                String choice = scanner.next().toLowerCase();
                while (!choice.equals("yes") &&!choice.equals("no")) {
                    System.out.print("Please enter 'yes' or 'no': ");
                    choice = scanner.next().toLowerCase();
                }
                if (choice.equals("no")) {
                    playAgain = false;
                }
            }
        }

        System.out.println("Thank you for playing Sudoku Solver!");

    }
}