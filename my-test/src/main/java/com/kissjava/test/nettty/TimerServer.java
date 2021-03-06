package com.kissjava.test.nettty;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimerServer {
	
	public void bind(int port){
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			 .channel(NioServerSocketChannel.class)
			 .option(ChannelOption.SO_BACKLOG, 1024)
			 .childHandler(new ChildChannelHander());
			
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		}catch(Exception e){
		}finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	private class ChildChannelHander extends ChannelInitializer<SocketChannel>{

		@Override
		protected void initChannel(SocketChannel channel) throws Exception {
			channel.pipeline().addLast(new TimerServerHandler());
		}
	}
	private class TimerServerHandler extends ChannelHandlerAdapter{
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			ctx.close();
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			ctx.flush();
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg){
			ByteBuf buf = (ByteBuf)msg;
			byte[] req = new byte[buf.readableBytes()];
			buf.readBytes(req);
			try {
				String body = new String(req, "UTF-8");
				System.out.println("Time server receive order : " + body);
				String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date().toString():"BAD ORDER";
				ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
				ctx.write(resp);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new TimerServer().bind(8080);
	}
}
