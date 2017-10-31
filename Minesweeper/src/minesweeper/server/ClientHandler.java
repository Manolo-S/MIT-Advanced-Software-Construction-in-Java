package minesweeper.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Each instance of ClientHandler runs in a separate thread of a Minesweeper server.
 * It accepts commands from a Minesweeper client, processes them
 * and sends back a message to the client.
 */

public class ClientHandler implements Runnable {

  private final Socket socket;

  public ClientHandler(Socket socket) {
    assert socket != null : "socket should not be null";
    this.socket = socket;
  }

  /**
   * Sets up an input/output stream with a client.
   * Reads client commands, handles them and sends back messages.
   */
  @Override
  public void run() {
    String output;
    try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    ) {
      out.println("Welcome to Minesweeper. Players: " + MinesweeperServer.getNumberOfThreads() +
          " including you. Board: " + MinesweeperServer.board.getSquaresArray()[0].length +
          " columns by " + MinesweeperServer.board.getSquaresArray().length + " rows." +
          " Type 'help' for help.");
      for (String line = in.readLine(); line != null; line = in.readLine()) {
        output = handleRequest(line);
        if (output.equals("client left game")) {
          out.println("Bye now!");
          return;
        } else if (output.equals("BOOM") && !MinesweeperServer.debug) {
          out.println("BOOM!!");
          return;
        } else if (output.equals("BOOM") && MinesweeperServer.debug) {
          out.println("BOOM!!");
        } else {
          out.println(output);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
  }


  /**
   * Handler for client commands. Executes them if valid and returns the resulting message.
   *
   * @param input client to server command
   * @return server to client message
   */
  private String handleRequest(String input) {
    assert input != null : "input should not be null";
    String regex = "(look)|(help)|(bye)|"
        + "(dig -?\\d+ -?\\d+)|(flag -?\\d+ -?\\d+)|(deflag -?\\d+ -?\\d+)";
    if (!input.matches(regex)) {
      return "Invalid command.";
    }
    String[] tokens = input.split(" ");
    if (tokens[0].equals("look")) {
      return MinesweeperServer.board.look();
    } else if (tokens[0].equals("help")) {
      return "Read the manual.";
    } else if (tokens[0].equals("bye")) {
      return "client left game";
    } else {
      int x = Integer.parseInt(tokens[1]);
      int y = Integer.parseInt(tokens[2]);
      if (tokens[0].equals("dig")) {
        return MinesweeperServer.board.dig(x, y);
      } else if (tokens[0].equals("flag")) {
        return MinesweeperServer.board.flag(x, y);
      } else if (tokens[0].equals("deflag")) {
        return MinesweeperServer.board.deflag(x, y);
      }
    }
    throw new UnsupportedOperationException();
  }
}
