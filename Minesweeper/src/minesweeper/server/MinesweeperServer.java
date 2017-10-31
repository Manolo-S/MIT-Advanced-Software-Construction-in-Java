/* Copyright (c) 2007-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Multiplayer Minesweeper server.
 */
public class MinesweeperServer {

  private static final int DEFAULT_PORT = 4444;
  private static final int MAXIMUM_PORT = 65535;
  static Board board;
  private static ExecutorService cachedThreadPool;
  private final int port;

  /**
   * Default square board size.
   */
  static final int DEFAULT_SIZE = 10;

  /**
   * True if the server should *not* disconnect a client after a BOOM message.
   */
  static boolean debug;

  // Representation invariant
  //  port > 0 && port <= 65535
  //  cachedThreadPool is not null
  //  board is not null

  // Abstraction function
  //  Represents a multi player Minesweeper server

  // Safety from representation exposure
  //  DEFAULT_PORT, MAXIMUM_PORT, port and DEFAULT_SIZE are private final and immutable;
  //  board and cachedThreadPool are mutable data types and the reference is also mutable
  //  board is package private and static as it has to be accessible to all clients (ClientHandler class) concurrently
  //  board is only mutated by methods on the Board class itself
  //  cachedThreadPool is a private variable and is only altered by the ExecutorService as clients connect and disconnect
  //  debug is an immutable data type but the reference is mutable and package private

  // Thread safety argument
  //  The Minesweeper server uses a single cachedThreadPool (from the Executors interface).
  //  Every client runs in a separate thread of the cached thread pool and they all access the same
  //  threadsafe board (from the Board class). There is no communication between the threads.

  /**
   * Checks if the representation invariants hold.
   */
  private void checkRep() {
    assert port >= 0 && port <= MAXIMUM_PORT : "Port " + port + " out of range.";
    assert cachedThreadPool != null : "cachedThreadPool should not be null.";
    assert board != null : "board should not be null.";
  }


  /**
   * Make a MinesweeperServer that listens for connections on port.
   *
   * @param port port number, requires 0 <= port <= 65535
   * @param debug debug mode flag
   * @throws IOException if an error occurs opening the server socket
   */
  public MinesweeperServer(int port, boolean debug) {
    assert port >= 0 && port <= MAXIMUM_PORT : "port " + port + " out of range";
    MinesweeperServer.debug = debug;
    this.port = port;
    cachedThreadPool = Executors.newCachedThreadPool();
    checkRep();
  }

  /**
   * Start a MinesweeperServer running on the specified port, with either a random new board or a
   * board loaded from a file.
   *
   * @param debug The server will disconnect a client after a BOOM message if and only if debug is
   * false.
   * @param file If file.isPresent(), start with a board loaded from the specified file, according
   * to the input file format defined in the documentation for main(..).
   * @param sizeX If (!file.isPresent()), start with a random board with width sizeX (and require
   * sizeX > 0).
   * @param sizeY If (!file.isPresent()), start with a random board with height sizeY (and require
   * sizeY > 0).
   * @param port The network port on which the server should listen, requires 0 <= port <= 65535.
   */
  public static void runMinesweeperServer(boolean debug, Optional<File> file, int sizeX, int sizeY,
      int port) {
    if (!file.isPresent() && (sizeX <= 0 || sizeY <= 0)) {
      throw new IllegalArgumentException("Board size parameters invalid. ");
    }
    board = Board.createBoard(file, sizeX, sizeY);
    MinesweeperServer server = new MinesweeperServer(port, debug);
    server.serve();
  }


