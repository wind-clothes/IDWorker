package com.hunk.idworker.util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;

/**
 * <pre>
 * </pre>
 * 
 * @author: xiongchengwei
 * @date: 2016年3月15日 下午6:01:04
 */
public class HttpRequestParamParse {

  private HttpRequestParamParse() {}

  public static Map<String, Object> parse(FullHttpRequest request) throws IOException {
    Map<String, Object> result = new LinkedHashMap<String, Object>();
    String uri = request.uri();
    HttpMethod method = request.method();
    if (HttpMethod.GET.equals(method)) {
      QueryStringDecoder query = new QueryStringDecoder(uri);
      query.parameters().entrySet().forEach(entry -> {
        //取第一个元素 ,TODO
        result.put(entry.getKey(), entry.getValue().get(0));
      });
    } else if (HttpMethod.POST.equals(method)) {
      HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
      decoder.offer(request);

      List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
      for (InterfaceHttpData parm : parmList) {
        if (parm.getHttpDataType() == HttpDataType.Attribute) {
          Attribute data = (Attribute) parm;
          result.put(data.getName(), data.getValue());
        }
      }
    } else {
      return null;
    }
    return result;
  }
}
