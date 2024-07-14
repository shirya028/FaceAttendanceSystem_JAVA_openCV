package faceattendancesystem;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;


/**
 *
 * @author shree
 */
public class UserDashboard implements ActionListener {
    JFrame dashboard;
    JPanel colorPanel;
    JTextField name,prn;
    JComboBox branch,_class;
    JButton next,cancel;
    JLabel panelTxt,nameTxt,prnTxt,branchTxt,classTxt;
    Font f1,f2;
    Connection c1=null;
    Statement s1;
    String branches[]={ "CSE","IT","MECHANICAL","CIVIL","ELECTRICAL","ENTC"};
    String classes[]={"first_year","second_year","third_year","final_year"};
    ArrayList<Integer> ar=new ArrayList<>();
    
    
    public UserDashboard() {
        dashboard=new JFrame("Face attendance system");
        dashboard.setSize(350,500);
        dashboard.setLayout(null);
        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        colorPanel=new JPanel();
        colorPanel.setSize(400,70);
        colorPanel.setBounds(0,0,400,70);
        colorPanel.setLayout(null);
        colorPanel.setBackground(new Color(199, 200, 204));
        dashboard.add(colorPanel);
        
        
        f1=new Font("Times New Roman",Font.BOLD,24);
        f2=new Font("Times New Roman",Font.BOLD,15);
        panelTxt =new JLabel("Welcome user");
        nameTxt=new JLabel("Name :");
        prnTxt=new JLabel("PRN :");
        branchTxt=new JLabel("Branch :");
        classTxt=new JLabel("Class :");
        setLabel();
        
        name=new JTextField();
        prn=new JTextField();
        setTextField();
        
        branch=new JComboBox(branches);
        _class=new JComboBox(classes);
        setComboBox();
        
        next=new JButton("next");
        cancel=new JButton("cancel");
        setButtons();
        
        try {
            c1=DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql","root","root");
            s1=c1.createStatement();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        dashboard.setLocationRelativeTo(null);
        dashboard.setVisible(true);
    }
    private void setLabel() {
        panelTxt.setBounds(20,15,200,40);
        panelTxt.setFont(f1);
        colorPanel.add(panelTxt);
        dashboard.add(colorPanel);
        
        nameTxt.setBounds(30,140,100,40);
        nameTxt.setFont(f2);
        dashboard.add(nameTxt);
        
        prnTxt.setBounds(30,190,100,40);
        prnTxt.setFont(f2);
        dashboard.add(prnTxt);
        
        branchTxt.setBounds(30,235,100,40);
        branchTxt.setFont(f2);
        dashboard.add(branchTxt);
        
        classTxt.setBounds(30,280,100,40);
        classTxt.setFont(f2);
        dashboard.add(classTxt);
    }
    private void setTextField() {
        name.setBounds(90,150,150,25);
        dashboard.add(name);
       
        prn.setBounds(90,200,150,25);
        dashboard.add(prn);
    }
    private void setComboBox() {
        branch.setBounds(90,245,150,25);
        dashboard.add(branch);
       
        _class.setBounds(90,290,150,25);
        dashboard.add(_class);
    }
    private void setButtons() {
        next.setForeground(Color.WHITE);
        next.setBackground(Color.BLACK);
        next.setBounds(140,340,100,30);
        next.addActionListener(this);
        dashboard.add(next);
        cancel.setForeground(Color.BLACK);
        cancel.setBackground(new Color(199, 200, 204));
        cancel.setBounds(30,340,100,30);
        cancel.addActionListener(this);
        dashboard.add(cancel);
    }
    public void actionPerformed(ActionEvent e) {
        ar.clear();
        if(e.getSource() == next) {
            if(name.getText().equals("") || prn.getText().equals("") ) {
                JOptionPane.showMessageDialog(null, "Enter details correctly");
                return;
            }
            String str="PRNS";
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                s1.execute("USE faceattendancesystem;");
                ResultSet r1=s1.executeQuery("SELECT * FROM all_users;");

                while(r1.next()) {
                   ar.add(r1.getInt(str));
                }
                
            }
            catch(ClassNotFoundException | SQLException se) {System.out.println(se);}
            if(!ar.contains(Integer.parseInt(prn.getText()))){
                JOptionPane.showMessageDialog(null, "User does not exists");
                return;
            }
            else {
                try {
                    ResultSet r2=s1.executeQuery("SELECT * FROM all_users WHERE PRNS="+ar.get(0));
                    r2.next();
                    String br=r2.getString("branch");
                    String ye=r2.getString("current_year");
                    
                    if(!br.equals(branches[branch.getSelectedIndex()]) || !ye.equals(classes[_class.getSelectedIndex()])) {
                        JOptionPane.showMessageDialog(null,"Invalid credentials");
                        return;
                    }
                    
                        
                    
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
            
            //after all validation .final camera screen on kela
            new Thread(new Runnable() 
            {
                    public void run() {
                        FaceRecognizer f1=new FaceRecognizer();
                        f1.recognize(prn.getText(),classes[_class.getSelectedIndex()],branches[branch.getSelectedIndex()]);
            }
            }).start();
            
        }
        if(e.getSource() == cancel) {
            dashboard.dispose();
        }
        
    }
    public static void main(String args[]) {
        UserDashboard u1=new UserDashboard();
       
    }
}
