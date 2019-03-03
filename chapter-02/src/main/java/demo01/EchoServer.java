package demo01;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.err.println(
                    "Usage: " + EchoServer.class.getSimpleName() +
                            " <port>"
            );
        }

        int port = Integer.parseInt(args[0]);

        new EchoServer(port).start();

    }

    private void start() throws Exception {

        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();

        try {

            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)

                    // 使用指定的端口套接字地址
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);
                        }
                    });

            // 异步地绑定服务器；调用sync方法阻塞等待直到完成
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();   // 获取Channel的closeFuture，并且阻塞当前线程直到它完成
        } finally {

            // 关闭EventLoopGroup释放所有资源
            group.shutdownGracefully().sync();
        }

    }
}
