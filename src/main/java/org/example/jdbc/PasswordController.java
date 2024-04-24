package org.example.jdbc;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PasswordController {

    public TextField tfNewPassword;
    public TextField tfConfirmNewPassword;
    public Label notif;
    public Button btnHome;

    public void onUpdatePasswordClick(){
        String new_pass = tfNewPassword.getText();
        String confirm_pass = tfConfirmNewPassword.getText();
        if(new_pass.isEmpty() || confirm_pass.isEmpty()){
            notif.setText("Please fill up all fields!");
            return;
        }
        if(!new_pass.equals(confirm_pass)){
            notif.setText("Password does not match up!");
            return;
        }

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "UPDATE users SET password=? WHERE userid=?"
             )) {
            statement.setString(1, new_pass);
            statement.setInt(2, LoginController.currentID);
            statement.executeUpdate();

            notif.setText("Password Updated!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onHomeButtonClick() throws IOException {
        Stage stage = (Stage) btnHome.getScene().getWindow();
        stage.close();
        // Parent p = FXMLLoader.load(getClass().getResource("home-view.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Parent p = loader.load();
        HomepageController homepageController = loader.getController();
        homepageController.displayContents(LoginController.currentID);
        Scene s = new Scene(p);
        stage.setScene(s);
        stage.show();
    }
}
