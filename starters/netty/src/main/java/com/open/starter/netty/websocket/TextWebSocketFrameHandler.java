package com.open.starter.netty.websocket;

import com.open.starter.cache.utils.RedisUtils;
import com.open.starter.netty.config.Constants;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * websocket消息的处理器
 *
 * @author open
 */
@ChannelHandler.Sharable
@Component
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public void setUserId(ChannelHandlerContext ctx, Long userId) {
        Channel channel = ctx.channel();
        // 将userId保存在Channel属性中
        channel.attr(Constants.USER_ID_KEY).set(userId);
        // 将channel关联到用户
        String channelId = channel.id().asLongText();
        RedisUtils.addCacheSet(channelId, userId);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        // 广播用户加入消息
        for (Channel channel : channelGroup) {
            channel.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 加入"));
        }
        channelGroup.add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel channel = ctx.channel();
        for (Channel ch : channelGroup) {
            if (channel != ch) {
                ch.writeAndFlush(new TextWebSocketFrame(msg.text()));
            }
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        // 广播用户离开消息
        channelGroup.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 离开"));
    }

    /**
     * 生成channel唯一标识符
     */
    private String generateChannelId(Channel channel) {
        InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
        return remoteAddress.getAddress().getHostAddress() + ":" +
                remoteAddress.getPort() + ":" +
                System.currentTimeMillis();
    }
}
