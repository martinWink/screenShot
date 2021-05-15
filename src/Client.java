import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Client extends javax.swing.JFrame {
    private boolean capturar = false;
    TCPCommunication tcpCom;
    String targetIP;
    String response;

    public Client() {
        initComponents();
        targetIP = "172.30.74.137";
        this.tcpCom = new TCPCommunication(targetIP);
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
            while(true) {
                try {
                    int scale = 2;
                    Robot r = new Robot();

                    int telaX = 100;
                    int telaY = 100;
                    BufferedImage bi = r.createScreenCapture(new Rectangle(telaX, telaY)); // by block
                    response = tcpCom.sendCommand(bi);
                    System.out.println(response);
                    Thread.sleep(67);
//                    for (int y = 0; y < telaY; y++) {
//                        for (int x = 0; x < telaX; x++) {
//
//                            g.setColor(new Color(bi.getRGB(x, y)));
//
//                            //ARGB                        0       0      0        0
//                            //int cor = bi.getRGB(x, y) & 0b00000000111111100000000011111111;
//                            //g.setColor(new Color(cor));
//
//                            g.drawRect(100 + x * scale, 100 + y * scale, scale, scale);
//
//                        }
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        capturar = false;
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
                new Client().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btCapturar;
    // End of variables declaration
}
