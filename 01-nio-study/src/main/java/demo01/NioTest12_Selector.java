package demo01;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioTest12_Selector {

    public static void main(String[] args) throws IOException {

        int[] ports = new int[5];

        ports[0] = 5000;
        ports[1] = 5001;
        ports[2] = 5002;
        ports[3] = 5003;
        ports[4] = 5004;

        Selector selector = Selector.open();

        for (int i = 0; i < ports.length; i++) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            // 调整阻塞模式
            serverSocketChannel.configureBlocking(false);

            // 与这个通道所关联的serverSocket
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(ports[i]);
            serverSocket.bind(address);

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("监听端口：" + ports[i]);
        }

        while (true) {

            int number = selector.select();
            // 返回键的数量
            System.out.println("number：" + number);

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionKeys：" + selectionKeys);

            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                // 如果是建立连接
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);

                    // 当前连接已经建立了，然后注册读
                    socketChannel.register(selector, SelectionKey.OP_READ);

                    // 连接移出
                    iterator.remove();
                    System.out.println("获取客户端连接：" + socketChannel);

                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    int bytesRead = 0;

                    while (true) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                        byteBuffer.clear();
                        int read = socketChannel.read(byteBuffer);

                        if (read <= 0) {
                            break;
                        }

                        byteBuffer.flip();
                        socketChannel.write(byteBuffer);

                        bytesRead += read;
                    }

                    System.out.println("读取：" + bytesRead + ", 来源自" + socketChannel);
                    iterator.remove();
                }

            }

        }




    }

}
