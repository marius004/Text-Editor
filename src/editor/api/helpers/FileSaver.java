package editor.api.helpers;

import java.io.FileWriter;

public class FileSaver {

    private String path;
    private String content;

    public FileSaver(String path, String content) {
        this.path    = path;
        this.content = content;
    }

    public void save() throws Exception {
        FileWriter writer = new FileWriter(path);
        writer.write(this.content);
        writer.close();
    }
}
