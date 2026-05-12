package echoserver;

public class GameDisplay {
    static String findState(Cell cell){
        return switch (cell) {
            case EMPTY -> ".";
            case X -> "X";
            case O -> "O";
        };
    }
    public static String display(Cell[][] field) {
        StringBuilder sb= new StringBuilder();
        //Letters
        for (int col = 0; col < field[0].length; col++) {
            int ASCII = col + 65;
            char columnletter = (char)ASCII;
            sb.append("   ").append(columnletter);
            System.out.print("   " + columnletter);
        }
        sb.append("\n");
        System.out.println();
        for (int row = 0; row < field.length; row++) {
            //First row of 2
            for (int col = 0; col < field[row].length; col++) {
                if(col==0){
                    sb.append(row+1).append(" ");
                    System.out.print(row+1 + " ");
                }
                sb.append(" ").append(findState(field[row][col])).append(" |");
                System.out.print(" " + findState(field[row][col]) +" |" );
            }
            sb.append("\n").append("   ");
            System.out.println();
            System.out.print("   ");
            //Second row
            for (int col = 0; col < field[row].length; col++) {
                if(col==0){
                    sb.append("__|");
                    System.out.print("__|");
                }
                else {
                    sb.append("___|");
                    System.out.print("___|");
                }
            }
            sb.append("\n");
            System.out.println();
        }
        return sb.toString();
    }
}
