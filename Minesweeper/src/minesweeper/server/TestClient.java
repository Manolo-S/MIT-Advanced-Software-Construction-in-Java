package minesweeper.server;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class TestClient {

  static void p(Object s) {
    System.out.println(s);
  }

  public static void main(String[] args) throws IOException {
Optional<File> file = Optional.empty();
//MinesweeperServer.runMinesweeperServer(false, file, 10,10, 4444);
//    String[] options = {"--debug"};
//    String[] options = {"--port", "1234"};
//    String[] options = {"--file", ".\\src\\minesweeper\\server\\board_4_by_3_1_bomb_2_1.txt"};
    String[] options = {"--port", "1234", "--size", "20,14"};

    MinesweeperServer.main(options);


  }
}
