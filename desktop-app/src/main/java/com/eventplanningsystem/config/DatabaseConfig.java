package com.eventplanningsystem.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.flywaydb.core.Flyway;

public class DatabaseConfig {
    public static void initializeDatabase() {
        Dotenv dotenv = Dotenv.load();
        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");

        Flyway flyway = Flyway.configure().dataSource(url, user, password).load();
        flyway.migrate();
    }
}