package org.example.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public void create_tables() {
        try (Connection c = MySQLConnection.getConnection()) {
            c.setAutoCommit(false);

            try (Statement statement = c.createStatement()) {
                String query1 = "CREATE TABLE IF NOT EXISTS users (" +
                        "userid INT PRIMARY KEY AUTO_INCREMENT," +
                        "name VARCHAR(50) NOT NULL," +
                        "email VARCHAR(50) NOT NULL," +
                        "password VARCHAR(50) NOT NULL)";
                String query2 = "CREATE TABLE IF NOT EXISTS posts (" +
                        "postid INT PRIMARY KEY AUTO_INCREMENT," +
                        "acctid INT(10) NOT NULL," +
                        "title VARCHAR(50) NOT NULL," +
                        "content VARCHAR(100) NOT NULL," +
                        "FOREIGN KEY (acctid) REFERENCES users(userid))";

                statement.execute(query1);
                statement.execute(query2);
                c.commit();

                System.out.println("Table \"users\" created successfully!");
                System.out.println("Table \"posts\" created successfully!");

            } catch (SQLException e) {
                c.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
