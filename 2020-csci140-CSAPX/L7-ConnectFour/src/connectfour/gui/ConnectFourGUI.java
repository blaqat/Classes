package connectfour.gui;

import connectfour.model.ConnectFourBoard;
import connectfour.model.Observer;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.EnumMap;

/**
 * A JavaFX GUI for the networked Connect Four game.
 *
 * @author RIT CS
 * @author YOUR NAME HERE
 */
public class ConnectFourGUI extends Application implements Observer<ConnectFourBoard> {
    private ConnectFourBoard board;
    private Label movesMade;
    private Label currentPlayer;
    private Label status;
    private SlotButton[][] buttons;
    private Stage stage;
    private GridPane gridPane;

    @Override
    public void init() {
        this.board = new ConnectFourBoard();
        this.board.addObserver(this);
    }

    /**
     * Prompts the user to either restart and just stare at the unusable window...
     */
    public void reset(){
        Button b = new Button("Yes");
        Alert alert = new Alert(Alert.AlertType.NONE, "" + board.getGameStatus() + ": would you like to restart?", ButtonType.YES, ButtonType.CLOSE);
        alert.setTitle("Game Over");
        alert.showAndWait().ifPresent(e -> {
            if(e == ButtonType.YES) {
                init();
                update(this.board);
            }
            else
                stage.close();
        });
    }

    /**
     * Construct the layout for the game.
     *
     * @param stage container (window) in which to render the GUI
     * @throws Exception if there is a problem
     */
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        // create the border pane that holds the grid and label
        BorderPane borderPane = new BorderPane();


        // get the grid pane from the helper method
        GridPane gridPane = makeGridPane();
        borderPane.setCenter(gridPane);

        // store the panes into the scene and display it
        FlowPane main = new FlowPane();
        main.getChildren().add(makeGridPane());
        main.getChildren().add(makeStatsPane());
        Scene scene = new Scene(main);
        stage.setTitle("ConnectFourGUI");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();

