
package faceattendancesystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.ResultSet;


/**
 *
 * @author shree
 */
public class test {
    public static void main(String args[]) {
         LocalDate currentDate = LocalDate.now();
         java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);
         System.out.println(sqlDate);
         Connection c1;
         Statement s1;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c1=DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql","root","root");
            s1=c1.createStatement();
            s1.execute("USE faceattendancesystem;");
            //s1.execute("INSERT INTO first_year VALUES(2254014,'shree','cse','"+sqlDate.toString()+"',CURRENT_TIMESTAMP);");
            ResultSet r1=s1.executeQuery("SELECT prn FROM first_year where date='2024-02-28' AND (time >='2024-02-28 19:10:30' AND time <='2024-02-28 19:10:50');");
            r1.next();
            System.out.println(r1.getInt(1));
        } catch (Exception ex) 
          {
            System.out.println(ex); 
          }
        
}
}
