
package faceattendancesystem;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author shree
 */

public class AdminDashboard implements ActionListener {
    
    JFrame dashboard;
    JPanel welcomePanel,sidePanel,removeUserPanel,attendancePanel,addUserPanel;
    JButton addMember,removeMember,attendanceBtn,logoutBtn,removeBtn,addUserBtn,openFolder,submit;
    JLabel welcomeTxt,removeUserTxt,prnTxt,prnTxt1,_classTxt,_classTxt1,branchTxt,branchTxt1,addUserTxt,time,nameTxt,folderPathTxt;
    JComboBox _class,_class1,branch,branch1,times;
    JTextField prn,prn1,name,folderPath,day,month,year;
    JTextArea ta1;
    Font f1,f2;
    String branches[]={ "CSE","IT","MECHANICAL","CIVIL","ELECTRICAL","ENTC"};
    String classes[]={"first_year","second_year","third_year","final_year"};
    String t[]={"10:10 - 11:10","11:10 - 12:10","01:55 - 02:55","03:10 - 04:10","04:10 - 05:10"};
    int flag=0;
    JScrollPane sp1;
    Connection c1=null;
    Statement s1;
    ArrayList<Integer> ar=new ArrayList<>();
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    AdminDashboard() {
        dashboard=new JFrame("Face Attendance System");
        dashboard.setSize(900,520);
        dashboard.setLayout(null);
        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f1=new Font("Times New Roman",Font.BOLD,24);
        f2=new Font("Times New Roman",Font.BOLD,15);
        
        
        _classTxt=new JLabel("CLASS :");
        branchTxt=new JLabel("BRANCH :");
        prnTxt=new JLabel("PRN :");
        nameTxt=new JLabel("Name :");
        branch=new JComboBox(branches);
        _class=new JComboBox(classes);
        
        times=new JComboBox(t);
        
        prn=new JTextField();
        removeBtn=new JButton("Remove");
        
        setWhitePanels();
        setRemovePanel();
        
        time=new JLabel("time :");
        ta1=new JTextArea(50,1);
        
        ta1.setLineWrap(true);
        sp1 = new JScrollPane(ta1);
        ta1.setLineWrap(true);
        
        setAttendancePanel();
        setAddUserPanel();
        
        dashboard.add(attendancePanel); 
        //dashboard.add(addUserPanel); 
        dashboard.add(sidePanel);
        
        try {
            c1=DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql","root","root");
            s1=c1.createStatement();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        dashboard.setLocationRelativeTo(null);
        dashboard.setVisible(true);
    }
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == addMember) {
            
        }
        if(ae.getSource() == removeMember) {
            dashboard.remove(attendancePanel);
            dashboard.remove(addUserPanel);
            dashboard.add(removeUserPanel);
            dashboard.repaint();
        }
        if(ae.getSource() == attendanceBtn) {
            dashboard.remove(removeUserPanel);
            dashboard.remove(addUserPanel);
            dashboard.add(attendancePanel);
            
            dashboard.repaint();
        }
        if(ae.getSource() ==  logoutBtn){
            dashboard.dispose();
            FaceAttendanceSystem f=new FaceAttendanceSystem();
        }
        if(ae.getSource() ==  addMember)
        {
            dashboard.remove(attendancePanel);
            dashboard.remove(removeUserPanel);
            dashboard.add(addUserPanel);
            dashboard.repaint();
        }
        if(ae.getSource() == openFolder) {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Select a Folder");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            
            File selectedFolder = fileChooser.getSelectedFile();
            File[] files = selectedFolder.listFiles();
            int jpgImageCount = 0;
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".jpg")) {
                        jpgImageCount++;
                    }
                }
            }
            if(jpgImageCount<20) {
                JOptionPane.showMessageDialog(null, "Min 20 images needed for processing");
                folderPath.setText("");
                return;
            }
      
            folderPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
        } 
        else {
            folderPath.setText("");
            JOptionPane.showMessageDialog(null, "No folder selected");
        }
        
        }
        
        if(ae.getSource() == addUserBtn) {
            if(name.getText().equals("") || prn1.getText().equals("") || folderPath.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Enter details correctly");
                return;
            }
            String str="PRNS";
            ar.clear();
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                s1.execute("USE faceattendancesystem;");
                ResultSet r1=s1.executeQuery("SELECT * FROM all_users;");

                while(r1.next()) {
                   ar.add(r1.getInt(str));
                }
            }
            catch(ClassNotFoundException | SQLException se) {System.out.println(se);}
            
            if(ar.contains(Integer.parseInt(prn1.getText())))
            {
                JOptionPane.showMessageDialog(null, "User already registered");
                return;
            }
            else {
                //here we are using cropped images folder due to ,image cropping logic is not working properly
                LBPHFaceRecognizer lbphRecognizer = LBPHFaceRecognizer.create();
                List<Mat> images = new ArrayList<>();
                List<Integer> labels = new ArrayList<>();

                for (int personID = 0; personID < 1; personID++) {
                    File personFolder = new File(folderPath.getText());

                    for (File imageFile : personFolder.listFiles()) {
                        Mat img = Imgcodecs.imread(imageFile.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
                        images.add(img);
                        labels.add(personID);  
                    }
                    MatOfInt labelsMat = new MatOfInt();
                    labelsMat.fromList(labels);
                    lbphRecognizer.train(images, labelsMat);
                    lbphRecognizer.save("users/"+prn1.getText()+"_model.xml");
                }
                try {
                    s1.execute("INSERT INTO all_users VALUES ('" + prn1.getText() + "','" + name.getText() + "','" + branches[branch.getSelectedIndex()] + "','" + classes[_class.getSelectedIndex()] + "');");
                } catch (SQLException ex) {
                   System.out.println(ex);
                }
                JOptionPane.showMessageDialog(null, "User added successfully");  
            }
            
        }
        
        if(ae.getSource() == removeBtn) {
            if(prn.getText() == "") {
                JOptionPane.showMessageDialog(null, "Enter valid PRN");
                return;
            }
            String str="PRNS";
            ar.clear();
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                s1.execute("USE faceattendancesystem;");
                ResultSet r1=s1.executeQuery("SELECT * FROM all_users;");

                while(r1.next()) {
                   ar.add(r1.getInt(str));
                }
            }
            catch(ClassNotFoundException | SQLException se) {System.out.println(se);}
            
            if(!ar.contains(Integer.parseInt(prn.getText())))
            {
                JOptionPane.showMessageDialog(null, "User not found");
                ar.clear();
                return;
            }
            else {
                try {
                    s1.execute("DELETE FROM all_users WHERE PRNS="+prn.getText());
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                File f1=new File("users/"+prn.getText()+"_model.xml");
                if (f1.exists()) 
                        f1.delete();
                
                JOptionPane.showMessageDialog(null, "User deleted successully");
            }
            
        }
        
        if(ae.getSource() == submit) {
            String c1=classes[_class.getSelectedIndex()];
            String b1=branches[branch.getSelectedIndex()];
            String dd=day.getText();
            String mm=month.getText();
            String yy=year.getText();
            String time=t[times.getSelectedIndex()];
            String finalDate=yy+"-"+mm+"-"+dd;
            String startTime=time.substring(0,5)+":00";
            String endTime=time.substring(8,time.length())+":00";
            startTime=finalDate+" "+startTime;
            endTime=finalDate+" "+endTime;
            if(dd.length()!=2 || mm.length()!=2 || yy.length()!=4){
                JOptionPane.showMessageDialog(null,"Invalid date \ncorrect format :- 01/01/2000");
            }
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                ResultSet r1=s1.executeQuery("SELECT DISTINCT prn FROM "+c1+" WHERE branch='"+b1+"' AND date='"+finalDate+"' AND (time >='"+startTime+"' AND time >='"+endTime+"' );");
                ar.clear();
                while(r1.next()) {
                    ar.add(r1.getInt("prn"));
                }
                for(int i=0;i<ar.size();i++) {
                    ta1.setText(ta1.getText()+"\n"+ar.get(i));
                }
                
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(null,e);
            }
        }
        
        
    }
    
    private void setWhitePanels() {
        welcomePanel=new JPanel();
        welcomePanel.setBounds(0,0,900,70);
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setLayout(null);
        
        welcomeTxt=new JLabel("Welcome Admin");
        welcomeTxt.setBounds(10,10,200,40);
        welcomeTxt.setFont(f1);
        welcomePanel.add(welcomeTxt);
        dashboard.add(welcomePanel);
        
        sidePanel=new JPanel();
        sidePanel.setLayout(null);
        sidePanel.setBounds(0,71,150,520);
        sidePanel.setBackground(Color.WHITE);
        
        addMember=new JButton("Add User");
        addMember.setBounds(1, 20, 148, 50);
        addMember.addActionListener(this);
        sidePanel.add(addMember);
        
        
        removeMember=new JButton("Remove user");
        removeMember.setBounds(1, 75, 148, 50);
        removeMember.addActionListener(this);
        sidePanel.add(removeMember);
        
        attendanceBtn=new JButton("Attendance");
        attendanceBtn.setBounds(1, 130, 148, 50);
        attendanceBtn.addActionListener(this);
        sidePanel.add(attendanceBtn);
        
        logoutBtn=new JButton("Logout");
        logoutBtn.setBounds(1, 350, 148, 50);
        logoutBtn.addActionListener(this);
        sidePanel.add(logoutBtn);
    }
    private void setAddUserPanel() {
        addUserPanel=new JPanel();
        addUserPanel.setBounds(151,71,599,450);
        addUserPanel.setLayout(null);
        
        addUserTxt =new JLabel("ADD User");
        addUserTxt.setBounds(20,30,200,40);
        addUserTxt.setFont(f1);
        addUserPanel.add(addUserTxt);
        
        nameTxt.setBounds(60,100,70,50);
        nameTxt.setFont(f2);
        addUserPanel.add(nameTxt);
        
        prnTxt1=new JLabel("PRN :");
        prnTxt1.setBounds(60,150,70,50);
        prnTxt1.setFont(f2);
        addUserPanel.add(prnTxt1);
        
        branchTxt1=new JLabel("Branch :");
        branchTxt1.setBounds(60,200,100,50);
        branchTxt1.setFont(f2);
        addUserPanel.add(branchTxt1);
        
        _classTxt1=new JLabel("Class :");
        _classTxt1.setBounds(60,250,70,50);
        _classTxt1.setFont(f2);
        addUserPanel.add(_classTxt1);
        
        name=new JTextField();
        name.setBounds(160,115,150,25);
        addUserPanel.add(name);
        
        prn1=new JTextField();
        prn1.setBounds(160,160,150,25);
        addUserPanel.add(prn1);
        
        branch1=new JComboBox(branches);
        branch1.setBounds(160,210,150,30);
        addUserPanel.add(branch1);
        
        _class1=new JComboBox(classes);
        _class1.setBounds(160,260,150,30);
        addUserPanel.add(_class1);
        
        folderPath=new JTextField();
        folderPath.setBounds(160,310,130,30);
        folderPath.setEditable(false);
        addUserPanel.add(folderPath);
        openFolder=new JButton();
        ImageIcon im1=new ImageIcon("images/open_file.jpg");
        openFolder.setIcon(im1);
        openFolder.setBounds(290,310,25,30);
        openFolder.addActionListener(this);
        addUserPanel.add(openFolder);
        
        folderPathTxt=new JLabel("images path :");
        folderPathTxt.setBounds(60,300,100,50);
        folderPathTxt.setFont(f2);
        addUserPanel.add(folderPathTxt);
        
        addUserBtn=new JButton("Add");
        addUserBtn.setForeground(Color.WHITE);
        addUserBtn.setBackground(Color.BLACK);
        addUserBtn.setBounds(130,360,100,30);
        addUserBtn.addActionListener(this);
        addUserPanel.add(addUserBtn);
        
        
        
        
    }
    private void setRemovePanel(){
        removeUserPanel=new JPanel();
        removeUserPanel.setBounds(151,71,599,450);
        removeUserPanel.setLayout(null);
        removeUserTxt=new JLabel("Remove user");
        removeUserTxt.setFont(f1);
        removeUserTxt.setBounds(20,30,200,40);
        removeUserPanel.add(removeUserTxt);
        
        prnTxt.setFont(f2);
        prnTxt.setBounds(40,180,100,40);
        removeUserPanel.add(prnTxt);
       
        prn.setBounds(140,190,150,25);
        removeUserPanel.add(prn);
        
        
        removeBtn.setForeground(Color.WHITE);
        removeBtn.setBackground(Color.BLACK);
        removeBtn.setBounds(120,230,100,35);
        removeBtn.addActionListener(this);
        removeUserPanel.add(removeBtn);
    }
    private void setAttendancePanel() {
        attendancePanel=new JPanel();
        attendancePanel.setBounds(151,71,700,450);
        attendancePanel.setLayout(null);
        _classTxt.setFont(f2);
        _classTxt.setBounds(10,10,100,30);
        attendancePanel.add(_classTxt);
        
        _class.setBounds(80,15,150,25);
        attendancePanel.add(_class);
        
        branchTxt.setFont(f2);
        branchTxt.setBounds(250,10,100,30);
        attendancePanel.add(branchTxt);
        
        branch.setBounds(330,15,100,25);
        attendancePanel.add(branch);
        
        JLabel date=new JLabel("Date :");
        date.setFont(f2);
        date.setBounds(460,10,50,30);
        attendancePanel.add(date);
        
         day=new JTextField();
        day.setBounds(505, 15, 20, 30);
        attendancePanel.add(day);
        
        month=new JTextField();
        month.setBounds(533, 15, 20, 30);
        attendancePanel.add(month);
        
         year=new JTextField();
        year.setBounds(560, 15, 50, 30);
        attendancePanel.add(year);
        
        JLabel dateinfo=new JLabel("d        m          y");
        dateinfo.setBounds(505,43,150,20);
        attendancePanel.add(dateinfo);
        
        submit=new JButton("Submit");
        submit.setBounds(340,135,80,30);
        submit.setForeground(Color.WHITE);
        submit.setBackground(Color.BLACK);
        submit.addActionListener(this);
        attendancePanel.add(submit);
        
        time.setBounds(280,70,60,20);
        attendancePanel.add(time);
        times.setBounds(320,70,150,25);
        times.setFont(f2);
        sp1.setBounds(530,120,130,270);
        attendancePanel.add(sp1);
        attendancePanel.add(times);
    }
    
    public static void main(String args[]) {
        AdminDashboard a=new AdminDashboard();
    }
}
