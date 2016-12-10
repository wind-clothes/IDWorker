package com.hunk.idworker.handler;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpResponseStatus.TEMPORARY_REDIRECT;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hunk.idworker.util.Constants;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;

/**
 * <pre>
 * 单个线程的handler处理机制
 * </pre>
 * 
 * @author: xiongchengwei
 * @date: 2016年3月14日 下午3:08:04
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpRequest> {

  private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

  private Map<String, HttpResponseHandler> allHandlers = new HashMap<String, HttpResponseHandler>();

  public HttpServerHandler(Map<String, HttpResponseHandler> allHandlers) {
    if (this.allHandlers.isEmpty()) {
      this.allHandlers = allHandlers;
    }
  }

  @Override
  protected void messageReceived(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
    if (HttpHeaderUtil.is100ContinueExpected(msg)) {
      ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
    }
    // 获取相关请求参数
    // Map<String, Object> params = HttpRequestParamParse.parse(msg);
    QueryStringDecoder queryStringDecoder = new QueryStringDecoder(msg.uri());
    String result = null;
    if (allHandlers.containsKey(queryStringDecoder.path())) {
      HttpResponseHandler newHandler = allHandlers.get(queryStringDecoder.path());
      result = newHandler.reponse();
    } else {
      responseOnFailure(ctx, msg);
      return;
    }
    responseOnSuccess(ctx, msg, result);
    if (logger.isDebugEnabled()) {
      logger.info("handler is end");
    }
  }

  private void responseOnFailure(ChannelHandlerContext ctx, HttpRequest request) {
    FullHttpResponse response;
    if (!request.decoderResult().isSuccess()) {
      response = new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST,
          Unpooled.copiedBuffer("params is error", CharsetUtil.UTF_8));
    } else {
      response = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND,
          Unpooled.copiedBuffer("resources is not found", CharsetUtil.UTF_8));
    }
    processResponse(false, response, ctx);
  }

  public void writeResponseOnRedirect(ChannelHandlerContext ctx, String redirectURL) {
    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, (TEMPORARY_REDIRECT));
    response.headers().set(HttpHeaderNames.LOCATION, redirectURL);
    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
  }


  private void responseOnSuccess(ChannelHandlerContext ctx, HttpRequest request, String result) {
    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
        (request.decoderResult().isSuccess() ? OK : BAD_REQUEST),
        Unpooled.copiedBuffer(result, CharsetUtil.UTF_8));
    boolean keepAlive = HttpHeaderUtil.isKeepAlive(request);
    processResponse(keepAlive, response, ctx);
  }

  private void processResponse(boolean keepAlive, FullHttpResponse response,
      ChannelHandlerContext ctx) {
    if (!response.status().equals(TEMPORARY_REDIRECT)) {
      response.headers().set(HttpHeaderNames.CONTENT_TYPE, Constants.APPLICATION_JSON);
      response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
    }
    response.headers().setObject(HttpHeaderNames.DATE, Calendar.getInstance().getTime());
    if (!keepAlive) {
      ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    } else {
      response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
      ctx.writeAndFlush(response);
    }
  }
}
