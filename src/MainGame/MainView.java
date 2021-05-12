package MainGame;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.Stage;

import java.io.IOException;


public class MainView {
    public Button one,two,three,four,five,six,seven,eight,nine;
    public ImageView otherPlayerImage;
    public boolean player=true;
    public String[][]arr= {{" "," "," "},{" "," "," "},{" "," "," "}};
    boolean Mode =true;
    //------------------------------------------
    public void Main(Event e){
        if(Mode) {
            player(e);
        }
        else {
            boolean flag=player(e);
            check();
            if(flag)
            if(cpu("0"))
                cpu("x");
            update();
            print();
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
    //------------------------------------------
    public void switchToMenu(Event event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MenuView.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void reset(){
        arr=new String[][]{{" "," "," "},{" "," "," "},{" "," "," "}};
        for(Button i:new Button[]{one,two,three,four,five,six,seven,eight,nine}){
            i.setOpacity(0);
            i.setGraphic(null);
        }
        player=true;
    }
    public void toggleMode(){
        Image other;
        other=new Image("assets\\pc.png");
        Mode=!Mode;
        if(Mode) other=new Image("assets\\player.png");
        otherPlayerImage.setImage(other);
        reset();
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
    public int[] pos(Button b){
        Button [][]a={{one,two,three},{four,five,six},{seven,eight,nine}};
        for(int i=0;i<a.length;i++)
            for(int j=0;j<a[i].length;j++)
                if(b.equals(a[i][j]))
                    return new int[]{i,j};
        return null;
    }
    public boolean player(Event e){
        ImageView xView = new ImageView(new Image("assets\\x.png"));
        ImageView oView = new ImageView(new Image("assets\\o.png"));
        xView.setFitHeight(50);oView.setFitWidth(50);
        xView.setFitWidth(50);oView.setFitHeight(50);
        String pl="0";
        ImageView image= oView;
        if(player){ image= xView;pl="x";}

        Button button=(Button)e.getSource();
        if (button.getOpacity()==0){
            button.setGraphic(image);
            button.setOpacity(1);
            arr[pos(button)[0]][pos(button)[1]]=pl;
        }
         else
             return false;
         print();
         if(Mode)
             player=!player;
         return true;
    }
    //----------------------------------
    public boolean tie() {
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                if(arr[i][j].matches(" "))
                    return false;
        return true;
    }
    //--------------------------------
    public void print() {
        for(int i=0;i<3;i++)
            System.out.println(arr[i][0]+"|"+arr[i][1]+"|"+arr[i][2]);
    }
    //------------------------------
    public boolean win(String ch)
    {
        for(int i=0;i<3;i++)
            if(arr[i][0].matches(ch)&&arr[i][1].matches(ch)&&arr[i][2].matches(ch))
                return true;
            else if(arr[0][i].matches(ch)&&arr[1][i].matches(ch)&&arr[2][i].matches(ch))
                return true;

        if(arr[0][0].matches(ch)&&arr[1][1].matches(ch)&&arr[2][2].matches(ch))
            return true;
        return
                arr[0][2].matches(ch) && arr[1][1].matches(ch) && arr[2][0].matches(ch);
    }
    //-----------------------------------

    public boolean cpu(String ch)
    {
        String []temp={arr[0][0],arr[1][1],arr[2][2]};
        if(canWin(temp,ch)) {
            osPlay(temp,"0");
            arr[0][0]=temp[0];
            arr[1][1]=temp[1];
            arr[2][2]=temp[2];
            return false;
        }
        temp= new String[]{arr[0][2], arr[1][1], arr[2][0]};
        if(canWin(temp,ch)) {
            osPlay(temp,"0");
            arr[0][2]=temp[0];
            arr[1][1]=temp[1];
            arr[2][0]=temp[2];
            return false;
        }
        for(int i=0;i<3;i++)
        {
            if(canWin(arr[i],ch)) {
                osPlay(arr[i],"0");
                return false;
            }
            temp= new String[]{arr[0][i], arr[1][i], arr[2][i]};
            if(canWin(temp,ch)) {
                osPlay(temp,"0");
                arr[0][i] =temp[0];
                arr[1][i]=temp[1];
                arr[2][i]=temp[2];
                return false;
            }
        }
        int rand1,rand2;
        if(ch.matches("x")){
            do{
                rand1=(int)(Math.random()*3);
                rand2=(int)(Math.random()*3);
            }
            while(!arr[rand1][rand2].matches(" "));
            arr[rand1][rand2]="0";
            return false;
        }
        return true;
    }
    public void update(){
        Button [][]a={{one,two,three},{four,five,six},{seven,eight,nine}};
        ImageView oView;
        for(int i=0;i<arr.length;i++)
            for(int j=0;j<arr[i].length;j++){
                oView = new ImageView(new Image("assets\\o.png"));
                oView.setFitWidth(50);
                oView.setFitHeight(50);
                if(arr[i][j].equals("0")){
                    a[i][j].setGraphic(oView);
                    a[i][j].setOpacity(1);
                }
            }
    }
    //-----------------------------------
    public static void osPlay(String []arr,String ch)
    {
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
    public static boolean notFull(String []arr)
    {
        for(int i=0;i<3;i++)
            if(arr[i].matches(" "))
                return true;
        return false;
    }
}