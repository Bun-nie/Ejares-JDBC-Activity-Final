package org.example.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public void create_tbl_users(){
        Connection c = MySQLConnection.getConnection();
        String query = "CREATE TABLE IF NOT EXISTS users (" +
                "userid INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(50) NOT NULL," +
                "email VARCHAR(50) NOT NULL," +
                "password VARCHAR(50) NOT NULL)";
        try {
            Statement statement = c.createStatement();
            statement.execute(query);
            System.out.println("Table \"users\" created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void create_tbl_posts(){
        Connection c = MySQLConnection.getConnection();
        String query = "CREATE TABLE IF NOT EXISTS posts (" +
                "postid INT PRIMARY KEY AUTO_INCREMENT," +
                "acctid INT(10) NOT NULL," +
                "title VARCHAR(50) NOT NULL," +
                "content VARCHAR(100) NOT NULL," +
                "FOREIGN KEY (acctid) REFERENCES users(userid))";
        try {
            Statement statement = c.createStatement();
            statement.execute(query);
            System.out.println("Table \"posts\" created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
