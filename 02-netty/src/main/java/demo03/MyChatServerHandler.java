package demo03;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class MyChatServerHandler extends SimpleChannelInboundHandler<String>{

    // 用来保存channel对象
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        Channel channel = ctx.channel();

        channelGroup.forEach(ch -> {
            // 不是自己
            if (channel != ch) {
                ch.writeAndFlush(channel.remoteAddress() + "发送的消息：" + msg + "\n");
            } else {
                ch.writeAndFlush("【自己】" + "发送的消息：" + msg + "\n");
            }
        });
    }

    /**
     * 服务器和客户端建立了链接
     *
     * 服务器和客户端每建立一个连接，就表示一个channel建立了。如果要进行广播保存channel就好了
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        // 消息广播
        channelGroup.writeAndFlush("【服务器】- " + channel.remoteAddress() + " 加入了聊天室" + "\n");
        channelGroup.add(channel);
    }

    /**
     * 表示连接断开
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        // 消息广播
        channelGroup.writeAndFlush("【服务器】- " + channel.remoteAddress() + " 退出了聊天室" + "\n");
        System.out.println(channelGroup.size());
        // 当连接断开的时候，这一行其实会自动调用
        // channelGroup.remove(channel);
    }

    /**
     * 连接出于活动状态
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 上线了！");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 下线了！");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
