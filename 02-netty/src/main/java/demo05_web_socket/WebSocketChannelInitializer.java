package demo05_web_socket;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel>{

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());


        // 将一个http请求聚合成一个
        pipeline.addLast(new HttpObjectAggregator(8192));

        /**
         *
         * webSocket的链接是 ws://server:port/context_path
         *
         * ws://localhost:9999/ws  这里的ws是后面这个ws，客户端发起链接
         *
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));


        pipeline.addLast(new TextWebSocketHanlder());

    }

}
