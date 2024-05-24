package ptui;
import javafx.scene.image.Image;
import model.RITQTNode;

import java.util.*;
import java.io.*;

public class RITUncompress {
    private RITQTNode quadTree;

    public void setQuadTree(int size, ArrayList<Integer> data) throws IndexOutOfBoundsException, Error {
        this.quadTree = RITQTNode.fromCompressed(size, data);
        if(data.size() > 0)
            throw new Error("Data should be emptied out if a file is correctly uncompressed");
    }

    public void setQuadTree(String path) throws FileNotFoundException {
        File file = new File(path);
        ArrayList<Integer> data = new ArrayList<>();
        Scanner fileReader = new Scanner(file);
        int size = fileReader.nextInt();
        while(fileReader.hasNextInt()){
            int num = fileReader.nextInt();
            data.add(num);
        }

        setQuadTree(size, data);
    }

    /**
     * Uncompresses the data in quadTree and stores it into a file under the directory argument's name
     * @param directory
     */
    public void run(String directory) throws IOException {
        File file = new File(directory);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        ArrayList<Integer> data = quadTree.toUncompressedList();
        StringBuilder s = new StringBuilder();
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
     * This is the main function that reads the data from the file given and runs the uncompress function
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java RITUncompress compressed.rit uncompressed.txt");
            return;
        }
        else {

            try {
                String path = args[0];
                String fileName = path.split("/")[2];
                RITUncompress uncp = new RITUncompress();
                uncp.setQuadTree(path);
                String newPath = args[1];

                System.out.println("Uncompressing: " + fileName);
                System.out.println("QTree: " + uncp.getQuadTree());
                uncp.run(newPath);
                System.out.println("Output file: "+newPath);
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
            catch(Error | IndexOutOfBoundsException e){
                System.out.println("Invalid format. \n" + args[0] + " is either already uncompressed or an incorrectly formatted file.");
                System.exit(0);
            }
        }
    }
}