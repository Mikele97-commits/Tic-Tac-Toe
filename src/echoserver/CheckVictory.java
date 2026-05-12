package echoserver;

public class CheckVictory {

    public static boolean check(int y, int x, Cell[][] field) {

        x = x;
        y = y;
        int startingX = x;
        int startingY = y;
        Cell symbol = field[y][x];
        int[] xdirection = {0, 1};
        int[] ydirection = {1, 0};
        int[] notXdirection = {0, -1};
        int[] notYdirection = {-1, 0};
        int[] diag1 = {1, 1};
        int[] diag2 = {1, -1};
        int[] diag3 = {-1, 1};
        int[] diag4 = {-1, -1};
        int[][] directions = {xdirection, ydirection, notXdirection, notYdirection, diag1, diag2, diag4, diag3};
        int[][] middleDirections = {xdirection, ydirection, diag1, diag2};

        for (int[] direction : directions) {
            int correct = 0;
            x = startingX;
            y = startingY;
            while (y >= 0 && y < field.length && x >= 0 && x < field[y].length) {
                if (field[y][x] == symbol)
                    correct++;
                else {
                    correct = 0;
                    break;
                }
                if (correct == 3) {
                    System.out.println("VICTORIA");
                    return true;
                }
                y = y + direction[0];
                x = x + direction[1];
            }
        }
        for (int[] direction : middleDirections) {
            y = startingY + direction[0];
            x = startingX + direction[1];
            if (y >= 0 && y < field.length && x >= 0 && x < field[y].length) {
                if (field[y][x] == symbol) {
                    y = startingY - direction[0];
                    x = startingX - direction[1];
                    if (y >= 0 && y < field.length && x >= 0 && x < field[y].length) {
                        if (field[y][x] == symbol) {
                            System.out.println("VICTORIA");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
