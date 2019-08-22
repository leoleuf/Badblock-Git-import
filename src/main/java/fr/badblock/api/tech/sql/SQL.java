package fr.badblock.api.tech.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class SQL {

    private String baseurl;
    private String host;
    private String database;
    private String username;
    private String password;
    private String table;
    private Connection connection;

    /** SQL Connection Parameter Method **/
    public SQL(String baseurl, String host, String database, String username, String password, String table) {
        this.baseurl = baseurl;
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.table = table;
    }
    /** Connect Method **/
    public void connection() {
        if (!isConnected()) {
            try {
                this.connection = DriverManager.getConnection(this.baseurl + this.host + "/" + this.database);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /** Disconnect Method **/
    public void disconnect() {
        if (isConnected()) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /** Check Connection **/
    public boolean isConnected() {
        try {
            if (this.connection == null || this.connection.isClosed() || this.connection.isValid(5)) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /** Get the current connection **/
    private Connection getConnection() {
        return this.connection;
    }
}
