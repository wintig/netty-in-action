package v003;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Netty服务启动流程，一句话说就是：创建一个引导类，然后给他指定线程模型，IO模型，链接读写处理逻辑，绑定端口之后服务就启动起来了
 *
 *
 */
public class NettyServer {


    public static void main(String[] args) {

        // 表示监听端口，accept新连接的线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();

        // 表示处理每一条链接的数据读写线程组
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        // 启动线程组
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap
                .group(bossGroup, workGroup)    // 给引导类配置量大线程组
                .channel(NioServerSocketChannel.class)      // 指定服务端io模型为NIO

                // 指定处理新连接数据的读写处理逻辑
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {


                    }
                });

        // 用于指定在服务端启动过程的一些逻辑
        serverBootstrap.handler(new ChannelInitializer<NioServerSocketChannel>() {
            @Override
            protected void initChannel(NioServerSocketChannel nioServerSocketChannel) throws Exception {
                System.out.println("初始化资源。。。");
                System.out.println("读取配置文件。。。");
                System.out.println("服务端启动中。。。");
            }
        });

        // 指定自定义属性
        serverBootstrap.attr(AttributeKey.newInstance("serverName"), "nettyServer");


        // 设置一些TCP底层相关的属性
        serverBootstrap
                .childOption(ChannelOption.SO_KEEPALIVE, true)      // 是否开启TCP底层心跳机制
                .childOption(ChannelOption.TCP_NODELAY, true);

        // 除了给每个链接设置这一系列属性之外，我们还可以给服务端channel设置一些属性
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);

        bind(serverBootstrap, 1000);



    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {

        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {

                if (future.isSuccess()) {
                    System.out.println("端口["+port+"]绑定成功！");
                } else {
                    System.out.println("端口["+port+"]绑定失败！");
                    bind(serverBootstrap, port + 1);
                }

            }
        });

    }

}
