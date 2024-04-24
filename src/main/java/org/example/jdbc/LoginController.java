package org.example.jdbc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    /*PROPERTIES*/
    @FXML
    public Button btnLogin;
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    public Label wrong;
    @FXML
    public Button btnToRegister;
    @FXML
    public Label sentence;
    @FXML
    public BorderPane pnLogin;
    @FXML
    public TextField tfPassword;
    public static int currentID;
    public static String currentName;


    /*FUNCTIONALITIES*/

    @FXML
    public void onLoginButtonClick(ActionEvent ae) {
        try {
            String name = tfUsername.getText();
            String pass = pfPassword.getText();
            if(name.isEmpty() || pass.isEmpty()){
                wrong.setText("Please fill all fields!");
                return;
            }
            if(userExists(name,pass)){
                Stage stage = (Stage) btnToRegister.getScene().getWindow();
                stage.close();
                // Parent p = FXMLLoader.load(getClass().getResource("home-view.fxml"));
                FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
                Parent p = loader.load();
                HomepageController homepageController = loader.getController();
                homepageController.displayContents(currentID); // Display contents for the current user
                Scene s = new Scene(p);
                stage.setScene(s);
                stage.show();
                System.out.println("CurrentID: " + currentID + "\nCurrent Name: " + currentName);
            } else {
                wrong.setText("Wrong Credentials!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void onShowButtonClick(MouseEvent ae){
        tfPassword.setText(pfPassword.getText());
        tfPassword.setVisible(true);
    }

    @FXML
    public void onShowButtonUnclick(MouseEvent ae){
        tfPassword.setVisible(false);
        pfPassword.setText(tfPassword.getText());
    }

    @FXML
    public void onToRegisterButtonClick(ActionEvent ae) throws IOException {
        Stage stage = (Stage) btnToRegister.getScene().getWindow();
        Parent p = FXMLLoader.load(getClass().getResource("register-view.fxml"));
        stage.close();
        Scene s = new Scene(p);
        stage.setScene(s);
        stage.show();
    }

    public boolean userExists(String username, String password){
        try (Connection c = MySQLConnection.getConnection();
             Statement statement = c.createStatement()) {
            String query = "SELECT * FROM users";
            ResultSet res = ((Statement) statement).executeQuery(query);//for reading, returns ResultSet
            while (res.next()){
                int id = res.getInt("userid");
                if(username.equals(res.getString("name")) && password.equals(res.getString("password")) ){
                    currentID = id;
                    currentName = res.getString("name");
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}