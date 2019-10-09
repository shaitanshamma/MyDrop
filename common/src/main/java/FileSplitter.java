import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileSplitter {

    public int getCount() {
        return count;
    }

    int count = 1;
    String newFilePath;
    String tempPath;

    public void join(String tempPath, String currentPath, int parts) throws IOException {
        //int part =1;
        long leninfile = 0, leng = 0;
        int data = 0;
        try {
            File filename = new File(tempPath);
            File newFilename = new File(currentPath);
            //RandomAccessFile outfile = new RandomAccessFile(filename,"rw");

            OutputStream outfile = new BufferedOutputStream(new FileOutputStream(newFilename));
//            while (true)
            for (int part = 1; part <= parts; part++) {
                filename = new File(tempPath + part + ".sp");
                if (filename.exists()) {
                    //RandomAccessFile infile = new RandomAccessFile(filename,"r");
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
            //RandomAccessFile infile = new RandomAccessFile(filename, "r");
            InputStream infile = new BufferedInputStream(new FileInputStream(filename));
            data = infile.read();
            while (data != -1) {
                filename = new File(FilePath + count + ".sp");
                //RandomAccessFile outfile = new RandomAccessFile(filename, "rw");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        // count = 1;
    }

    public void removeTemp(String tempPath, int part) throws IOException {
        for (; part >= 1; part--) {
            Files.delete(Paths.get(tempPath + part + ".sp"));
        }
    }

    public static void main(String[] args) throws Exception {
//        RandomAccessFile raf = new RandomAccessFile("client_storage/1.txt", "r");
//        long numSplits = 10; //from user input, extract it from args
//        long sourceSize = raf.length();
//        long bytesPerSplit = sourceSize/numSplits ;
//        long remainingBytes = sourceSize % numSplits;
//
//        int maxReadBufferSize = 8 * 1024; //8KB
//        for(int destIx=1; destIx <= numSplits; destIx++) {
//            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream("client_storage/split."+destIx));
//            if(bytesPerSplit > maxReadBufferSize) {
//                long numReads = bytesPerSplit/maxReadBufferSize;
//                long numRemainingRead = bytesPerSplit % maxReadBufferSize;
//                for(int i=0; i<numReads; i++) {
//                    readWrite(raf, bw, maxReadBufferSize);
//                }
//                if(numRemainingRead > 0) {
//                    readWrite(raf, bw, numRemainingRead);
//                }
//            }else {
//                readWrite(raf, bw, bytesPerSplit);
//            }
//            bw.close();
//        }
//        if(remainingBytes > 0) {
//            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream("client_storage/split."+(numSplits+1)));
//            readWrite(raf, bw, remainingBytes);
//            bw.close();
//        }
//        raf.close();
//    }
//
//    static void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException {
//        byte[] buf = new byte[(int) numBytes];
//        int val = raf.read(buf);
//        if(val != -1) {
//            bw.write(buf);
//        }
////    }
//        FileSplitter fl = new FileSplitter();
////        fl.split("client_storage/1.txt",26);
//        fl.join("client_storage/1.txt");
    }
}
