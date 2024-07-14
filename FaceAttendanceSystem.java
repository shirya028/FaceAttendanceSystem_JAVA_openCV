
package faceattendancesystem;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.event.ActionListener;
/**
 *
 * @author shree
 */
public class FaceAttendanceSystem implements ActionListener {

    JFrame dashboard;
    JLabel title,temp,img,adminLabel,clientLabel;
    JButton admin,client;
    
    public FaceAttendanceSystem() {
        dashboard=new JFrame("Face Attendance System");
        dashboard.setSize(500,500);
        dashboard.setLayout(null);
        dashboard.getContentPane().setBackground(Color.WHITE);
        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        title =new JLabel("Face Recognition");
        temp = new JLabel("Attendance System");
        title.setBounds(50,50,250,100);
        temp.setBounds(50,78,250,100);
        Font f1=new Font("Times New Roman",Font.BOLD,24);
        title.setFont(f1);
        temp.setFont(f1);
        dashboard.add(title);
        dashboard.add(temp);
        
        img=new JLabel("");
        img.setBounds(300,50,160,150);
        ImageIcon im =new ImageIcon("images/logo.png");
        img.setIcon(im);
        dashboard.add(img);
        
        admin=new JButton("");
        ImageIcon i2=new ImageIcon("images/admin.jpg");
        admin.setBounds(120,300, 100, 100);
        admin.setIcon(i2);
        admin.addActionListener(this);
        dashboard.add(admin);
        
        client=new JButton("");
        ImageIcon i3=new ImageIcon("images/student.jpg");
        client.setBounds(250,300, 100, 100);
        client.setIcon(i3);
        client.addActionListener(this);
        dashboard.add(client);
        
        adminLabel=new JLabel("Admin");
        adminLabel.setBounds(145,380,100,50);
        dashboard.add(adminLabel);
        
        clientLabel=new JLabel("student");
        clientLabel.setBounds(275,380,100,50);
        dashboard.add(clientLabel);
        dashboard.setLocationRelativeTo(null);
        
        dashboard.setVisible(true);
    }
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == admin) {
            dashboard.dispose();
            AdminPanel ap=new AdminPanel();
            
        }
        if(ae.getSource() == client) {
            UserDashboard um=new UserDashboard();
        }
    }
    public static void main(String[] args) {
        FaceAttendanceSystem f1=new FaceAttendanceSystem();
    }
    
}
