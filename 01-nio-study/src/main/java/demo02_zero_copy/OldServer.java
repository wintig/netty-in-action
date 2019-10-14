package demo02_zero_copy;


import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class OldServer {

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(8899);

        while (true) {

            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            try {

                byte[] byteArray = new byte[4096];

                // 获取客户端发来的数据
                while (true) {

                    // 实际读到的数量
                    int readCount = dataInputStream.read(byteArray, 0, byteArray.length);
                    if (readCount == -1) {
                        break;
                    }
                }

            } catch (Exception e) {

            }

        }

    }

}