  /**
   * Run the server, listening for client connections and handling them.
   * Never returns unless an exception is thrown.
   *
   * @throws IOException if the main server socket is broken (IOExceptions from individual clients
   * do *not* terminate serve())
   */
  public void serve() {
    System.out.println("Minesweeper started.");
    try (ServerSocket serverSocket = new ServerSocket(port);) {
      while (true) {
        // block until a client connects
        Socket socket = serverSocket.accept();
        Runnable clientHandler = new ClientHandler(socket);
        cachedThreadPool.execute(clientHandler);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return;
    } finally {
      if (cachedThreadPool != null) {
        cachedThreadPool.shutdown();
      }
    }
  }

  /**
   * Returns approximate number of threads.
   */
  public static int getNumberOfThreads() {
    return ((ThreadPoolExecutor) cachedThreadPool).getActiveCount();
  }

  /**
   * Start a MinesweeperServer using the given arguments.
   *
   * <br> Usage: MinesweeperServer [--debug | --no-debug] [--port PORT] [--size SIZE_X,SIZE_Y |
   * --file FILE]
   *
   * <br> The --debug argument means the server should run in debug mode. The server should
   * disconnect a client after a BOOM message if and only if the --debug flag was NOT given. Using
   * --no-debug is the same as using no flag at all. <br> E.g. "MinesweeperServer --debug" starts
   * the server in debug mode.
   *
   * <br> PORT is an optional integer in the range 0 to 65535 inclusive, specifying the port the
   * server should be listening on for incoming connections. <br> E.g. "MinesweeperServer --port
   * 1234" starts the server listening on port 1234.
   *
   * <br> SIZE_X and SIZE_Y are optional positive integer arguments, specifying that a random board
   * of size SIZE_X*SIZE_Y should be generated. <br> E.g. "MinesweeperServer --size 42,58" starts
   * the server initialized with a random board of size 42*58.
   *
   * <br> FILE is an optional argument specifying a file pathname where a board has been stored. If
   * this argument is given, the stored board should be loaded as the starting board. <br> E.g.
   * "MinesweeperServer --file boardfile.txt" starts the server initialized with the board stored in
   * boardfile.txt.
   *
   * <br> The board file format, for use with the "--file" option, is specified by the following
   * grammar:
   * <pre>
   *   FILE ::= BOARD LINE+
   *   BOARD ::= X SPACE Y NEWLINE
   *   LINE ::= (VAL SPACE)* VAL NEWLINE
   *   VAL ::= 0 | 1
   *   X ::= INT
   *   Y ::= INT
   *   SPACE ::= " "
   *   NEWLINE ::= "\n" | "\r" "\n"?
   *   INT ::= [0-9]+
   * </pre>
   *
   * <br> If neither --file nor --size is given, generate a random board of size 10x10.
   *
   * <br> Note that --file and --size may not be specified simultaneously.
   *
   * @param args arguments as described
   */
  public static void main(String[] args) {
    // Command-line argument parsing is provided. Do not change this method.
    boolean debug = false;
    int port = DEFAULT_PORT;
    int sizeX = DEFAULT_SIZE;
    int sizeY = DEFAULT_SIZE;
    Optional<File> file = Optional.empty();

    Queue<String> arguments = new LinkedList<>(Arrays.asList(args));
    try {
      while (!arguments.isEmpty()) {
        String flag = arguments.remove();
        try {
          if (flag.equals("--debug")) {
            debug = true;
          } else if (flag.equals("--no-debug")) {
            debug = false;
          } else if (flag.equals("--port")) {
            port = Integer.parseInt(arguments.remove());
            if (port < 0 || port > MAXIMUM_PORT) {
              throw new IllegalArgumentException("port " + port + " out of range");
            }
          } else if (flag.equals("--size")) {
            String[] sizes = arguments.remove().split(",");
            sizeX = Integer.parseInt(sizes[0]);
            sizeY = Integer.parseInt(sizes[1]);
            file = Optional.empty();
          } else if (flag.equals("--file")) {
            sizeX = -1;
            sizeY = -1;
            file = Optional.of(new File(arguments.remove()));
            if (!file.get().isFile()) {
              throw new IllegalArgumentException("file not found: \"" + file.get() + "\"");
            }
          } else {
            throw new IllegalArgumentException("unknown option: \"" + flag + "\"");
          }
        } catch (NoSuchElementException nsee) {
          throw new IllegalArgumentException("missing argument for " + flag);
        } catch (NumberFormatException nfe) {
          throw new IllegalArgumentException("unable to parse number for " + flag);
        }
      }
    } catch (IllegalArgumentException iae) {
      System.err.println(iae.getMessage());
      System.err.println(
          "usage: MinesweeperServer [--debug | --no-debug] [--port PORT] [--size SIZE_X,SIZE_Y | --file FILE]");
      return;
    }
    runMinesweeperServer(debug, file, sizeX, sizeY, port);
  }
}

