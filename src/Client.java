import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class Client extends javax.swing.JFrame {
    private boolean capturar = false;
    TCPCommunication tcpComImage;
    TCPCommunication tcpComSound;
    String targetIP;

    public Client() throws SocketException {
        initComponents();
        targetIP = "192.168.0.15";
        this.tcpComImage = new TCPCommunication(targetIP, 5000);
        this.tcpComSound = new TCPCommunication(targetIP, 6000);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        btCapturar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btCapturar.setText("Capturar");
        btCapturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCapturarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(btCapturar)
                                .addGap(0, 641, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 481, Short.MAX_VALUE)
                                .addComponent(btCapturar))
        );

        pack();
    }// </editor-fold>

    private void btCapturarActionPerformed(java.awt.event.ActionEvent evt) {
        capturar = true;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        if (capturar) {
            this.shareMic();
            try {
                for(int x = 0; x < 1537; x += 384){
                    for(int y = 0; y < 865; y += 216){
                        int finalX = x;
                        int finalY = y;
                        Thread t1 = new Thread(){
                            @Override
                            public void run() {
                                BufferedImage bi = null;
                                Robot r;
                                int telaX = 384;
                                int telaY = 216;
                                while (true) {
                                    try {
                                        r = new Robot();
                                        BufferedImage tempBi = r.createScreenCapture(new Rectangle(finalX, finalY, telaX, telaY));
                                        if(tempBi != bi) {
                                            tcpComImage.sendImageCommand(tempBi, finalX, finalY);
                                            bi = tempBi; // by block
                                            sleep(360);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                        t1.start();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        capturar = false;
    }

    public void shareMic() {

        Thread t = new Thread(){
            @Override
            public void run() {

                try {
                    TargetDataLine line;

                    AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
                    float rate = 44100.0f;
                    int channels = 2;
                    int sampleSize = 16;
                    boolean bigEndian = false;

                    AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);

                    DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                    if (!AudioSystem.isLineSupported(info)) {
                        System.out.println("Line matching " + info + " not supported.");
                        return;
                    }

                    line = (TargetDataLine) AudioSystem.getLine(info);
                    line.open(format);
                    line.start();

                    byte[] data = new byte[4096];

                    while(true) {
                        line.read(data, 0, data.length);
                        tcpComSound.sendSoundComand(data);
                    }
                } catch (LineUnavailableException | IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Client().setVisible(true);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btCapturar;
    // End of variables declaration
}
