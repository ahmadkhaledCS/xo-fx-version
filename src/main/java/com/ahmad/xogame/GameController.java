package com.ahmad.xogame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class GameController {
    @FXML
    Button one,two,three,four,five,six,seven,eight,nine;
    @FXML
    ImageView otherPlayerImage;

    boolean playerTurn=true;
    String[][]board= {{" "," "," "},
                    {" "," "," "},
                    {" "," "," "}};
    boolean isSinglePlayer =true;

    /**
    * I made this game without a game loop it is entirely event
    * driven so all the buttons trigger the Main function
    * the function will determine if the mode is single player
    * or 2 player and act accordingly
    **/
    public void Main(ActionEvent actionEvent) {
        if(isSinglePlayer){
            playerTurn(actionEvent);
        }else{
            //the else is for the vs computer mode
            //if the player played then the computer checks if he can win
            //if he can't, he will check to protect himself
            boolean flag=playerTurn(actionEvent);
            check();
            if(flag)
                if(computerTurn("0"))
                    computerTurn("x");
            update();
        }
        check();
    }
    public void check(){
        if(win("x"))
            restart("x");
        if(win("0"))
            restart("0");
        if(tie())
            restart("tie");
    }
    public boolean playerTurn(ActionEvent actionEvent){
        //importing the images of X and O
        ImageView xView = new ImageView(new Image(String.valueOf(getClass().getResource("images/x.png"))));
        ImageView oView = new ImageView(new Image(String.valueOf(getClass().getResource("images/o.png"))));
        xView.setFitHeight(50);
        oView.setFitWidth(50);
        xView.setFitWidth(50);
        oView.setFitHeight(50);

        Button button=(Button)actionEvent.getSource();//getting the button that triggered the event
        String value ="0"; //this string is to update the board array
        ImageView image= oView;
        if(playerTurn){
            image= xView;
            value ="x";
        }
        //if the opacity of clicked button is 0 then the player clicked a valid
        //button, so we take action else we return false(he made invalid move)
        if (button.getOpacity()==0){
            button.setGraphic(image);
            button.setOpacity(1);
            //the getPosition function returns the position of the clicked button
            int []coordinate=getPosition(button);
            board[coordinate[0]][coordinate[1]]= value;//then we update the board array accordingly
        }else{
            return false;
        }
        //we can only reach here if he played a valid move now we change the player
        if(isSinglePlayer)
            playerTurn=!playerTurn;
        return true;
    }

    /** the getPosition function returns the position of the clicked button **/
    public int[] getPosition(Button button){
        Button [][]a={{one,two,three},{four,five,six},{seven,eight,nine}};
        for(int i=0;i<a.length;i++)
            for(int j=0;j<a[i].length;j++)
                if(button.equals(a[i][j]))
                    return new int[]{i,j};
        return null;
    }

    /**
     * The logic behind the computers' behavior is as follows if he can win
     * by plying a move he will do it and return false if he can't win
     * he will do the same function but to block your win if he can't win and
     * there is nothing to block he makes a random move**/
    public boolean computerTurn(String ch) {
        //my strategy to check if he can make a move was by slicing
        //the bord into one dimensional mini arrays and checking them
        //instead of the entire array (I didn't want some messy for loops)
        String []temp={board[0][0],board[1][1],board[2][2]};
        if(canWin(temp,ch)) {
            osPlay(temp,"0");
            board[0][0]=temp[0];
            board[1][1]=temp[1];
            board[2][2]=temp[2];
            return false;
        }
        temp= new String[]{board[0][2], board[1][1], board[2][0]};
        if(canWin(temp,ch)) {
            osPlay(temp,"0");
            board[0][2]=temp[0];
            board[1][1]=temp[1];
            board[2][0]=temp[2];
            return false;
        }
        for(int i=0;i<3;i++)
        {
            if(canWin(board[i],ch)) {
                osPlay(board[i],"0");
                return false;
            }
            temp= new String[]{board[0][i], board[1][i], board[2][i]};
            if(canWin(temp,ch)) {
                osPlay(temp,"0");
                board[0][i] =temp[0];
                board[1][i]=temp[1];
                board[2][i]=temp[2];
                return false;
            }
        }
        int rand1,rand2;
        if(ch.matches("x")){
            do{
                rand1=(int)(Math.random()*3);
                rand2=(int)(Math.random()*3);
            }
            while(!board[rand1][rand2].matches(" "));
            board[rand1][rand2]="0";
            return false;
        }
        return true;
    }
    public static void osPlay(String []arr,String ch) {
        for(int i=0;i<3;i++)
            if(arr[i].matches(" "))
                arr[i]=ch;
    }
    public static boolean canWin(String []arr,String ch) {
        if(arr[0].matches(ch)&&arr[0].matches(arr[1])&&notFull(arr))
            return true;
        if(arr[0].matches(ch)&&arr[0].matches(arr[2])&&notFull(arr))
            return true;
        return arr[1].matches(ch) && arr[1].matches(arr[2])&&notFull(arr);
    }
    public static boolean notFull(String []arr) {
        for(int i=0;i<3;i++)
            if(arr[i].matches(" "))
                return true;
        return false;
    }
    /**
     * update function updates the fx view according to the board array**/
    public void update(){
        Button [][]a={{one,two,three},{four,five,six},{seven,eight,nine}};
        ImageView oView;
        for(int i=0;i<board.length;i++)
            for(int j=0;j<board[i].length;j++){
                oView = new ImageView(new Image(String.valueOf(getClass().getResource("images/o.png"))));
                oView.setFitWidth(50);
                oView.setFitHeight(50);
                if(board[i][j].equals("0")){
                    a[i][j].setGraphic(oView);
                    a[i][j].setOpacity(1);
                }
            }
    }
    public boolean win(String ch) {
        for(int i=0;i<3;i++)
            if(board[i][0].matches(ch)&&board[i][1].matches(ch)&&board[i][2].matches(ch))
                return true;
            else if(board[0][i].matches(ch)&&board[1][i].matches(ch)&&board[2][i].matches(ch))
                return true;
        if(board[0][0].matches(ch)&&board[1][1].matches(ch)&&board[2][2].matches(ch))
            return true;
        return
                board[0][2].matches(ch) && board[1][1].matches(ch) && board[2][0].matches(ch);
    }
    public boolean tie() {
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                if(board[i][j].matches(" "))
                    return false;
        return true;
    }
    public void restart(String player){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Game over");
        if(player.equals("tie"))
            alert.setHeaderText("it is a tie!");
        else
            alert.setHeaderText(String.format("player %s won the game",player));
        alert.setContentText("Press ok to play again");
        alert.showAndWait();
        reset();
    }
    public void reset(){
        board=new String[][]{{" "," "," "},{" "," "," "},{" "," "," "}};
        for(Button i:new Button[]{one,two,three,four,five,six,seven,eight,nine}){
            i.setOpacity(0);
            i.setGraphic(null);
        }
        playerTurn=true;
    }
    public void toggleMode() {
        Image other;
        other=new Image(String.valueOf(getClass().getResource("images/pc.png")));
        isSinglePlayer=!isSinglePlayer;
        if(isSinglePlayer)
            other=new Image(String.valueOf(getClass().getResource("images/player.png")));
        otherPlayerImage.setImage(other);
        reset();
    }

    public void switchToMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource("MainMenu.fxml"));
        Stage stage = (Stage) one.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 600, 300);
        stage.setScene(scene);
        stage.show();
    }
}
