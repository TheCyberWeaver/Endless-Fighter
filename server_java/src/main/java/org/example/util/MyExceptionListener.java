package org.example.util;

import com.corundumstudio.socketio.listener.DefaultExceptionListener;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyExceptionListener extends DefaultExceptionListener {
    private static final Logger log = LoggerFactory.getLogger(MyExceptionListener.class);


    @Override
    public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        return true;
    }

}
