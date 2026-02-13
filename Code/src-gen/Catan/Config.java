package Catan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public final class Config {
    public final int turns;

    private Config(int turns) {
        this.turns = turns;
    }

    public static Config load(String path) throws IOException {
        int turns = -1;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                // format: turns: int
                if (line.startsWith("turns:")) {
                    String value = line.substring("turns:".length()).trim();
                    turns = Integer.parseInt(value);
                }
            }
        }

        if (turns < 1 || turns > 8192) {
            throw new IllegalArgumentException("turns must be in [1, 8192]");
        }

        return new Config(turns);
    }
}