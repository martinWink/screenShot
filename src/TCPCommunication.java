import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPCommunication {

    private String targetIP;

    public TCPCommunication(String targetIP) {
        this.targetIP = targetIP;
    }

    public String sendCommand(BufferedImage command) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ImageIO.write(command, "jpg", baos);
        baos.flush();

        byte[] bytes = baos.toByteArray();
        baos.close();

        Socket s = new Socket(targetIP, 5000);

        OutputStream  pr = s.getOutputStream();
        DataOutputStream dos = new DataOutputStream(pr);
        dos.writeInt(bytes.length);
        dos.write(bytes, 0, bytes.length);

//        dos.close();
//        pr.close();

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        return bf.readLine();
    }

}