        //restart the stats
        update(board);
    }

    /**
     * Called by the model, model.ConnectFourBoard, whenever there is a state change
     * that needs to be updated by the GUI.
     *
     * @param connectFourBoard the board
     */
    @Override
    public void update(ConnectFourBoard board) {
        for(int x = 0; x <= 6; ++x){
            for(int y = 0; y <= 5; ++y){
                SlotButton button = buttons[x][y];
                ConnectFourBoard.Player playerAtSpot = board.getContents(y,x);
                if(button.getState()!=playerAtSpot) {
                    if(button.getState() == ConnectFourBoard.Player.NONE)
                        new GhostDisk(button, y, x, playerAtSpot).place();
                    else
                        button.setState(playerAtSpot);
                }
            }
        }
        movesMade.setText("Moves made: " + board.getMovesMade());
        currentPlayer.setText("Current player: " + board.getCurrentPlayer());
        status.setText("Status: " + board.getGameStatus());
    }

    /**
     * The main method expects the host and port.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Makes the grid of buttons
     * @return
     */
    private GridPane makeGridPane(){
        gridPane = new GridPane();
        buttons = new SlotButton[7][6];
        // build remaining rows with row numbers and column values
        for(int x = 0; x <= 6; ++x){
            for(int y = 0; y <= 5; ++y){
                SlotButton button = new SlotButton(this.board.getContents(y, x));
                int finalX = x;
                /**
                 * Button on click method that palces ghost disk and updates board
                 */
                button.setOnAction(action -> {
                    if(board.getGameStatus() == ConnectFourBoard.Status.NOT_OVER && this.board.isValidMove(finalX)) {
                        board.makeMove(finalX);
                    }
                    if(board.getGameStatus()!=ConnectFourBoard.Status.NOT_OVER)
                        reset();
                });
                /**
                 * Button on mouse hovering method that calls the button's SetHover method
                 */
                button.setOnMouseEntered(e -> {
                    //button.setHover(board.getCurrentPlayer());

                    int pr = board.getPlacementRow(finalX);
                    SlotButton b = buttons[finalX][pr==-1?5:pr];
                    b.setHover(board.getCurrentPlayer());
                    button.setOnMouseExited(event -> {
                        b.setState(b.getState());
                    });

                });


                buttons[x][y] = button;
                gridPane.add(button, x, y);
            }
        }
        return gridPane;
    }

    /**
     * makes the list of stats
     * @return
     */
    private FlowPane makeStatsPane(){
        FlowPane stats = new FlowPane();
        movesMade = new Label("X Moves Made");
        currentPlayer = new Label("Current Player: PX");
        status = new Label("Status: XXX_XXXX");

        stats.getChildren().add(movesMade);
        stats.getChildren().add(currentPlayer);
        stats.getChildren().add(status);

        stats.setHgap(50);
        return stats;
    }

    /**
     * The button that represents the disk with images
     */
    private class SlotButton extends Button {
        private Image empty = new Image(getClass().getResourceAsStream(
                "empty.png"));
        private Image p1 = new Image(getClass().getResourceAsStream(
                "p1black.png"));
        private Image p2 = new Image(getClass().getResourceAsStream(
                "p2red.png"));
        private Image p1H = new Image(getClass().getResourceAsStream(
                "p2highlight.png"));
        private Image p2H = new Image(getClass().getResourceAsStream(
                "p1highlight.png"));
        private ConnectFourBoard.Player state;

        public SlotButton(ConnectFourBoard.Player state) {
            this.setState(state);
            this.setBackground(new Background( new BackgroundFill(Color.WHITE, null, null)));
        }

        /**
         * Changes the button into the hover version showing a mouse is over it
         * @param state
         */
        public void setHover(ConnectFourBoard.Player state){
            Image image = switch (state) {
                case P2 -> p2H;
                default -> p1H;
            };
            this.setGraphic(new ImageView(image));
        }

        public void setState(ConnectFourBoard.Player state){
            this.state = state;
            Image image = switch (state) {
                case NONE -> empty;
                case P1 -> p1;
                default -> p2;
            };
            this.setGraphic(new ImageView(image));
        }

        public ConnectFourBoard.Player getState() {
            return this.state;
        }


    }

    /**
     * The animated disk that slides into place and then deletes
     */
    private class GhostDisk extends Label {
        private Image p1 = new Image(getClass().getResourceAsStream(
                "p1ghost.png"));
        private Image p2 = new Image(getClass().getResourceAsStream(
                "p2ghost.png"));
        private int row;
        private int col;
        private ConnectFourBoard.Player state;
        private SlotButton parentButton;
        public GhostDisk(SlotButton button, int row, int col, ConnectFourBoard.Player state) {
            this.parentButton = button;
            this.row = row;
            this.col = col;
            this.setState(state);
        }
        public void setState(ConnectFourBoard.Player state){
            this.state = state;
            Image image = switch (state) {
                case P1 -> p1;
                default -> p2;
            };
            this.setGraphic(new ImageView(image));
            parentButton.setState(ConnectFourBoard.Player.NONE);
        }

        /**
         * Slides the ghost disk into place and then deletes it
         * @param grid
         */
        public void place(){
            Button button = buttons[col][row];
            double c = button.getLayoutX()+7;
            double r = button.getLayoutY();
            gridPane.getChildren().add(this);
            this.setTranslateX(c);
            TranslateTransition t = new TranslateTransition();
            t.setNode(this);
            t.setToX(c);
            t.setToY(r);
            t.setCycleCount(1);
            t.setInterpolator(Interpolator.EASE_OUT);
            t.setDuration(Duration.seconds(.3));
            t.play();
            t.setOnFinished(e -> {
                gridPane.getChildren().remove(this);
                parentButton.setState(state);
            });

        }

    }
}
