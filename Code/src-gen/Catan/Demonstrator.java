package Catan;

import java.util.List;

public class Demonstrator {
    public static void main(String[] args) throws Exception {
        Config cfg = Config.load("config.txt");

        Board board = new Board();
        List<Player> players = List.of(
                new Player(1),
                new Player(2),
                new Player(3),
                new Player(4)
        );

        Game game = new Game(cfg.turns, board, players);
        game.run();
    }
}