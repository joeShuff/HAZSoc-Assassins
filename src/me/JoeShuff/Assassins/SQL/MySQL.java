/*
 * 
 * @author - Joseph Shufflebotham
 * 
 * � Copyright 2016 
 */
package me.JoeShuff.Assassins.SQL;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.JoeShuff.Assassins.MainWindow;

public class MySQL
{
  private static Connection connection;
  private static Statement s;

  private static String SERVER_IP = "";
  private static String NAME = "";
  private static String USER = "";
  private static String PASS = "";
  
  public static void initialiseSQL()
  {
	  try {
		new MySQL(SERVER_IP, NAME, USER, PASS);
	} catch (SQLTimeoutException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  public MySQL(String ip, String name, String user, String pass) throws SQLTimeoutException
  {
    try
    {
    	String DB_NAME = "jdbc:mysql://" + ip + ":3306/" + name + "?autoReconnect=true&interactive_timeout=" + Integer.MAX_VALUE;
    	Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Connecting to database!");
        connection = DriverManager.getConnection(DB_NAME, user, pass);
        System.out.println("Connected to database");
        s = connection.createStatement();
        DatabaseMetaData dm = connection.getMetaData(); 
    }
    catch (Exception e)
    {
      throw new SQLTimeoutException("Error Connecting to the provided SQL");
    }
  }
  
  public static void openConnection(String ip, String name, String user, String pass) throws SQLTimeoutException
  {
    new MySQL(ip, name, user, pass);
  }
  
  public static Connection getConnection()
  {
    return connection;
  }
  
  public static boolean tableExists(String tableName)
  {
	  try
	  {
		  Connection connection = getConnection();
		  DatabaseMetaData dm = connection.getMetaData();
	      System.out.println("Attempting to find table " + tableName);
	      ResultSet tables = dm.getTables(null, null, tableName, null);
	      if (tables.next()) 
	      {
	    	  return true;
	      }
	  }
	  catch (Exception e)
	  {
		  e.printStackTrace();
		  return false;
	  }
	  
	  return false;
  }
  
  public static boolean executeUpdate(String query)
  {
	  try
	  {
		  Connection c = getConnection();
		  Statement s = c.createStatement();
		  
		  s.executeUpdate(query);
		  
		  return true;
	  }
	  catch (Exception e)
	  {
		  e.printStackTrace();
	  }
	  
	  return false;
  }
}

