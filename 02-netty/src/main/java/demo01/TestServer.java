package demo01;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {

    public static void main(String[] args) throws InterruptedException {

        // 从客户端接受连接，但是不对连接进行任何处理，丢给workerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        // 处理客户端连接，返回
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 用于启动服务端的类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //.handler()  handler针对的是bossGroup


                    /**
                     * 连接到来的时候，首先交给bossGroup。bossGroup将连接转发给workerGroup
                     * workerGroup具体是怎么处理的，就是“TestServerInitializer”我们创建的方法
                     */
                    .childHandler(new TestServerInitializer());    // 自己的请求处理器

            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 优雅的关闭，因为这里都是多线程，这里有很多的连接
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
