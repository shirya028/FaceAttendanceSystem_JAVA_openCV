
package faceattendancesystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author shree
 */
public class DatabaseSetup {
    
    Connection c1=null;
    Statement s1;
    public DatabaseSetup() {
        try {
            c1=DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql","root","root");
            s1=c1.createStatement();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
    }
    
    private void setUp() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connection done :)");
            s1.execute("CREATE DATABASE FaceAttendanceSystem;");
            s1.execute("USE FaceAttendanceSystem;");
            System.out.println("Database Created");
            s1.execute("CREATE TABLE first_year(prn INT,branch VARCHAR(20) ,date DATE,time TIMESTAMP);");
            s1.execute("CREATE TABLE second_year(prn INT,branch VARCHAR(20) ,date DATE,time TIMESTAMP);");
            s1.execute("CREATE TABLE third_year(prn INT,branch VARCHAR(20) ,date DATE,time TIMESTAMP);");
            s1.execute("CREATE TABLE final_year(prn INT,branch VARCHAR(20) ,date DATE,time TIMESTAMP);");
            s1.execute("CREATE TABLE all_users(PRNS INT PRIMARY KEY,name VARCHAR(20),branch VARCHAR(15),current_year VARCHAR(10));");
            System.out.println("Query Executed");
        }
        catch(ClassNotFoundException | SQLException e) {System.out.println(e);}
        
    }
    private void deleteDB(){
        try{
        s1.execute("DROP DATABASE FaceAttendanceSystem");
        }
        catch(Exception e) 
        {System.out.println(e);}
    }
    
    public static void main(String args[]) {
        DatabaseSetup d1=new DatabaseSetup();
        d1.setUp();
        //d1.deleteDB();
    }
}
