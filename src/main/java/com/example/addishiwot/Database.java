package com.example.addishiwot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Database {
    private static final String URL = "your database url here";
    private static final String USER = "your username here";
    private static final String PASSWORD = "your password here";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // Read schema.sql
            InputStream is = Database.class.getResourceAsStream("/schema.sql");
            if (is == null) {
                System.err.println("schema.sql not found!");
                return;
            }
            
            String schema = new BufferedReader(new InputStreamReader(is))
                .lines().collect(Collectors.joining("\n"));
            
            // Split by semicolon to execute individual statements
            String[] statements = schema.split(";");
            
            try (Statement stmt = conn.createStatement()) {
                for (String sql : statements) {
                    if (!sql.trim().isEmpty()) {
                        stmt.execute(sql);
                    }
                }
                System.out.println("Database initialized successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
