package echoserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Player {
    Cell symbol;
    Socket socket;
    BufferedReader input;
    DataOutputStream output;

    public Player(Cell symbol) {
        this.symbol = symbol;
        /*this.input = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
        this.output = new DataOutputStream(socket.getOutputStream());
    */}
}
