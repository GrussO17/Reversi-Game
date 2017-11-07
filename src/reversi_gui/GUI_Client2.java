package reversi_gui;

import javafx.application.Application;

import java.util.*;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import reversi.ReversiException;
import reversi2.Board;
import reversi2.NetworkClient;

/**
 * This application is the UI for Reversi.
 *
 * @author Owen Gruss
 */
public class GUI_Client2 extends Application  implements Observer  {

    /**
     * Connection to network interface to server
     */
    private NetworkClient serverConn;


    private Board board;

    private GridPane buttons;

    private Label turnNum;
    private Label turn;

    private Image PEmpty = new Image(getClass().getResourceAsStream("empty.jpg"));
    private Image P1 = new Image(getClass().getResourceAsStream("othelloP1.jpg"));
    private Image P2 = new Image(getClass().getResourceAsStream("othelloP2.jpg"));


    /**
     * Where the command line parameters will be stored once the application
     * is launched.
     */
    private Map< String, String > params = null;

    /**
     * Look up a named command line parameter (format "--name=value")
     * @param name the string after the "--"
     * @return the value after the "="
     * @throws ReversiException if name not found on command line
     */
    private String getParamNamed( String name ) throws ReversiException {
        if ( params == null ) {
            params = super.getParameters().getNamed();
        }
        if ( !params.containsKey( name ) ) {
            throw new ReversiException(
                    "Parameter '--" + name + "=xxx' missing."
            );
        }
        else {
            return params.get( name );
        }
    }
    public void init() throws ReversiException{
        this.board = new Board();
            int port  = Integer.parseInt(getParamNamed("port"));
            String host = getParamNamed("host");
            this.serverConn = new NetworkClient(host, port, board);
        board.addObserver(this);

    }
    public void start( Stage mainStage ) {

        BorderPane border = new BorderPane();



        buttons = new GridPane();
        for(int row = 0; row < board.getNRows(); row ++){
            for(int col = 0; col < board.getNCols(); col++) {
                Button b1 = new Button();
                b1.setUserData(new Coordinates(row, col));
                b1.setFocusTraversable(false);
                b1.setOnAction( event -> serverConn.sendMove(((Coordinates)b1.getUserData()).getRow(), ((Coordinates)b1.getUserData()).getCol()));
                b1.setGraphic(new ImageView(PEmpty));
                buttons.add(b1, row, col);
            }
        }
        border.setCenter(buttons);

        turnNum = new Label("Moves Left 0");
        turn = new Label("Starting game...");

        Region filler = new Region();
        HBox bottom = new HBox(turnNum, filler,  turn);
        HBox.setHgrow(filler, Priority.ALWAYS);

        border.setBottom(bottom);
        Scene sc = new Scene(border);
        mainStage.setTitle("Reversi");
        mainStage.setScene(sc);
        mainStage.show();

        board.initializeGame();
    }

    public void newBoard(){
        if(board.getStatus())

        System.out.println("ITS GONNA FUCKIN WORK");
        for(Node stuff: buttons.getChildren()){
            Coordinates pair = (Coordinates)stuff.getUserData();
            if(board.getContents(pair.getRow(), pair.getCol()).equals(Board.Move.PLAYER_ONE)) {
                ((Button) stuff).setGraphic(new ImageView(P1));
            }
            else if(board.getContents(pair.getRow(), pair.getCol()).equals(Board.Move.PLAYER_TWO)) {
                ((Button) stuff).setGraphic(new ImageView(P2));
            }
            else{
                ((Button) stuff).setGraphic(new ImageView(PEmpty));
            }
            if(board.isValidMove(pair.getRow(), pair.getCol()) && board.isMyTurn()){
                stuff.setDisable(false);
            }
            else{
                stuff.setDisable(true);
            }
            turnNum.setText("Moves Left " + board.getMovesLeft());
            if(board.isMyTurn())
                turn.setText("Your Turn");
            else
                turn.setText("Waiting for other player...");



        }
    }
    public void update(Observable obs, Object obj){
        javafx.application.Platform.runLater(() -> newBoard());
    }






    /**
     * Launch the JavaFX GUI.
     *
     * @param args not used, here, but named arguments are passed to the GUI.
     *             <code>--host=<i>hostname</i> --port=<i>portnum</i></code>
     */
    public static void main( String[] args ) {
        Application.launch( args );
    }

}
