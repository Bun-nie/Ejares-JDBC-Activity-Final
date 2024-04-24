package org.example.jdbc;

import com.mysql.cj.log.Log;
import com.mysql.cj.xdevapi.Table;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class HomepageController {
    @FXML
    public Label notif;
    @FXML
    public TextField tfTitle;
    @FXML
    public TextArea taContent;
    @FXML
    public TableView<Post> tblContents;
    @FXML
    public TableColumn<Post, String> titleColumn;
    @FXML
    public TableColumn<Post, String> contentColumn;
    @FXML
    public Button btnPost;
    @FXML
    public Button btnUpdate;
    @FXML
    public Button btnDelete;
    @FXML
    public Button btnLogout;
    public Button btnToChangePassword;
    public Button btnDeleteAccount;
    int index, postid;

    public void onPostClick(ActionEvent ae){
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO posts(acctid, title, content) VALUES (?,?,?)"
            )) {
            if(tfTitle.getText().isEmpty() || taContent.getText().isEmpty()){
                notif.setText("Nothing to post!");
                return;
            }
            int acctid = LoginController.currentID;
            String title = tfTitle.getText();
            String content = taContent.getText();
            statement.setInt(1, acctid);
            statement.setString(2, title);
            statement.setString(3, content);
            int rowsInserted = statement.executeUpdate(); //for CREATE, UPDATE, DELETE, return int
            System.out.println("Rows Inserted: " + rowsInserted);
            tfTitle.setText("");
            taContent.setText("");
            notif.setText("You posted a new content!");
            displayContents(acctid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onUpdateClick(ActionEvent ae){

        if (tblContents.getSelectionModel().getSelectedIndex() == -1) {
            notif.setText("No Post Selected!");
            return;
        }
        index = tblContents.getSelectionModel().getSelectedIndex();
        postid = tblContents.getItems().get(index).getPostid();
        System.out.println(postid);
        String new_title = tfTitle.getText();
        String new_content = taContent.getText();
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "UPDATE posts SET title=?, content=? WHERE postid=?"
             )) {
            statement.setString(1, new_title);
            statement.setString(2, new_content);
            statement.setInt(3, postid);
            statement.executeUpdate();

            displayContents(LoginController.currentID);

            notif.setText("Post Updated!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onDeleteClick(ActionEvent ae){
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "DELETE FROM posts WHERE postid=?"
             )) {
            index = tblContents.getSelectionModel().getSelectedIndex();
            postid = tblContents.getItems().get(index).getPostid();
            statement.setInt(1, postid);
            statement.executeUpdate();

            displayContents(LoginController.currentID);

            notif.setText("Post Deleted!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onLogoutButtonClick(ActionEvent ae) throws IOException {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.close();
        // Parent p = FXMLLoader.load(getClass().getResource("home-view.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Parent p = loader.load();
        Scene s = new Scene(p);
        stage.setScene(s);
        stage.show();
    }

    public void onDeleteAccountClick(){
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement deletePostsStatement = c.prepareStatement(
                     "DELETE FROM posts WHERE acctid=?"
             );
             PreparedStatement deleteUserStatement = c.prepareStatement(
                     "DELETE FROM users WHERE userid=?"
             )) {
            c.setAutoCommit(false);
            int userId = LoginController.currentID;

            deletePostsStatement.setInt(1, userId);
            deletePostsStatement.executeUpdate();

            deleteUserStatement.setInt(1, userId);
            deleteUserStatement.executeUpdate();

            c.commit();

            Stage stage = (Stage) btnDeleteAccount.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent p = loader.load();
            Scene s = new Scene(p);
            stage.setScene(s);
            stage.show();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            try(Connection c = MySQLConnection.getConnection()) {
                c.rollback();
            } catch (SQLException s) {
                s.printStackTrace();
            }
        }
    }


    public void onToPasswordChangeClick() throws IOException {
        Stage stage = (Stage) btnToChangePassword.getScene().getWindow();
        stage.close();
        // Parent p = FXMLLoader.load(getClass().getResource("home-view.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("password-view.fxml"));
        Parent p = loader.load();
        Scene s = new Scene(p);
        stage.setScene(s);
        stage.show();
    }
    public void displayContents(int currentUserId) {
        ObservableList<Post> posts = FXCollections.observableArrayList();
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("SELECT * FROM posts WHERE acctid = ?")
        ) {
            statement.setInt(1, currentUserId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Post p = new Post();
                p.setPostid(rs.getInt("postid"));
                p.setAcctid(rs.getInt("acctid"));
                p.setTitle(rs.getString("title"));
                p.setContent(rs.getString("content"));
                posts.add(p);
            }
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));

            tblContents.setItems(posts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tblContents.setRowFactory(tv->{
            TableRow<Post> postrow = new TableRow<>();
            postrow.setOnMouseClicked(event->{
                if(event.getClickCount() == 1 && (!postrow.isEmpty())){
                    index = tblContents.getSelectionModel().getSelectedIndex();
                    postid = tblContents.getItems().get(index).getPostid();
                    tfTitle.setText(tblContents.getItems().get(index).getTitle());
                    taContent.setText(tblContents.getItems().get(index).getContent());
                }
            });
            return postrow;
        });
    }
}
