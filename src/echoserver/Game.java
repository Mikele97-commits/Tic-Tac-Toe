package echoserver;


import java.io.IOException;
import java.util.Arrays;

public class Game {
    Boolean gameOver = false;
    String playerXAnswer;
    String playerOAnswer;
    String ID;
    int tileNumber;
    int lastX;
    int lastY;
    int Tiles;
    Cell[][] field;
    GameState state = null;
    Player playerX=new Player(Cell.X);
    Player playerO=new Player(Cell.O);

    public Game(int Tiles) {
        this.Tiles = Tiles;
        tileNumber = Tiles*Tiles;
        field =new Cell[Tiles][Tiles];
    }

    public Game() {

    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void initField(int size) {
        this.Tiles = size;
        this.tileNumber = size * size;
        this.field = new Cell[size][size];
        createNewField(this.field);
    }

    public synchronized void sendMessage(String message, Player player) throws IOException {
        player.out.writeBytes(message+"\n");
        player.out.flush();
    }

    public synchronized void  sendBoth(String message) throws IOException {
        playerX.out.writeBytes(message+"\n");
        playerX.out.flush();
        playerO.out.writeBytes(message+"\n");
        playerO.out.flush();
    }

    public synchronized void displayBoard(Game game) throws IOException {
        String board = GameDisplay.display(game.field);
        game.playerX.out.writeBytes(board);
        game.playerX.out.writeBytes("END\n");
        game.playerX.out.flush();//Initial display of field
        game.playerO.out.writeBytes(board);
        game.playerO.out.writeBytes("END\n");
        game.playerO.out.flush();//Initial display of field
    }

    public static void createNewField(Cell[][] field) {
        for (Cell[] cells : field) {
            Arrays.fill(cells, Cell.EMPTY);
        }
    }

    public boolean makeMove(String move, Player player) {
        if (move.length() != 2) {
            System.out.println("Invalid input");
            return false;
        }
        move=move.toUpperCase();
        String xString = move.substring(0, 1);
        int x = xString.charAt(0)-65;
        if (x<0 || x>Tiles - 1) {
            System.out.println("Invalid input x, out of bounds");
            return false;
        }
        int y = Integer.parseInt(move.substring(1, 2))-1;
        if (y<0 || y>Tiles - 1) {
            System.out.println("Invalid input y, out of bounds");
            return false;
        }

        if (field[y][x] != Cell.EMPTY) {
            System.out.println("Cell is already occupied");
            return false;
        }

        field[y][x] = player.symbol;
        lastX=x;
        lastY=y;

        return true;
    }
}

enum GameState {
    CREATING,
    CREATED,
    PLAYING

}

