import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public class Server extends JFrame{

    private static boolean draw = false;
    static BufferedImage bi = null;

    static AudioInputStream ais;
    static AudioFormat format;
    static float sampleRate = 44100.0f;

    static DataLine.Info dataLineInfo;
    static SourceDataLine sourceDataLine;

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

    private void connectImage() {
        Thread t = new Thread() {
            @Override
            public void  run() {
                try {
                    DatagramSocket ds = new DatagramSocket(5000);
                    ds.getReceiveBufferSize();
                    byte[] receivedData = new byte[ds.getReceiveBufferSize()];
                    DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);
                    byte[] x = new byte[4];
                    byte[] y = new byte[4];
                    byte[] is = new byte[4];
                    int imageSize;
                    while (true) {
                        ds.receive(receivePacket);

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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public void connectAudio() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    DatagramSocket serverSocket = new DatagramSocket(6000);
                    byte[] receiveData = new byte[4096];

                    format = new AudioFormat(sampleRate, 16, 2, true, false);
                    dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
                    sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                    sourceDataLine.open(format);
                    sourceDataLine.start();

                    DatagramPacket receiveAudioPacket = new DatagramPacket(receiveData, receiveData.length);
                    ByteArrayInputStream baiss = new ByteArrayInputStream(receiveAudioPacket.getData());
                    while(true){
                        serverSocket.receive(receiveAudioPacket);
                        ais = new AudioInputStream(baiss, format, receiveAudioPacket.getLength());
                        reproduceAudio(receiveAudioPacket.getData());
                    }
                } catch (IOException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void reproduceAudio(byte soundbytes[]) {
        try {
            System.out.println("At the speaker");
            sourceDataLine.write(soundbytes, 0, soundbytes.length);
        } catch (Exception e) {
            System.out.println("Not working in speakers...");
            e.printStackTrace();
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

    public static void main(String[] args) {
        Server s = new Server();
        s.setVisible(true);
        s.initComponents();
        s.connectImage();
        s.connectAudio();

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
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
