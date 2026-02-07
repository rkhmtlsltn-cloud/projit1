package com.comix.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Database {

    private static Database instance;

    private final String url;
    private final String user;
    private final String pass;

    private Database() {
        Properties p = new Properties();

        try {
            InputStream in = Database.class.getClassLoader().getResourceAsStream("db.properties");
            if (in != null) {
                p.load(in);
                in.close();
            }
        } catch (Exception e) {
        }

        this.url = p.getProperty("db.url", "jdbc:postgresql://localhost:5432/comix_db");
        this.user = p.getProperty("db.user", "postgres");
        this.pass = p.getProperty("db.pass", "0000");
    }

    public static Database getInstance() {
        if (instance == null) instance = new Database();
        return instance;
    }

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, user, pass);
    }
}

