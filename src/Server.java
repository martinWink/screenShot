import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Server extends javax.swing.JFrame{

    private Socket socket = null;
    private static boolean draw = false;
    static BufferedImage bi = null;

    private int xPosition = 0, yPosition = 0;


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
        ));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 481, Short.MAX_VALUE)
        ));

        pack();
    }// </editor-fold>

    private void handleConnections() throws IOException  {
        DatagramSocket ds = new DatagramSocket(5000);
        ds.getReceiveBufferSize();

        byte[] receivedData = new byte[ds.getReceiveBufferSize()];
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);
            ds.receive(receivePacket);

            byte[] x = new byte[4];
            byte[] y = new byte[4];
            byte[] is = new byte[4];
            int imageSize;

            int aux = 0;
            for(int i = 0; i < 4; i++) {
                x[i] = receivedData[aux];
                aux++;
            }
            xPosition = ByteBuffer.wrap(x).getInt();
            for(int i = 0; i < 4; i++) {
                y[i] = receivedData[aux];
                aux++;
            }
            yPosition = ByteBuffer.wrap(y).getInt();

            for(int i = 0; i < 4; i++) {
                is[i] = receivedData[aux];
                aux++;
            }
            imageSize = ByteBuffer.wrap(is).getInt();
            byte[] image = new byte[imageSize];
            for(int i = 0; i < imageSize; i++) {
                image[i] = receivedData[aux];
                aux++;
            }

            InputStream ian = new ByteArrayInputStream(image);
            bi = ImageIO.read(ian);
            bi.setRGB(0, 0, 255);
            draw = true;
            repaint();

        }
    }

    @Override
    public void paint(Graphics g) {
        if(draw) {
            try {
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(bi, xPosition, yPosition, this);
                draw = false;
            } catch (Exception e) {
                e.printStackTrace();
                draw = false;
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server s = new Server();
        s.setVisible(true);
        s.initComponents();
        s.handleConnections();

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

}
