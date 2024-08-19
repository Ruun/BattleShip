module com.isruan.battleshipz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.isruan.battleshipz to javafx.fxml;


   // exports com.isruan.battleshipz.controller; // Export the package containing GameController
   // opens com.isruan.battleshipz.controller to javafx.fxml; // Open it for reflection if necessary


    exports com.isruan.battleshipz.Client.Controller to javafx.fxml;
    opens com.isruan.battleshipz.Client.Controller to javafx.fxml;

    exports com.isruan.battleshipz.Server.Controller to javafx.fxml;
    opens com.isruan.battleshipz.Server.Controller to javafx.fxml;
    exports com.isruan.battleshipz;

}