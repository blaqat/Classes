package gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;
import java.io.*;

public class RITViewer extends Application {
    int imageSize;
    int[][] rawImageData;

    /**
     * Reads the file inputted into a 2d array
     * @param dir File path
     * @throws FileNotFoundException
     * @throws ArrayIndexOutOfBoundsException when imagesize is not a square
     */
    public void setImage(String dir) throws FileNotFoundException, ArrayIndexOutOfBoundsException {
        ArrayList<Integer> raw = new ArrayList<>();
        File file = new File(dir);
        Scanner fr = new Scanner(file);

        while(fr.hasNextInt()){
            int num = fr.nextInt();
            raw.add(num);

            if(num>255 || num < 0){
                throw new IndexOutOfBoundsException("Out of range");
            }
        }

        int size = raw.size();
        imageSize = (int)Math.sqrt(size);
        rawImageData = new int[imageSize][imageSize];
        for(int i = 0; i < size; i++){
            rawImageData[i/imageSize][i%imageSize] = raw.get(i);
        }
    }

    /**
     * The function ran at the start of the application that reads a file for numbers in 0-255 rrange and stores them
     * into a 2d-array that will be loaded as the image later
     */
    @Override
    public void init() {
        // get the command line args
        List<String> args = getParameters().getRaw();
        try {
            setImage(args.get(0));
        } catch (FileNotFoundException e) {
            System.out.println(args.get(0)+" is not a valid directory.");
            System.exit(0);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("The image must be a square.");
            System.exit(0);
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("All numbers must be in the range 0-255");
            System.exit(0);
        }
    }

    /**
     * Creates the GUI of the image
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        Group main = new Group();
        Canvas canvas = new Canvas();
        drawImageToCanvas(canvas);
        main.getChildren().add(canvas);
        Scene scene = new Scene(main);
        stage.setTitle("RITViewer");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Launches the application
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java RITViewer uncompressed.txt");
            return;
        }
        Application.launch(args);
    }

    /**
     * Draws the data from the 2d-array rawImageData into a Canvas.
     * @param c
     */
    public void drawImageToCanvas(Canvas c){
        c.setWidth(imageSize);
        c.setHeight(imageSize);
        GraphicsContext gc = c.getGraphicsContext2D();
        for(int x = 0; x < imageSize; ++x){
            for(int y = 0; y < imageSize; ++y){
                int greyC = rawImageData[x][y];
                gc.setFill(Color.rgb(greyC, greyC, greyC));
                gc.fillRect(y, x, 1, 1);
            }
        }
    }
}
