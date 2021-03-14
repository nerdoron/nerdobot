package me.nerdoron.nerdobot.utils;

import java.sql.Connection;
import java.sql.DriverManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;

public class Database {

	
	public static Connection connect() {
        Connection con = null;
        final Logger logger = LoggerFactory.getLogger(Database.class);
        Dotenv dotenv = Dotenv.configure().directory("D:\\dev\\eclipse15\\nerdobot\\.env").load();
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection((dotenv.get("DB")));
        } catch (Exception ex) {
            logger.error("An exception occurred while trying to connect to the database!", ex);
        }
        return con;
    }
	
}