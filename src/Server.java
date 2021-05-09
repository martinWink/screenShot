import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends javax.swing.JFrame{

    private Socket socket = null;
    private static boolean draw = false;
    BufferedImage bi = null;

    public Server() {
        initComponents();
    }

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

    private static void handleConnections() throws IOException, ClassNotFoundException  {
        ServerSocket ss = new ServerSocket(5000);

        while (true) {

            Socket s = ss.accept();

            InputStream in = s.getInputStream();
            DataInputStream dis = new DataInputStream(in);

            int len = dis.readInt();

            byte[] data = new byte[len];
            dis.readFully(data);
            dis.close();
            in.close();

            InputStream ian = new ByteArrayInputStream(data);
            BufferedImage bI = ImageIO.read(ian);
            System.out.println(bI);

            PrintWriter pr = new PrintWriter(s.getOutputStream());

            pr.println("Sim"); // Comando de retorno
            pr.flush(); // limpa o buffer do PrintWriter
        }
    }

    @Override
    public void paint(Graphics g) {
        if(draw) {
            try {
                int scale = 2;
                Robot r = new Robot();

                int telaX = 500;
                int telaY = 500;
                bi = r.createScreenCapture(new Rectangle(telaX, telaY)); // by block

                for (int y = 0; y < telaY; y++) {
                    for (int x = 0; x < telaX; x++) {

                        g.setColor(new Color(bi.getRGB(x, y)));

                        //ARGB                        0       0      0        0
                        //int cor = bi.getRGB(x, y) & 0b00000000111111100000000011111111;
                        //g.setColor(new Color(cor));

                        g.drawRect(100 + x * scale, 100 + y * scale, scale, scale);

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        draw = false;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

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
                new Server().setVisible(true);
            }
        });
        handleConnections();
    }

}