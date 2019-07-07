package demo01;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest4 {

    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("input.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("output.txt");

        FileChannel inputChannel = fileInputStream.getChannel();
        FileChannel outputChannel = fileOutputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(512);

        while (true) {

            /**
             * 清空缓冲区，如果不清空，就会发生死循环
             *
             * 同时read永远就只会返回0，这是因为当把文件读到buffer中后
             * 后flip切换模式这时 position = 0，limit等于读取到的具体文件大小。
             * 然后channel把缓冲区的文件写入到文件中后，这是position=limit
             * 如果不进行clear重置的话，就说明当前buffer无法写入数据了，无法写入数据的时候就会返回0
             *
             */
            buffer.clear();

            /**
             * 0：buffer无法写入数据
             * -1：数据读取完毕
             * >-1 : 读取的实际字节数量
             */
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
