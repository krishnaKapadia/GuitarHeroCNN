import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Runs the AI that can learn to play guitar hero
 * Created by krishna kapadia on 28/03/2018.
 */
public class Main {

    public Main() {
//        Step 1: use open cv to detect the screen edges
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        System.out.println(this.getClass().getProtectionDomain());
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose a directory to save your file: ");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (jfc.getSelectedFile().isDirectory()) {
                System.out.println("You selected the directory: " + jfc.getSelectedFile());
            }
//        }

//        VideoCapture videoCapture =

        Mat color = Imgcodecs.imread(jfc.getSelectedFile().toString() + File.separator + "testImage.jpg");
        System.out.println(color);
        Mat gray = new Mat();
        Mat draw = new Mat();
        Mat wide = new Mat();
        Imgproc.cvtColor(color, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(gray, wide, 50, 150, 3, false);
        wide.convertTo(draw, CvType.CV_8U);

//        if(Imgcodecs.imwrite("", draw)) {
//            System.out.println("edge detected");
//        }
//
//        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
//        System.out.println("mat = " + mat.dump());
        showResult(draw);
    }

    /**
     * Shows the image in a new window
     * @param img, image to display
     */
    public static void showResult(Mat img) {
        Imgproc.resize(img, img, new Size(640, 480));
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            JFrame frame = new JFrame();
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    THIS METHOD IS A LOT FASTER BUT REQUIRES US TO DRAW IT TO A GRAPHICS OBJECT
//    public Image toBufferedImage(Mat m){
//        int type = BufferedImage.TYPE_BYTE_GRAY;
//        if ( m.channels() > 1 ) {
//            type = BufferedImage.TYPE_3BYTE_BGR;
//        }
//        int bufferSize = m.channels()*m.cols()*m.rows();
//        byte [] b = new byte[bufferSize];
//        m.get(0,0,b); // get all the pixels
//        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
//        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//        System.arraycopy(b, 0, targetPixels, 0, b.length);
//        return image;
//
//    }



    public static void main(String[] args) {
        new Main();
    }

}
