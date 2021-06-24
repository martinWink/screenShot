import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class TCPCommunication {

    private String targetIP;
    private DatagramSocket senderSocket;
    private int port;

    public TCPCommunication(String targetIP, int port) throws SocketException {
        this.targetIP = targetIP;
        this.senderSocket = new DatagramSocket();
        this.port = port;
    }

    public void sendImageCommand(BufferedImage command, int xPosition, int yPosition) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ImageIO.write(command, "jpg", baos);
        baos.flush();
        ByteBuffer bbx =  ByteBuffer.allocate(4);
        ByteBuffer bby =  ByteBuffer.allocate(4);
        ByteBuffer bbis = ByteBuffer.allocate(4);
        bbx.putInt(xPosition);
        bby.putInt(yPosition);
        byte[] x = bbx.array();
        byte[] y = bby.array();
        byte[] imageData = baos.toByteArray();
        bbis.putInt(imageData.length);
        byte[] is = bbis.array();
        byte[] data = new byte[imageData.length + x.length + y.length + is.length];

        int aux = 0;
        for (int i = 0; i < x.length; i++) {
            data[i] = x[i];
            aux++;
        }
        for (int i = 0; i < y.length; i++) {
            data[aux] = y[i];
            aux++;
        }
        for (int i = 0; i < is.length; i++) {
            data[aux] = is[i];
            aux++;
        }
        for (int i = 0; i < imageData.length; i++) {
            data[aux] = imageData[i];
            aux++;
        }

        DatagramPacket send = new DatagramPacket(data, data.length, InetAddress.getByName(targetIP), port);
        senderSocket.send(send);
    }

    public void sendSoundComand(byte[] soundData) throws IOException {
        DatagramPacket send = new DatagramPacket(soundData, soundData.length, InetAddress.getByName(targetIP), port);
        senderSocket.send(send);
    }

}
