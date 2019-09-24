package demo01;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 *
 * Scattering: 分散
 * Gathering：收集
 *
 */
public class NioTest11_Scattering与Gathering {


    /**
     * 在我们读写的时候，我们通常会创建一个很大的buffer，这当然是没问题的。
     *
     * 但是这一个buffer中的信息，似乎就不是那么好的进行“分类”，例如有时候我们自定义协议。
     * 我们要求第一个请求过来的头是10个字节，第二个是4个字节，第三个是body他的长度是可变的
     *
     * 那么这时候我们就可以直接用一个数组来进行接收，如果第一个数组读完了后，就接着读第二个数组
     * 这样的话，buffer里面的数据就更加专一。
     *
     * test:
     * nc localhost 8899
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(8899);
        serverSocketChannel.socket().bind(address);

        int messageLength = 2 + 3 + 4;

        ByteBuffer[] buffers = new ByteBuffer[3];
        buffers[0] = ByteBuffer.allocate(2);
        buffers[1] = ByteBuffer.allocate(3);
        buffers[2] = ByteBuffer.allocate(4);

        SocketChannel socketChannel = serverSocketChannel.accept();

        while (true) {
            int byteRead = 0;

            while (byteRead < messageLength) {
                long r = socketChannel.read(buffers);
                byteRead += r;

                System.out.println("bytesRead: " + byteRead);

                Arrays.asList(buffers).stream().
                        map(e -> e.toString()).
                        forEach(System.out::println);

            }


            Arrays.asList(buffers).forEach(buffer -> {
                buffer.flip();
            });

            long byteWritten = 0;
            while (byteWritten < messageLength) {
                long r = socketChannel.write(buffers);
                byteWritten += r;
            }

            Arrays.asList(buffers).forEach(buffer -> {
                buffer.clear();
            });

            System.out.println("byteRead: " + byteRead + ", bytesWritten: " + byteWritten +
            ", messageLength: " + messageLength);

        }

    }

}
