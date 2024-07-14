package faceattendancesystem;

import javax.swing.JOptionPane;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.ResultSet;

/**
 *
 * @author shree
 */
public class FaceRecognizer {
    
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
     Connection c1;
     Statement s1;
    public void recognize(String prn,String _class,String branch) {
        CascadeClassifier faceCascade = new CascadeClassifier("haar/haarcascade_frontalface_default.xml");

        LBPHFaceRecognizer lbphRecognizer = LBPHFaceRecognizer.create();

        lbphRecognizer.read("users/"+prn+"_model.xml");
        
         Mat frame = captureImageFromCamera();         
        
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        
        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(grayFrame, faces, 1.1, 3, Objdetect.CASCADE_SCALE_IMAGE, new Size(30, 30), new Size());
        
        for (Rect rect : faces.toArray()) {
            Mat detectedFace = new Mat(grayFrame, rect);
            
            Size size = new Size(200, 200);
            Imgproc.resize(detectedFace, detectedFace, size);
            int[] predictedLabel = new int[1];
            double[] confidence = new double[1];
            lbphRecognizer.predict(detectedFace, predictedLabel, confidence);
            
	    double pabel = confidence[0];
            if (pabel>60 ) {
                System.out.println("Face is not valid. Deny access."+pabel);
                JOptionPane.showMessageDialog(null, "Invalid Face");
                return;
            } else { 
                LocalDate currentDate = LocalDate.now();
                java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        c1=DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql","root","root");
                        s1=c1.createStatement();
                        s1.execute("USE faceattendancesystem;");
                        s1.execute("INSERT INTO "+_class+" VALUES("+prn+",'"+branch+"','"+sqlDate.toString()+"',CURRENT_TIMESTAMP);");
                     /*   ResultSet r1=s1.executeQuery("SELECT prn FROM first_year where date='2024-02-28' AND (time >='2024-02-28 19:10:30' AND time <='2024-02-28 19:10:50');");
                        r1.next();
                        System.out.println(r1.getInt(1));*/
                    } catch (Exception ex) 
                      {
                        JOptionPane.showMessageDialog(null, ex);
                      }
                JOptionPane.showMessageDialog(null, "Valid face .Attendace Granted"+pabel);
            }
        }
    }
    private static Mat captureImageFromCamera() {
        
        CameraScreen f1=new CameraScreen();
        Mat m1=f1.startCamera();
        return m1;
    }
 
    
}

