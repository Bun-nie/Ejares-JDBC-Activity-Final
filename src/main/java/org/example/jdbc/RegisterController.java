package org.example.jdbc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class RegisterController {
    public TextField tfUsername;
    public TextField tfEmail;
    public PasswordField pfPassword;
    public Button btnRegister;
    public Label success;
    public Button btnBackToLogin;

    @FXML
    public void onRegisterButtonClick(ActionEvent ae) {
        String name = tfUsername.getText();
        String email = tfEmail.getText();
        String pass = pfPassword.getText();
        if(name.isEmpty() || pass.isEmpty() || email.isEmpty()){
            success.setText("Please fill all fields!");
            return;
        }
        if(userExists(name, email)){
            success.setText("User already exists!");
            return;
        }
        register(name, email, pass);
        success.setText("Account Created! Please Login!");
    }

    @FXML
    public void onToLoginButtonClick(ActionEvent ae) throws IOException {
        Stage stage = (Stage) btnBackToLogin.getScene().getWindow();
        Parent p = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        stage.close();
        Scene s = new Scene(p);
        stage.setScene(s);
        stage.show();
    }

    public void register(String username, String email, String password){
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO users(name, email, password) VALUES (?,?,?)"
            )) {
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);
            int rowsInserted = statement.executeUpdate(); //for CREATE, UPDATE, DELETE, return int
            System.out.println("Rows Inserted: " + rowsInserted);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean userExists(String username, String email){
        try (Connection c = MySQLConnection.getConnection();
             Statement statement = c.createStatement()) {
            String query = "SELECT * FROM users";
            ResultSet res = ((Statement) statement).executeQuery(query);//for reading, returns ResultSet
            while (res.next()){
                if(username.equals(res.getString("name")) && email.equals(res.getString("email")) ){
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
