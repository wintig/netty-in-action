package demo01;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest8_DirectBuffer {

    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("input.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("output.txt");

        FileChannel inputChannel = fileInputStream.getChannel();
        FileChannel outputChannel = fileOutputStream.getChannel();

        // 创建直接缓冲区
        ByteBuffer buffer = ByteBuffer.allocateDirect(512);

        while (true) {

            int read = inputChannel.read(buffer);

            System.out.println(read);

            if (-1 == read) {
                break;
            }

            buffer.flip();
            outputChannel.write(buffer);
        }

        inputChannel.close();
        outputChannel.close();

    }

}
