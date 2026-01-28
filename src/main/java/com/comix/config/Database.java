package com.comix.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    private static Database instance;

    private Database() {
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/comix_db",
                "postgres",
                "0000"
        );
    }
}
