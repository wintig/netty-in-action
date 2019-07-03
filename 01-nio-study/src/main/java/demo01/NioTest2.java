package demo01;


import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest2 {

    public static void main(String[] args) throws Exception {

        FileOutputStream fileOutputStream = new FileOutputStream("nio-study.txt");
        FileChannel fileChannel = fileOutputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(512);

        byte[] message = "hello world".getBytes();

        for (int i = 0; i < message.length; i++) {
            buffer.put(message[i]);
        }

        buffer.flip();

        fileChannel.write(buffer);
        fileOutputStream.close();
    }

}
