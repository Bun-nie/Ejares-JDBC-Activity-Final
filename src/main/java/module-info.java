module org.example.jdbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens org.example.jdbc to javafx.fxml;
    exports org.example.jdbc;
}