# Multiplayer Tic-Tac-Toe — Java Socket Server

A multithreaded, two-player networked Tic-Tac-Toe game built in Java utilizing raw sockets. Players connect to the host, authenticate securely, and play against one another in real time through an interactive terminal interface.

The entire environment is containerized for seamless deployment.

---

## Features

- **Multi-Container Orchestration:** Spin up the entire infrastructure with a single command.
- **Two-Player Gameplay:** Real-time matches over TCP socket connections.
- **Dynamic Boards:** Configurable grid sizes (set dynamically by Player X at game start).
- **Secure Authentication:** User registration and login protected via **BCrypt password hashing**.
- **Persistent Data Volume:** Player score tracking using a flat-file properties database mapped securely to host storage.
- **Rematch Engine:** Automatic round resets with symbol swaps between rounds.

---

## Multi-Room Support

The server effortlessly handles multiple concurrent matches. Each pair of connecting users is dynamically routed to their own isolated game room with a unique UUID tracked in the `Lobby` manager. Each room operates on its own dedicated threads, preventing any crosstalk or performance bottlenecks between different active games.

---

## Architecture Blueprint

- **EchoServer** — Listens for incoming socket traffic and handles initialization.
- **Lobby** — Pairs incoming client connections into match instances.
- **EchoServerThread** — A runnable execution context dedicated to each individual player session.
- **Game** — Thread-safe, synchronized data model governing match mechanics.
- **Database** — Encapsulated entry point utilizing `java.util.Properties` to manage account data.
- **Client** — Interactive terminal UI that connects players to the remote container infrastructure.

---

## Setup & Running with Docker

The easiest way to build and experience the ecosystem is using **Docker Compose**.

### Prerequisites

Ensure you have [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed on your machine.

### Execution Steps

1. **Build the Fat JAR Artifact:**
   Compile your project locally using Maven to update code definitions:
   ```bash
   mvn clean package
   
2. **Launch the Server Stack**
   Bring up the network and the game server in detached mode:
   ```bash
   docker compose up -d game-server
   
3. **Connect Player 1:**
   In a separate terminal window, launch and attach to the first player instance:
   ```bash
   docker compose run player-1

4. **Connect Player 2:**
   In a third terminal window, launch and attach to the second player instance:
   ```bash
   docker compose run player-2
   
5. **Wipe Environment:**
   When finished playing, spin down the active containers and safely tear down the virtual network switches by running:
   ```bash
   docker compose down
___

## Gameplay Mechanics

- **Grid Selection:** Coordinates are passed via standard string inputs like `A1`, `B2`, or `C3`.
- **Match Rewards, visible during login:**
   - **Win:** +3 points
   - **Loss:** -1 point

---

## Database Management

User profiles and encrypted records are stored in a `database.db` file structure. In development and runtime, this directory is dynamically linked out of the ephemeral container context into a persistent volume, ensuring player records and scores survive container restarts.


