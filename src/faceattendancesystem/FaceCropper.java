
package faceattendancesystem;

import org.opencv.objdetect.CascadeClassifier;
import java.io.File;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author shree
 */
public class FaceCropper {
    
    CascadeClassifier cf;
    private String nm;
    public FaceCropper() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        cf=new CascadeClassifier("haar/haarcascade_frontalface_default.xml");
    }
    public int cropNsave(String path,String name) {
        nm=name;
        File folder=new File(path+"/cropped_imgs");
        if(folder.mkdir()) {
            File temp=new File(path);
            File[] files = temp.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".jpg")) {
                       
				Mat selectedImage=Imgcodecs.imread(file.getAbsolutePath());
                                cropAndSave(selectedImage,path);
                                
			}
                }
            }
            return 0;
        }
        
        return -1;
    }
    
    private void cropAndSave(Mat img,String path) {
        MatOfRect detection= new MatOfRect();
        cf.detectMultiScale(img, detection,1.1,3,0,new Size(50,20),new Size());
        int i=1;
        for(Rect rect : detection.toArray()) {             
            Rect roi = new Rect(new org.opencv.core.Point(rect.x, rect.y-2), new org.opencv.core.Point(rect.x + rect.width,rect.y + rect.height+5));
            Mat croppedImage = new Mat(img, roi);
            String name=nm+(i++); 
            Imgcodecs.imwrite(path+"/cropped_imgs/"+name+".jpg", croppedImage);
           
        }
    }
    public static void main(String args[]) {
        FaceCropper c=new FaceCropper();
    }
    
}
