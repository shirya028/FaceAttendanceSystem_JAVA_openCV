
package faceattendancesystem;

/**
 *
 * @author shree
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author shree
 */

public class CameraScreen extends JFrame {

	private JLabel label;
	private ImageIcon icon;
	private VideoCapture capture;
	private Mat image;
	private boolean clicked = false;

	public CameraScreen() {
		setLayout(null);

		label = new JLabel();
		label.setBounds(0, 0, 640, 480);
		add(label);

		JButton btn = new JButton("capture");
                
		btn.setBounds(280, 480, 80, 40);
		add(btn);
		
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clicked = true;
			}
		});
                
		
		
		setFocusable(false);
		setSize(640, 560);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	

	public Mat startCamera() {

		capture = new VideoCapture(0);
		image = new Mat();
		byte[] imageData;
		while (true) {
			capture.read(image);
                       // Core.flip(image, image, 1);

			final MatOfByte buf = new MatOfByte();
			Imgcodecs.imencode(".jpg", image, buf);

			imageData = buf.toArray();

			icon = new ImageIcon(imageData);
			label.setIcon(icon);
			System.out.println(image.cols());
			if (clicked) {
				
                                capture.release();
				clicked = false;
                                return image;
                                
			}
                        
		}
	}

}
