package com.odanorr.bungeeforum;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLUtils
{
  private Connection connection;
  private Statement statement;
  private String url;
  private boolean isConnected = false;
  private static SQLUtils instance = null;
  
  public static SQLUtils getInstance()
  {
    return instance;
  }
  
  public SQLUtils()
  {
    instance = this;
  }
  
  public void connect()
    throws ClassNotFoundException, SQLException
  {
    this.url = "jdbc:mysql://195.154.163.228:3306/site_report";
    
    Class.forName("com.mysql.jdbc.Driver");
    this.connection = DriverManager.getConnection(this.url, "mj", "ezRADh7aqFRx4P9X");
    this.isConnected = true;
    this.statement = getStatement();
  }
  
  public boolean isConnected()
  {
    return this.isConnected;
  }
  
  public Statement getStatement()
  {
    try
    {
      if ((this.statement == null)) {
        return createStatement();
      }
    }
    catch (SQLException e)
    {
      return null;
    }
    return this.statement;
  }
  
  public Statement createStatement()
    throws SQLException
  {
    this.statement = this.connection.createStatement();
    
    return this.statement;
  }
  
  public void closeConnection()
    throws SQLException
  {
    this.statement = null;
    this.isConnected = false;
    this.connection.close();
  }
  
  public Connection getConnection()
  {
    return this.connection;
  }
  
  public ResultSet query(String request)
  {
    try
    {
      return getStatement().executeQuery(request);
    }
    catch (SQLException|NullPointerException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public void update(String request)
  {
    try
    {
      getStatement().executeUpdate(request);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
}
