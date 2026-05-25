package echoserver;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

public class Lobby {
    HashMap<String, Game> games= new HashMap<>();
    public Lobby() {
    }

    public synchronized Player joinGame(Socket socket) throws IOException {
        for (Game game : games.values()) {
            if(game.state==GameState.CREATING||game.state==GameState.CREATED) {
                game.playerO=new Player(socket, Cell.O,game.ID);
                return game.playerO;
            }
        }
        String roomID= UUID.randomUUID().toString();
        Game game=new Game();
        game.ID=roomID;
        game.state=GameState.CREATING;
        games.put(roomID, game);
        game.playerX=new Player(socket, Cell.X,game.ID);
        return game.playerX;
    }


}
