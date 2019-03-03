package v001;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 客户端每隔两秒发送一个带有时间戳的 "hello world" 给服务端，服务端收到之后打印。
 */
public class IOServer {

    public static void main(String[] args) throws Exception {

        System.out.println("正在启动服务器。。。");
        ServerSocket serverSocket = new ServerSocket(8000);

        // 新连接接收线程
        new Thread(() -> {

            System.out.println("服务器已启动。。。");

            while (true) {

                try {

                    // 阻塞方式获取新的连接
                    Socket socket = serverSocket.accept();

                    // 每一个新的连接都创建一个线程，负责读取数据
                    new Thread(() -> {

                        try {
                            int len;
                            byte[] data = new byte[1024];
                            InputStream inputStream = socket.getInputStream();

                            // 字节流的方式读取数据
                            while ((len = inputStream.read(data)) != -1 ) {
                                System.out.println(new String(data, 0, len));
                            }

                        } catch (IOException e) {

                        }

                    }).start();


                } catch (IOException e) {

                }

            }

        }).start();

    }


}
