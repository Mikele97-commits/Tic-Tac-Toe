package echoserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Player {
    String roomID;
    Cell symbol;
    Socket socket;
    BufferedReader input;
    DataOutputStream out;

    public Player(Cell symbol) {
        this.symbol = symbol;
        }

    public Player(Socket socket) {
        this.socket = socket;
    }


    public Cell getSymbol() {
        return symbol;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public Player(Socket socket, Cell symbol) throws IOException {
        this.socket = socket;
        this.symbol = symbol;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    public Player(Socket socket, Cell symbol, String roomID) throws IOException {
        this.socket = socket;
        this.symbol = symbol;
        this.roomID = roomID;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new DataOutputStream(socket.getOutputStream());
    }


}
