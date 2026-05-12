package echoserver;

public class GameDisplay {
    static String findState(Cell cell){
        return switch (cell) {
            case EMPTY -> ".";
            case X -> "X";
            case O -> "O";
        };
    }
    public static void display(Cell[][] field) {
        //Letters
        for (int col = 0; col < field[0].length; col++) {
            int ASCII = col + 65;
            char columnletter = (char)ASCII;
            System.out.print("   " + columnletter);
        }
        System.out.println();
        for (int row = 0; row < field.length; row++) {
            //First row of 2
            for (int col = 0; col < field[row].length; col++) {
                if(col==0){
                    System.out.print(row+1 + " ");
                }
                System.out.print(" " + findState(field[row][col]) +" |" );
            }
            System.out.println();
            System.out.print("   ");
            //Second row
            for (int col = 0; col < field[row].length; col++) {
                if(col==0){
                    System.out.print("__|");
                }
                else
                    System.out.print("___|");

            }

            System.out.println();
        }
    }
}
