import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileSplitter {

    int count = 1;

    public int getCount() {
        return count;
    }


    public void join(String tempPath, String currentPath, int parts) {
        int data;
        try {
            File filename;
            File newFilename = new File(currentPath);
            OutputStream outfile = new BufferedOutputStream(new FileOutputStream(newFilename));

            for (int part = 1; part <= parts; part++) {
                filename = new File(tempPath + part + ".sp");
                if (filename.exists()) {
                    InputStream infile = new BufferedInputStream(new FileInputStream(filename));
                    data = infile.read();
                    while (data != -1) {
                        outfile.write(data);
                        data = infile.read();
                    }
                    infile.close();
                } else {
                    break;
                }
            }
            outfile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void split(String FilePath, long splitLength) {
        long leng = 0;
        int data;
        count = 1;
        try {
            File filename = new File(FilePath);
            InputStream infile = new BufferedInputStream(new FileInputStream(filename));
            data = infile.read();
            while (data != -1) {
                filename = new File(FilePath + count + ".sp");
                OutputStream outfile = new BufferedOutputStream(new FileOutputStream(filename));
                while (data != -1 && leng < splitLength) {
                    outfile.write(data);
                    leng++;
                    data = infile.read();
                }
                leng = 0;
                outfile.close();
                count++;
            }
            infile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeTemp(String tempPath, int part) {
        for (; part >= 1; part--) {
            try {
                Files.delete(Paths.get(tempPath + part + ".sp"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
