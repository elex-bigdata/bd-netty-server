package com.elex.bigdata.nettyserver;

import com.elex.bigdata.ro.BasicRedisOperation;

/**
 * User: Z J Wu Date: 14-3-6 Time: 下午1:59 Package: com.elex.bigdata.nettyserver
 */
public class NettyServerUtils {
  public static final BasicRedisOperation REDIS_OPERATION = new BasicRedisOperation("/redis.site.properties");

  public static String extractURI2UID(String uri) {
    return uri.substring(3, uri.length());
  }
}
