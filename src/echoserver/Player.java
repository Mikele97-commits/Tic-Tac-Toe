package echoserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Player {
    Cell symbol;
    Socket socket;
    BufferedReader input;
    DataOutputStream out;

    public Player(Cell symbol) {
        this.symbol = symbol;
        /*this.input = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
        this.output = new DataOutputStream(socket.getOutputStream());
    */}

    public Player(Socket socket) {
        this.socket = socket;
    }

    public Player(Socket socket, Cell symbol) throws IOException {
        this.socket = socket;
        this.symbol = symbol;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new DataOutputStream(socket.getOutputStream());
    }


}
