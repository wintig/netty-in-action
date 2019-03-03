import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockingServer {

    // 对外暴露的服务端口号
    private static final int portNumber = 8080;

    public static void main(String[] args) throws IOException {

        // 创建一个新的ServerSocket，用于监听端口上的链接请求
        ServerSocket serverSocket = new ServerSocket(portNumber);

        // 调用accept方法后会阻塞，直到一个连接建立
        Socket clientSocket = serverSocket.accept();

        // 对这些流对象都派生与该套接字流对象
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String request;
        String response;

        // 开始循环处理流对象
        while ((request = in.readLine()) != null) {

            if ("Done".equals(request)) {
                break;
            }

            response = processRequest(request);
            out.println(response);
        }

    }

    private static String processRequest(String request) {

        return "";
    }

}
