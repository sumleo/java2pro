package Utils;


import java.io.*;
import java.nio.charset.Charset;

public class CSVinitial {
    private File file;

    public CSVinitial(String path) {
        this.file = new File(path);
    }

    public void initial() {
        try {
            InputStream inputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
