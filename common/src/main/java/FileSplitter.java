import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileSplitter {

    public int getCount() {
        return count;
    }

    int count = 1;

    public void join(String tempPath, String currentPath, int parts) throws IOException {
        long leninfile = 0, leng = 0;
        int data = 0;
        try {
            File filename = new File(tempPath);
            File newFilename = new File(currentPath);
            OutputStream outfile = new BufferedOutputStream(new FileOutputStream(newFilename));
//            while (true)
            for (int part = 1; part <= parts; part++) {
                filename = new File(tempPath + part + ".sp");
                if (filename.exists()) {
                    InputStream infile = new BufferedInputStream(new FileInputStream(filename));
                    data = infile.read();
                    while (data != -1) {
                        outfile.write(data);
                        data = infile.read();
                    }
                    leng++;
                    infile.close();
                    // part++;
                } else {
                    break;
                }
            }
            outfile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void split(String FilePath, long splitlen) throws IOException {
        long leninfile = 0, leng = 0;
        int data;
        count = 1;
        try {
            File filename = new File(FilePath);
            InputStream infile = new BufferedInputStream(new FileInputStream(filename));
            data = infile.read();
            while (data != -1) {
                filename = new File(FilePath + count + ".sp");
                OutputStream outfile = new BufferedOutputStream(new FileOutputStream(filename));
                while (data != -1 && leng < splitlen) {
                    outfile.write(data);
                    leng++;
                    data = infile.read();
                }
                leninfile += leng;
                leng = 0;
                outfile.close();
                count++;
            }
                infile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeTemp(String tempPath, int part) throws IOException {
        for (; part >= 1; part--) {
            Files.delete(Paths.get(tempPath + part + ".sp"));
        }
    }
}
