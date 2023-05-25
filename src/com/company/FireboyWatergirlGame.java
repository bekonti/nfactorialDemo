package com.company;

import java.io.*;
import java.nio.file.Watchable;
import java.util.Scanner;

public class FireboyWatergirlGame {
    private static final int ROWS = 10;
    private static final int COLUMNS = 10;

    private static final char FIREBOY_CHAR = 'F';
    private static final char WATERGIRL_CHAR = 'W';
    private static final char WALL_CHAR = '#';
    private static final char EMPTY_CHAR = ' ';
    private static final char GOAL_CHAR = 'G';
    private static char[][] selectedMap = null;
    private static boolean fireboyDone = false;
    private static boolean waterGirlDone = false;

    private static final char[][] map1 = {
            {WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR},
            {WALL_CHAR, FIREBOY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, WALL_CHAR},
            {WALL_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, WALL_CHAR},
            {WALL_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, WALL_CHAR},
            {WALL_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, WALL_CHAR},
            {WALL_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, WALL_CHAR},
            {WALL_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, WALL_CHAR},
            {WALL_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, WALL_CHAR},
            {WALL_CHAR, EMPTY_CHAR, EMPTY_CHAR, GOAL_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, WATERGIRL_CHAR, WALL_CHAR},
            {WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR}
    };

    private static final char[][] map2 = {
            {WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR},
            {WALL_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, GOAL_CHAR, WALL_CHAR},
            {WALL_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR},
            {WALL_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, WALL_CHAR},
            {WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, EMPTY_CHAR, WALL_CHAR},
            {WALL_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, WALL_CHAR},
            {WALL_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, WALL_CHAR},
            {WALL_CHAR, FIREBOY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, WALL_CHAR},
            {WALL_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, WATERGIRL_CHAR, WALL_CHAR},
            {WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR, WALL_CHAR}
    };

    private static int fireboyX;
    private static int fireboyY;
    private static int watergirlX;
    private static int watergirlY;

    private static boolean isPaused = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter '1' for Map 1 or '2' for Map 2:");
        String mapSelection = scanner.nextLine();
        switch (mapSelection) {
            case "1":
                selectedMap = map1;
                break;
            case "2":
                selectedMap = map2;
                break;
            default:
                System.out.println("Invalid map selection. Exiting the game.");
                return;
        }
        initializeGame(selectedMap);
        printMap();

