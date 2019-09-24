package demo01;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *
 * 代码很简单，就是创建一个服务端，多个客户端连接到服务端。  服务端把消息转发给所有的客户端
 *
 */
public class NioServer {

    private static Map<String, SocketChannel> clientMap = new HashMap<>();

    public static void main(String[] args) throws IOException {


        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8899));

        Selector selector = Selector.open();

        // 因为一开始是没有任何连接的，所以这里就关注accept事件就好了
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {

            // 这个方法会一直阻塞再那里，直到等到我们感兴趣的事件发生的时候，返回返回事件个数
            selector.select();

            // 获得之前注册的、所有发生的事件的key
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            selectionKeys.forEach(selectionKey -> {

                final SocketChannel client;

                if (selectionKey.isAcceptable()) {

                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    try {
                        client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);

                        String key = "【" + UUID.randomUUID().toString() + "】";
                        clientMap.put(key, client);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (selectionKey.isReadable()) {

                    client = (SocketChannel) selectionKey.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                    try {
                        int count = client.read(readBuffer);
                        if (count > 0) {
                            readBuffer.flip();
                            Charset charset = Charset.forName("utf-8");
                            String receivedMessage = String.valueOf(charset.decode(readBuffer).array());
                            System.out.println(client + ": " + receivedMessage);

                            String senderKey = null;

                            for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                if (client == entry.getValue()) {
                                    senderKey = entry.getKey();
                                    break;
                                }
                            }

                            // 给没一个连接的客户端发送消息
                            for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                SocketChannel value = entry.getValue();

                                ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                                writeBuffer.put((senderKey + ": " + receivedMessage).getBytes());
                                writeBuffer.flip();

                                value.write(writeBuffer);
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            });

            selectionKeys.clear();
        }

    }

}
