# Multiplayer Tic-Tac-Toe — Java Socket Server

A two-player networked Tic-Tac-Toe game built in Java using raw sockets. Players connect to a server, authenticate, and play against each other in real time through a terminal interface.

---

## Features

- Two-player real-time gameplay over TCP sockets
- Configurable board size (set by Player X at game start)
- User registration and login with **BCrypt password hashing**
- Persistent player scoring via a properties-based database
- Rematch system with automatic symbol swap between rounds
- Tie detection and win detection in all directions (rows, columns, diagonals)

---

## Multi-Room Support

The server handles multiple concurrent games simultaneously. Each pair of connecting players is automatically assigned to their own isolated game room with a unique UUID. Rooms are tracked in the `Lobby` via a `HashMap`, and each room runs entirely in its own pair of threads — meaning multiple games can be in progress at the same time without interfering with each other.

---

## Architecture

The project follows a classic multi-threaded server model:

- **EchoServer** — listens on port 6666, accepts incoming connections
- **Lobby** — matches connecting players into game rooms
- **EchoServerThread** — one thread per player, handles the full session lifecycle (auth → game → rematch)
- **Game** — shared game state between two threads, synchronized for thread safety
- **Client** — terminal-based client, sends input and prints server responses

---

## How to Run

### Requirements

- Java 17+
- Two external JARs (no Maven required — add manually to classpath):
  - `bcrypt-0.10.2.jar` by [favre.io / at.favre.lib](https://github.com/patrickfav/bcrypt)
  - `bytes-1.5.0.jar` by [favre.io / at.favre.lib](https://github.com/patrickfav/bytes-java) *(dependency of bcrypt)*

### Steps

1. Start the server by running `EchoServer.main()`
2. Start two separate `Client.main()` instances (two terminal windows)
3. Each client registers or logs in
4. Player X sets the board size, then the game begins

---

## Gameplay

- Moves are entered as column letter + row number, e.g. `A1`, `B2`, `C3`
- Three in a row in any direction wins
- After a game ends, both players are asked if they want a rematch
- If both agree, symbols swap and a new game starts
- Points are awarded as follows:
  - **Win:** +3 points
  - **Loss:** -1 point

---

## Database

User data is stored in a `database.db` file using Java's `Properties` format. Each entry stores a BCrypt-hashed password and the player's point total. No SQL or external database engine is required.

---

## Notes

*This README was written by Claude (Anthropic) at the author's request.*