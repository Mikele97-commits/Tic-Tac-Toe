package echoserver;


import java.io.IOException;
import java.util.Arrays;

public class Game {

    int tieCounter=0;
    int tileNumber;
    int lastX;
    int lastY;
    int Tiles;
    Cell[][] field;
    GameState state = GameState.WAITING;
    Player playerX=new Player(Cell.X);
    Player playerO=new Player(Cell.O);

    public Game(int Tiles) {
        this.Tiles = Tiles;
        tileNumber = Tiles*Tiles;
        field =new Cell[Tiles][Tiles];

    }

    public Game() {

    }

    public void initField(int size) {
        this.Tiles = size;
        this.tileNumber = size * size;
        this.field = new Cell[size][size];
        createNewField(this.field);
    }

    public synchronized void  sendBoth(String message) throws IOException {
        playerX.out.writeBytes(message);
        playerX.out.flush();
        playerO.out.writeBytes(message);
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



    /*public void round(Socket socket, Game game){
        BufferedReader brinp = null;
        DataOutputStream out = null;
        String threadName = Thread.currentThread().getName();

        try{
            brinp = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );
            out = new DataOutputStream(socket.getOutputStream());
        }
        catch(IOException e){
            System.out.println(threadName + "| Błąd przy tworzeniu strumieni " + e);
            return;
        }
        System.out.println("Give size of side");

        int x = sc.nextInt();
        Scanner sc = new Scanner(System.in);
        //Game game = new Game(x);
        createNewField(game.field);
        GameDisplay.display(game.field);
        while(true){
            System.out.println("Player X:");
            String move = sc.next();
            while (!game.makeMove(move, playerX)) {
                System.out.println("Invalid move, please try again");
                move = sc.next();
            }
            GameDisplay.display(game.field);
            if (CheckVictory.check(game.lastY, game.lastX, game.field )){
                System.out.println("Player X wins!");
                return;
            }
            game.tieCounter++;
            if (game.tieCounter==game.tileNumber){
                System.out.println("DRAW");
                return;
            }

            System.out.println("Player O:");
            move = sc.next();
            while (!game.makeMove(move, playerO)) {
                System.out.println("Invalid move, please try again");
                move = sc.next();
            }
            GameDisplay.display(game.field);
            if (CheckVictory.check(game.lastY, game.lastX, game.field )){
                System.out.println("Player Y wins!");
                return;
            }
            game.tieCounter++;
            if (game.tieCounter==game.tileNumber){
                System.out.println("DRAW");
                return;
            }
        }

    }*/
}

enum GameState {
    WAITING,
    PLAYING

}

