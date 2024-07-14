package faceattendancesystem;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
/**
 *
 * @author shree
 */
public class AdminPanel implements ActionListener {
    
    JFrame adminPanel;
    JLabel title,uNMLabel,passLabel;
    JTextField uNM;
    JPasswordField pass;
    JButton loginBtn;
    AdminPanel() {
        
        adminPanel=new JFrame("Face Attendance System");
        adminPanel.setSize(500,500);
        adminPanel.setLayout(null);
        adminPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminPanel.getContentPane().setBackground(Color.WHITE);
        
        title=new JLabel("Welcome ADMIN !");
        title.setBounds(100,30,300,100);
        Font f1=new Font("Times New Roman",Font.BOLD,34);
        title.setFont(f1);
        adminPanel.add(title);
        
        
        Font f2=new Font("Arial",Font.BOLD,14);
        uNMLabel=new JLabel("Username :");
        uNMLabel.setFont(f2);
        uNMLabel.setBounds(80,160,100,50);
        adminPanel.add(uNMLabel);
        
        passLabel=new JLabel("Password :");
        passLabel.setFont(f2);
        passLabel.setBounds(80,200,100,50);
        adminPanel.add(passLabel);
        
        
        
        uNM=new JTextField();
        uNM.setBounds(180,175,140,28);
        adminPanel.add(uNM);
        
        pass=new JPasswordField();
        pass.setBounds(180,215,140,28);
        adminPanel.add(pass);
        
        loginBtn=new JButton("Login");
        loginBtn.setFont(f2);
        loginBtn.setBounds(180,250,100,40);
        loginBtn.addActionListener(this);
        adminPanel.add(loginBtn);
        
        adminPanel.setLocationRelativeTo(null);
        adminPanel.setVisible(true);
        
    }
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == loginBtn) {
            
            if((uNM.getText().toString().equals("admin")) && (pass.getText().toString().equals("admin"))) {
            adminPanel.dispose();
            JOptionPane.showMessageDialog(null,"Login Successfull !");
            AdminDashboard admin=new AdminDashboard();
            }
            else {
            JOptionPane.showMessageDialog(null,"Login Invalid ");
            }
                
        }
    }
   /* public static void main(String args[]) {
        AdminPanel ap=new AdminPanel();
    }*/
  
    
}
