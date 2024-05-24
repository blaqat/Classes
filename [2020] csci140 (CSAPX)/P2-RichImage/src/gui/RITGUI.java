package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ptui.RITCompress;
import ptui.RITUncompress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class RITGUI extends Application {
    //Used for making buttons that open up the file explorer prompt
    private static class DirButton extends BorderPane {
        private final TextField textBox;
        private String path;
        private String fileName;
        private final FileChooser fileChooser;

        /**
         * Returns the name of the file thats currently selected
         * @return The fileName with out the path included
         */
        public String getFileName() {
            return fileName;
        }

        /**
         * Returns the path of the file that's currently selected
         * @return directory
         */
        public String getPath() {
            return path;
        }

        /**
         * Sets the textBox and fileName equal to the path inputted
         * @param path
         */
        public void setPath(String path) {
            this.path = path;
            textBox.setText(path);
            File file = new File(path);
            fileName = file.getName();
        }

        /**
         * Sets the directory the FileExplorer will open up to
         * @param path
         */
        public void setDirectory(String path) {
            File file = new File(path);
            fileChooser.setInitialDirectory(file.getParentFile());
            setPath(file.getAbsolutePath());
        }

        /**
         * Sets the directory to the source directory.
         * @throws IOException
         */
        public void resetDirectory() throws IOException {
            File s = new File(".");
            fileChooser.setInitialDirectory(s.getCanonicalFile());
            setPath(s.getCanonicalPath());
        }

        /**
         * @param save This determines of the button will open the save menu or the open menu in the file explorer
         */
        public DirButton(Stage stage, String text, boolean save) throws IOException {
            this.fileChooser = new FileChooser();
            this.textBox = new TextField();
            this.textBox.setMinWidth(400);
            Button button = new Button(text);
            //this.getChildren().add(button);
            this.setLeft(button);
            this.setCenter(textBox);
            resetDirectory();
            /**
             * When the button is clicked the filexplorer will be opened
             */
            button.setOnAction(action -> {
                File file;
                if(save) file = fileChooser.showSaveDialog(stage);
                else file = fileChooser.showOpenDialog(stage);
                if(file!=null)
                    this.setDirectory(file.getAbsolutePath());
            });

            /**
             * When enter is pressed when typing in the textfield, the directory will be changed.
             */
            textBox.setOnAction(action -> {
                setDirectory(textBox.getText());
            });
        }
    }
    //Used for the Horizontal closable menu
    private static class DropDownButton extends FlowPane {
        public HBox frame;
        public Button button;
        public DropDownButton(String defaultText){
            frame = new HBox();
            button = new Button(defaultText);
            frame.setSpacing(1);
            frame.setVisible(false);
            button.setOnAction(action -> {
                frame.setVisible(!frame.isVisible());
                if(frame.isVisible()){
                    button.setText("x");
                }
                else{
                    button.setText(defaultText);
                }
            });
            this.getChildren().add(button);
            this.getChildren().add(frame);
        }

        /**
         * Adds buttons into the HBox
         * @param buttons An array of buttons
         */
        public void addButtons(Button[] buttons){
            for(Button b: buttons){
                addButton(b);
            }
        }

        /**
         * Adds a button into the HBox
         * @param b A Button
         */
        public void addButton(Button b){
            frame.getChildren().add(b);
        }
    }

    private DropDownButton menu;
    private TextArea output;
    private Stage stage;
    private Canvas image;
    private RITViewer imageViewer;
    private DirButton inputDir;
    private DirButton outputDir;
    private RITCompress compressor;
    private RITUncompress decompressor;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        BorderPane frame = new BorderPane();
        ScrollPane imageHolder = new ScrollPane();

        stage.setTitle("RITGUI");
        stage.setScene(new Scene(frame));

        inputDir = new DirButton(stage, "Input File", false);
        outputDir = new DirButton(stage, "Output File", true);
        menu.addButtons(createMenuButtons());
        output.setPrefHeight(150);
        output.setEditable(false);
        output.setWrapText(true);
        image.setHeight(300);

        VBox organizer = new VBox();
        organizer.setSpacing(3);
        organizer.getChildren().addAll(menu, inputDir, outputDir);

        imageHolder.setContent(image);
        frame.setTop(organizer);
        frame.setCenter(imageHolder);
        frame.setBottom(output);

        stage.sizeToScene();
        stage.show();
    }

    @Override
    public void init() throws Exception {
        menu = new DropDownButton("Operations");
        output = new TextArea();
        image = new Canvas();
        imageViewer = new RITViewer();
        compressor = new RITCompress();
        decompressor = new RITUncompress();
    }

    /**
     * Creates the buttons needed for the functionality of the gui
     * @return Array of buttons
     */
    private Button[] createMenuButtons(){
        String[] names = {"Compress", "Decompress", "View", "Clear", "Quit"};
        Button[] buttons = new Button[5];
        for(int i =0; i < 5; i++){
            buttons[i] = new Button(names[i]);
        }
        buttons[0].setOnAction(action -> {
            output.setText("");
            try {
                String path = inputDir.getPath();
                String newPath = outputDir.getPath();
                compressor.setRawData(path);
                String fileName = inputDir.getFileName();
                output.appendText("Compressing: " + fileName + "\n");
                compressor.run(newPath);
                output.appendText("QTree: " + compressor.getQuadTree());
                StringBuilder s = new StringBuilder();
                int rawSize = compressor.getRawData().size();
                int comSize = compressor.getComData().size()+1;
                s.append("\nRaw image size: ").append(rawSize).append("\n").append("Compressed image size: ").append(comSize).append("\n").append("Compression %:").append((1 - (double)comSize/rawSize) * 100);
                output.appendText(s.toString());
            } catch (FileNotFoundException e) {
                if(outputDir.getPath().equals(inputDir.getPath()) || e.toString().indexOf(outputDir.getPath()) <= 0)
                    output.appendText(inputDir.getPath() + " is not a valid path. File can not be found.");
                else
                    output.appendText("\n" + outputDir.getPath() + " is not a valid directory for file output. File can not be created.");
                //System.exit(0);
            } catch (IOException e) {
                output.appendText("\n" + outputDir.getPath() + " File can not be created.");
                //System.exit(0);
            }
            catch (ArrayIndexOutOfBoundsException e){
                output.appendText("Invalid format. \n" + inputDir.getPath() + " is either already compressed or an incorrectly formatted file.");
            }
        });
        buttons[1].setOnAction(action -> {
            output.setText("");
            try {
                String path = inputDir.getPath();
                String newPath = outputDir.getPath();
                decompressor.setQuadTree(path);
                String fileName = inputDir.getFileName();
                output.appendText("Uncompressing: " + fileName + "\n");
                decompressor.run(newPath);
                output.appendText("QTree: " + decompressor.getQuadTree());
                output.appendText( "\n" + "Output file: "+newPath);
            } catch (FileNotFoundException e) {
                if(outputDir.getPath().equals(inputDir.getPath()) || e.toString().indexOf(outputDir.getPath()) <= 0)
                    output.appendText(inputDir.getPath() + " is not a valid path. File can not be found.");
                else
                    output.appendText("\n" + outputDir.getPath() + " is not a valid directory for file output. File can not be created.");
                //System.exit(0);
            } catch (IOException e) {
                output.appendText("\n" + outputDir.getPath() + " File can not be created.");
                //System.exit(0);
            }
            catch(Error | IndexOutOfBoundsException e){
                output.appendText("Invalid format. \n" + inputDir.getPath() + " is either already uncompressed or an incorrectly formatted file.");
            }
        });
        buttons[2].setOnAction(action -> {
            output.setText("");
            try {
                String path = inputDir.getPath();
                imageViewer.setImage(path);
                updateImage();
                output.appendText("Viewing Image: " + inputDir.getFileName());
            } catch (FileNotFoundException e) {
                output.appendText(inputDir.getPath() + " is not a valid path. File can not be found.");
                //System.exit(0);
            }
            catch (ArrayIndexOutOfBoundsException e){
                output.appendText("Invalid Image. The image must be a square.");
            }
            catch (IndexOutOfBoundsException e){
                output.appendText("This is not a correctly formatted file. Every number must be in the range 0-255");
            }
        });
        buttons[3].setOnAction(action -> {
            image.setHeight(-1);
            updateImage();
            output.setText("");
            try {
                inputDir.resetDirectory();
                outputDir.resetDirectory();
            } catch (IOException e) {
                output.appendText("Could not reset the directories");
            }
        });
        buttons[4].setOnAction(action -> {
            stage.close();
            System.exit(0);
        });

        return buttons;
    }

    /**
     * Updates the image in the canvas to the one in imageViewer
     */
    private void updateImage(){
        image.getGraphicsContext2D().clearRect(0, 0, image.getWidth(), image.getHeight());
        if(image.getHeight() != -1)
            imageViewer.drawImageToCanvas(image);
        else{
            image.setHeight(0);
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
