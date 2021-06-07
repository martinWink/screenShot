import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.SocketException;

public class Client extends javax.swing.JFrame {
    private boolean capturar = false;
    TCPCommunication tcpCom;
    String targetIP;

    public Client() throws SocketException {
        initComponents();
        targetIP = "192.168.0.15";
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
            try {
                for(int x = 0; x <= 1536; x += 384){
                    for(int y = 0; y <= 864; y += 216){
                        int finalX = x;
                        int finalY = y;
                        Thread t1 = new Thread(){
                            @Override
                            public void run() {
                                while (true) {
                                    Robot r;
                                    try {
                                        r = new Robot();
                                        int telaX = 384;
                                        int telaY = 216;
                                        BufferedImage bi = r.createScreenCapture(new Rectangle(finalX, finalY, telaX, telaY)); // by block
                                        tcpCom.sendCommand(bi, finalX, finalY);
                                        Thread.sleep(300);
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
