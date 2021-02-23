package editor.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader {

    private final String path;

    public FileReader(String path) {
        this.path = path;
    }

    public String readFile() throws FileNotFoundException {

        Scanner reader = new Scanner(new File(path));

        String res = "";
        while(reader.hasNextLine())
            res = res + reader.nextLine() + "\n";

        reader.close();

        return res;
    }
}
