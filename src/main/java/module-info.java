module com.ahmad.xogame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ahmad.xogame to javafx.fxml;
    exports com.ahmad.xogame;
}