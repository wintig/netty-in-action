package demo01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioClient {

    public static void main(String[] args) throws Exception {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();

        // 这里监听的事件是建立连接，因为我们是客户端么
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8899));

        while (true) {

            selector.select();
            Set<SelectionKey> keySet = selector.selectedKeys();

            for (SelectionKey selectionKey : keySet) {

                // 如果已经连接上了
                if (selectionKey.isConnectable()) {

                    SocketChannel client = (SocketChannel) selectionKey.channel();

                    // 查看一个连接是否正在挂起
                    if (client.isConnectionPending()) {
                        client.finishConnect(); // 这时候连接才是真正建立的

                        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                        writeBuffer.put((LocalDateTime.now() + "连接成功").getBytes());
                        writeBuffer.flip();
                        client.write(writeBuffer);

                        // 启动一个新的线程去监听键盘的输入
                        ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
                        executorService.submit(() -> {
                           while (true) {

                               writeBuffer.clear();
                               InputStreamReader inputStreamReader = new InputStreamReader(System.in);
                               BufferedReader br = new BufferedReader(inputStreamReader);

                               String sendMessage = br.readLine();

                               writeBuffer.put(sendMessage.getBytes());
                               writeBuffer.flip();
                               client.write(writeBuffer);
                           }
                        });

                    }

                    client.register(selector, SelectionKey.OP_READ);

                } else if (selectionKey.isReadable()) {
                    SocketChannel client = (SocketChannel) selectionKey.channel();

                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int count = client.read(readBuffer);

                    if (count > 0) {
                        String receivedMessage = new String(readBuffer.array(), 0, count);
                        System.out.println(receivedMessage);
                    }
                }

                keySet.clear();

            }

        }

    }

}
