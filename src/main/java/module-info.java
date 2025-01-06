module melke.bogdo.kth.lab2.labb2mungodb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mongo.java.driver;

    exports melke.bogdo.kth.lab2.labb2mungodb.View to javafx.graphics;
    exports melke.bogdo.kth.lab2.labb2mungodb.Model;
    exports melke.bogdo.kth.lab2.labb2mungodb.Model.DAO;

    opens melke.bogdo.kth.lab2.labb2mungodb to javafx.fxml;
    opens melke.bogdo.kth.lab2.labb2mungodb.Model to javafx.fxml;
    opens melke.bogdo.kth.lab2.labb2mungodb.Model.DAO to javafx.fxml;
    exports melke.bogdo.kth.lab2.labb2mungodb to javafx.graphics;
}
