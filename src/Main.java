import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.Vector;

/**
 * Runs the AI that can learn to play guitar hero
 * Created by krishna kapadia on 28/03/2018.
 */
public class Main {
    JFrame frame = new JFrame();

    public Main() {
//        Step 1: use open cv to detect the screen edges
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Rectangle screenRect = new Rectangle(600, 600);

        while (true) {
            try {
//                Reads screen and passes the output to open cv
                BufferedImage capture = new Robot().createScreenCapture(screenRect);
                Mat color = convertImageToMat(capture);
                System.out.println(color);
                Mat gray = new Mat();
                Mat draw = new Mat();
                Mat edges = new Mat();
                Imgproc.cvtColor(color, gray, Imgproc.COLOR_BGR2GRAY);
                Imgproc.Canny(gray, edges, 50, 150, 3, false);
                Mat circles = new Mat();
                Vector<Mat> circlesList = new Vector<Mat>();

                Imgproc.HoughCircles(edges, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 60, 200, 20, 30, 0);



                edges.convertTo(draw, CvType.CV_8U);



//                Display the output to the JFrame
                showResult(draw);
            } catch (AWTException | IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Convert  buffered image object to a Mat so tha open cv can read it.
     * @param image, to convert
     * @return a Mat representing the passed in buffered image
     * @throws IOException
     */
    public static Mat convertImageToMat(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
    }

    /**
     * Shows the image in a new window
     * @param img, image to display
     */
    public void showResult(Mat img) {
        Imgproc.resize(img, img, new Size(640, 480));
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    THIS METHOD IS A LOT FASTER BUT REQUIRES US TO DRAW IT TO A GRAPHICS OBJECT
//    public BufferedImage toBufferedImage(Mat m){
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
//    }



    public static void main(String[] args) {
        new Main();
    }

}
