package com.hunk.idworker.handler;

import com.hunk.idworker.annotations.RequestMapping;
import com.hunk.idworker.config.PropertiesUtil;
import com.hunk.idworker.id.IdMaker;
import com.hunk.idworker.util.Constants;

/**
 * <pre>
 * </pre>
 * 
 * @author: xiongchengwei
 * @date: 2016年3月15日 下午3:21:47
 */
@RequestMapping(uri = "/idmaker")
public class IdMakerHandler implements HttpResponseHandler {

  public final static long workerId =
      Long.valueOf(PropertiesUtil.getValue(Constants.IDMAKER_DATDACENTERID));
  public final static long datacenterId =
      Long.valueOf(PropertiesUtil.getValue(Constants.IDMAKER_WORKERID));

  public final static IdMaker idMaker = new IdMaker(workerId, datacenterId);

  @Override
  public String reponse() {
    String nextId = String.valueOf(idMaker.nextId());
    return nextId;
  }
}