        while (true) {
            if (waterGirlDone)
                System.out.println("Watergirl finished waiting Fireboy");
            if (fireboyDone)
                System.out.println("Fireboy finished waiting Watergirl");
            System.out.println("Enter 'w' to move Fireboy up, 's' to move Fireboy down, 'a' to move Fireboy left, 'd' to move Fireboy right.");
            System.out.println("Enter 'i' to move Watergirl up, 'k' to move Watergirl down, 'j' to move Watergirl left, 'l' to move Watergirl right.");
            System.out.println("Enter 'p' to pause the game, 'c' to continue, 's' to save the game, or 'q' to quit.");

            if (!isPaused) {
                String input = scanner.nextLine();
                if (input.equals("q")) {
                    System.out.println("Game over. You quit the game.");
                    break;
                } else if (input.equals("p")) {
                    isPaused = true;
                    System.out.println("Game paused.");
                    continue;
                }

                moveFireboyAndWatergirl(input);
                printMap();
                if (checkWinCondition()) {
                    System.out.println("Congratulations! You won the game.");
                    break;
                }
            } else {
                String input = scanner.nextLine();
                if (input.equals("q")) {
                    System.out.println("Game over. You quit the game.");
                    break;
                } else if (input.equals("c")) {
                    isPaused = false;
                    System.out.println("Game resumed.");
                } else if (input.equals("s")) {
                    saveGame(selectedMap);
                    System.out.println("Game saved.");
                }
            }
        }
    }

    private static void initializeGame(char[][] map) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (map[row][col] == FIREBOY_CHAR) {
                    fireboyX = col;
                    fireboyY = row;
                } else if (map[row][col] == WATERGIRL_CHAR) {
                    watergirlX = col;
                    watergirlY = row;
                }
            }
        }
    }

    private static void printMap() {
        System.out.print("  ");
        for (int row = 0; row < ROWS; row++) {
            System.out.print(row + " ");
        }
        System.out.println();
        for (int row = 0; row < ROWS; row++) {
            System.out.print(row + " ");
            for (int col = 0; col < COLUMNS; col++) {
                System.out.print(selectedMap[row][col] + " ");
            }
            System.out.println();
        }
    }

    private static void moveFireboyAndWatergirl(String direction) {
        int newFireboyX = fireboyX;
        int newFireboyY = fireboyY;
        int newWatergirlX = watergirlX;
        int newWatergirlY = watergirlY;

        switch (direction) {
            case "w": // Fireboy: Move up
                newFireboyY--;
                break;
            case "s": // Fireboy: Move down
                newFireboyY++;
                break;
            case "a": // Fireboy: Move left
                newFireboyX--;
                break;
            case "d": // Fireboy: Move right
                newFireboyX++;
                break;
            case "i": // Watergirl: Move up
                newWatergirlY--;
                break;
            case "k": // Watergirl: Move down
                newWatergirlY++;
                break;
            case "j": // Watergirl: Move left
                newWatergirlX--;
                break;
            case "l": // Watergirl: Move right
                newWatergirlX++;
                break;
            default:
                System.out.println("Invalid input. Please try again.");
                return;
        }

        if (!isMoveValid(newFireboyX, newFireboyY)) {
            newFireboyX = fireboyX;
            newFireboyY = fireboyY;
        }

        if (!isMoveValid(newWatergirlX, newWatergirlY)) {
            newWatergirlX = watergirlX;
            newWatergirlY = watergirlY;
        }

        if (!fireboyDone && !waterGirlDone) {
            if (newFireboyX == newWatergirlX && newFireboyY == newWatergirlY) {
                System.out.println("Invalid move. Fireboy and Watergirl cannot occupy the same position.");
                return;
            }
        }
        ;


        if (!fireboyDone)
            selectedMap[fireboyY][fireboyX] = EMPTY_CHAR;
        fireboyX = newFireboyX;
        fireboyY = newFireboyY;
        if (selectedMap[newFireboyY][newFireboyX] != GOAL_CHAR)
            selectedMap[fireboyY][fireboyX] = FIREBOY_CHAR;
        else
            fireboyDone = true;

        if (!waterGirlDone)
            selectedMap[watergirlY][watergirlX] = EMPTY_CHAR;
        watergirlX = newWatergirlX;
        watergirlY = newWatergirlY;
        if (selectedMap[newWatergirlY][newWatergirlX] != GOAL_CHAR)
            selectedMap[watergirlY][watergirlX] = WATERGIRL_CHAR;
        else
            waterGirlDone = true;
    }

    private static boolean isMoveValid(int x, int y) {
        return x >= 0 && x < COLUMNS && y >= 0 && y < ROWS && selectedMap[y][x] != WALL_CHAR;
    }

    private static boolean checkWinCondition() {
        return fireboyDone && waterGirlDone || // check twice 😅🙃
                selectedMap[fireboyY][fireboyX] == GOAL_CHAR && selectedMap[watergirlY][watergirlX] == GOAL_CHAR;
    }

    private static void saveGame(char[][] map) {
        try (FileWriter writer = new FileWriter("game_save.txt")) {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLUMNS; col++) {
                    writer.write(map[row][col]);
                }
                writer.write(System.lineSeparator());
            }
            writer.write(fireboyX + "," + fireboyY);
            writer.write(System.lineSeparator());
            writer.write(watergirlX + "," + watergirlY);
            writer.write(System.lineSeparator());
            writer.write(isPaused ? "1" : "0");
        } catch (IOException e) {
            System.out.println("Failed to save the game: " + e.getMessage());
        }
    }
}
