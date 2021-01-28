package ru.geekbrains.Tic_Tac_Toe;

import java.util.Random;
import java.util.Scanner;


public class Main {
    static class Field {
        int x;
        int y;
        boolean wouldWin = false;

        Field(int x, int y, boolean wouldWin) {
            this.x = x;
            this.y = y;
            this.wouldWin = wouldWin;
        }
    }

    public static char [][] map;
    public static final int SIZE = 3;
    public static final char DOT_EMPTY = '•';
    public static final char DOT_X = 'X';
    public static final char DOT_O = 'O';
    public static Scanner sc = new Scanner(System.in);
    public static Random rand = new Random();

    public static void main(String[] args) {
        initMap();
        printMap();
        while (true){
            humanTurn();
            printMap();
            if (checkWin(DOT_X)) {
                System.out.println("You are winner");
                break;
            }

            if (isMapFull()) {
                System.out.println("Dead heat!");
                break;
            }

            aiTurn();
            printMap();
            if (checkWin(DOT_O)) {
                System.out.println("You lose!");
            }

            if (isMapFull()) {
                System.out.println("Dead heat!");
                break;
            }
        }
        System.out.println("Game over!");
    }

    public static void initMap() {
        map = new char [SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = DOT_EMPTY;
            }
        }
    }

    public static void printMap(){
        for (int i = 0; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i+1) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void humanTurn(){
        int x, y;
        do{
            System.out.println("Enter coordinates in the format X Y");
            x = sc.nextInt() - 1;
            y = sc.nextInt() - 1;
        } while (!isCellValid(x, y));
        map[y][x] = DOT_X;
    }

    public static boolean isCellValid(int x, int y){
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE){
            return false;
        }
        if (map[y][x] == DOT_EMPTY) {
            return true;
        } return false;
    }

    public static boolean isMapFull(){
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < map.length; j++){
                if (map[i][j] == DOT_EMPTY){
                    return false;
                }
            }
        }
        return true;
    }

    public static void aiTurn(){
        int x, y;
        do {
            Field bestTryField = defendOrWinField();
            if (map[1][1] == DOT_EMPTY){
                x = 1;
                y = 1;
            } else if (bestTryField != null) {
                x = bestTryField.x;
                y = bestTryField.y;
            } else {
                x = rand.nextInt(SIZE);
                y = rand.nextInt(SIZE);
            }
        } while (!isCellValid(x, y));
        System.out.println("PC was go3 to the point " + (x + 1) + " " + (y + 1));
        map[y][x] = DOT_O;
    }

    private static Field defendOrWinBlock(int block, boolean isRow) {
        int enemyMarkCount = 0;
        int myMarkCount = 0;
        int turnIndex = -1;
        for (int i = 0; i < SIZE; i++) {
            char dot = isRow ? map[block][i] : map[i][block];
            if(dot == DOT_X) {
                enemyMarkCount++;
            } else if (dot == DOT_O) {
                myMarkCount++;
            } else if (dot == DOT_EMPTY) {
                turnIndex = i;
            }
        }

        if ((myMarkCount == 2 || enemyMarkCount == 2) && turnIndex != -1) {
            if (isRow) {
                return new Field(turnIndex, block, myMarkCount == 2);
            } else {
                return new Field(block, turnIndex, myMarkCount == 2);
            }
        } else {
            return null;
        }
    }

    private static Field defendOrWinField() {
        Field bestTry = null;
        for (int i = 0; i < SIZE; i++) {
            Field field;
            field = defendOrWinBlock(i, true);
            if (field != null) {
                bestTry = field;
                if (bestTry.wouldWin) {
                    return bestTry;
                }
            }
            field = defendOrWinBlock(i, false);
            if (field != null) {
                bestTry = field;
                if (bestTry.wouldWin) {
                    return bestTry;
                }
            }
        }

        int enemyMarkCountMain = 0;
        int myMarkCountMain = 0;
        int enemyMarkCountCross = 0;
        int myMarkCountCross = 0;
        int turnIndexMain = -1;
        int turnIndexCross = -1;
        for (int i = 0; i < SIZE; i++) {
            char mainDot = map[i][i];
            if(mainDot == DOT_X) {
                enemyMarkCountMain++;
            } else if (mainDot == DOT_O) {
                myMarkCountMain++;
            } else if (mainDot == DOT_EMPTY) {
                turnIndexMain = i;
            }

            char crossDot = map[i][SIZE - 1 - i];
            if(crossDot == DOT_X) {
                enemyMarkCountCross++;
            } else if (mainDot == DOT_O) {
                myMarkCountCross++;
            } else if (crossDot == DOT_EMPTY) {
                turnIndexCross = i;
            }
        }

        if((myMarkCountMain == 2 || enemyMarkCountMain == 2) && turnIndexMain != -1) {
            bestTry = new Field(turnIndexMain, turnIndexMain, myMarkCountMain == 2);
            if (bestTry.wouldWin) {
                return bestTry;
            }
        } else if ((myMarkCountCross == 2 || enemyMarkCountCross == 2) && turnIndexCross != -1) {
            bestTry = new Field(SIZE - 1 - turnIndexCross, turnIndexCross, myMarkCountCross == 2);
        }
        return bestTry;
    }

    public static boolean checkWin(char symb) {
        boolean isMainDiagFilled = true;
        boolean isCrossDiagFilled = true;
        for (int i = 0; i < SIZE; i++) {
            boolean isRowFilled = true;
            boolean isColumnFilled = true;
            for (int j = 0; j < SIZE; j++) {
                isRowFilled = isRowFilled && map[i][j] == symb;
                isColumnFilled = isColumnFilled && map[j][i] == symb;
            }
            if (isRowFilled || isColumnFilled) {
                return true;
            }
            isMainDiagFilled = isMainDiagFilled && map[i][i] == symb;
            isCrossDiagFilled = isCrossDiagFilled && map[i][map.length - 1 - i] == symb;
        }
        return isMainDiagFilled || isCrossDiagFilled;
    }


}

