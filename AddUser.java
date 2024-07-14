
package faceattendancesystem;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
public class AddUser implements ActionListener {
    JFrame panel;
    JPanel design_panel;
    JTextField name,prn,folder_path;
    JButton add,cancel,open;
    JLabel newuser_txt,name_txt,email_txt,branch_txt,class_txt,folder_txt,details;
    JComboBox branch,_class;
    
    AddUser() {
        panel=new JFrame("Face Attendance System");
        panel.setSize(530,400);
        panel.setLayout(null);
        panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.getContentPane().setBackground(Color.WHITE);
        
        design_panel = new JPanel();
        design_panel.setSize(530,100);
        design_panel.setBounds(0,0,530,70);
        design_panel.setBackground(new Color(199, 200, 204));
        
        setLabel();
        setTextFields();
        setButtons();
        panel.setVisible(true);
    }
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == cancel) {
            panel.dispose();
        }
        if(ae.getSource() == add) {
            if(name.getText().equals("") || prn.getText().equals("") || folder_path.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Enter details correctly");
                return;
            }
            File f1=new File("users/user_details.txt");
            try{
            Scanner sc=new Scanner(f1);
            String prns="";
            while(sc.hasNextLine()) {
                prns=prns+sc.nextLine();
            }
            String temp=prn.getText();
            if(prns.contains(temp))
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
                    File personFolder = new File(folder_path.getText());

                    for (File imageFile : personFolder.listFiles()) {
                        Mat img = Imgcodecs.imread(imageFile.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
                        images.add(img);
                        labels.add(personID);  
                    }
                    MatOfInt labelsMat = new MatOfInt();
                    labelsMat.fromList(labels);
                    lbphRecognizer.train(images, labelsMat);
                    lbphRecognizer.save("users/"+prn.getText()+"_model.xml");
                }
                JOptionPane.showMessageDialog(null, "User addes successfully");  
            }
            }catch(Exception e) { System.out.println(e); }
        }
        if(ae.getSource() == open) {
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
                return;
            }
      
            folder_path.setText(fileChooser.getSelectedFile().getAbsolutePath());
        } 
        else {
            JOptionPane.showMessageDialog(null, "No folder selected");
        }
        
        }
        
    }
    
    private void setLabel() {
        Font f1=new Font("Times New Roman",Font.BOLD,24);
        newuser_txt=new JLabel("New User");
        newuser_txt.setBounds(30,30,100,30);
        newuser_txt.setFont(f1);
        panel.add(newuser_txt);
         panel.add(design_panel);
        
        Font f2=new Font("Times New Roman",Font.BOLD,14);
        name_txt=new JLabel("Name :");
        name_txt.setBounds(50,100,100,30);
        name_txt.setFont(f2);
        
        email_txt=new JLabel("PRN :");
        email_txt.setBounds(50,140,100,30);
        email_txt.setFont(f2);
        
        branch_txt=new JLabel("Department :");
        branch_txt.setBounds(50,180,100,30);
        branch_txt.setFont(f2);
        
        class_txt=new JLabel("Class :");
        class_txt.setBounds(50,220,90,30);
        class_txt.setFont(f2);
        
         folder_txt=new JLabel("images path :");
        folder_txt.setBounds(50,260,90,30);
        folder_txt.setFont(f2);
        panel.add(folder_txt);
        
        Font f3=new Font("Airal",Font.PLAIN,9);
        details=new JLabel("( please select an folder with min images: 20 )");
        details.setBounds(140,290,200,20);
        details.setFont(f3);
        panel.add(details);
    }
    private void setTextFields() {
        name=new JTextField();
        name.setBounds(170,100,140,25);
        panel.add(name);
        
        prn=new JTextField();
        prn.setBounds(170,140,140,25);
        panel.add(prn);
        
        String branches[]={ "CSE","IT","MECHANICAL","CIVIL","ELECTRICAL","ENTC"};
        branch=new JComboBox(branches);
        branch.setBounds(170,180,140,25);
        panel.add(branch);
        
        String classes[]={"First Year","Second Year","Third Year"};
        _class=new JComboBox(classes);
        _class.setBounds(170,224,140,25);
        panel.add(_class);
        
        folder_path=new JTextField();
        folder_path.setBounds(170,265,140,25);
        folder_path.setEditable(false);
        panel.add(folder_path);
        
    }
    private void setButtons() {
        add=new JButton("Add");
        add.setForeground(Color.WHITE);
        add.setBackground(Color.BLACK);
        add.setBounds(200,315,100,30);
        add.addActionListener(this);
        cancel=new JButton("Cancel");
        cancel.setForeground(Color.BLACK);
        cancel.setBackground(new Color(199, 200, 204));
        cancel.setBounds(90,315,90,30);
        cancel.addActionListener(this);
        open=new JButton();
        ImageIcon im1=new ImageIcon("images/open_file.jpg");
        open.setIcon(im1);
        open.setBounds(310,265,25,25);
        open.addActionListener(this);
        panel.add(name_txt);
        panel.add(email_txt);
        panel.add(branch_txt);
        panel.add(class_txt);
        panel.add(open);
        panel.add(add);
        panel.add(cancel);
        
    }
    public static void main(String args[]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        AddUser a=new AddUser();
     
    }
}
