package ptui;

import model.RITQTNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class RITCompress {
    private RITQTNode quadTree;
    private ArrayList<Integer> rawData;
    private ArrayList<Integer> comData;

    /**
     * Converts the data into a quadTree
     * @param data
     */
    public void setRawData(ArrayList<Integer> data) {
        rawData = data;
        quadTree = RITQTNode.fromUncompressed(data);
        comData = quadTree.toCompressedList();
    }

    /**
     * Reads a file to get the data and then converts it into the quadtree
     * @param directory
     * @throws FileNotFoundException
     */
    public void setRawData(String directory) throws FileNotFoundException {
        File file = new File(directory);
        ArrayList<Integer> data = new ArrayList<>();
        Scanner fileReader = new Scanner(file);
        while(fileReader.hasNextInt()){
            int num = fileReader.nextInt();
            data.add(num);
        }
        setRawData(data);
    }

    /**
     * Returns an arraylist representing the compiled quadTree
     * @return ArrayList<Integer>  ArrayList representing the quadtree
     */
    public ArrayList<Integer> getComData() {
        return comData;
    }

    /**
     * Returns an arraylist representing the uncompiled quadTree
     * @return ArrayList<Integer>  ArrayList representing the quadtree
     */
    public ArrayList<Integer> getRawData() {
        return rawData;
    }

    /**
     * Uncompresses the data in quadTree and stores it into a file under the directory argument's name
     * @param directory
     */
    public void run(String directory) throws IOException {
        File file = new File(directory);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        ArrayList<Integer> data = comData;
        StringBuilder s = new StringBuilder();
        s.append(rawData.size()).append("\n");
        for(int d: data){
            s.append(d).append("\n");
        }
        writer.write(s.toString());
        writer.flush();
        writer.close();
    }

    /**
     * Returns the quadTree instance variable
     * @return RITQTNode
     */
    public RITQTNode getQuadTree() {
        return quadTree;
    }

    /**
     * Main method that takes in args and compresses a file and prints out stats about the compression
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java RITCompress uncompressed-file.txt compressed-file.rit");
            return;
        }
        else {
            try {
                String path = args[0];
                String fileName = path.split("/")[2];
                RITCompress cp = new RITCompress();
                cp.setRawData(path);
                String newPath = args[1];
                System.out.println("Compressing: " + fileName);
                cp.run(newPath);
                System.out.println("QTree: " + cp.getQuadTree());
                int rawSize = cp.getRawData().size();
                int comSize = cp.getComData().size()+1;
                System.out.println("Output file: "+newPath);
                StringBuilder s = new StringBuilder();
                s.append("Raw image size:").append(rawSize).append("\n").append("Compressed image size:").append(comSize).append("\n").append("Compression %:").append((1 - (double)comSize/rawSize) * 100);
                System.out.println(s.toString());
            } catch (FileNotFoundException e) {
                if(e.toString().replaceAll("\\\\","/").indexOf(args[1]) > 0 && !args[0].equals(args[1]))
                    System.out.println(args[1] + " is not a valid directory for file output. File can not be created.");
                else
                    System.out.println(args[0] + " is not a valid path. File can not be found.");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(args[1] + " can not be created.");
                System.exit(0);
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Invalid format. \n" + args[0] + " is either already compressed or an incorrectly formatted file.");
                System.exit(0);
            }

        }
    }
}
