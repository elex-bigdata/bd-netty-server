package com.elex.bigdata.nettyserver;

import com.elex.bigdata.hashing.BDMD5;
import com.elex.bigdata.hashing.HashingException;
import com.elex.bigdata.ro.BasicRedisOperation;
import com.elex.bigdata.ro.RedisOperationException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * User: Z J Wu Date: 14-3-6 Time: 下午5:44 Package: com.elex.bigdata.nettyserver
 */
public class PutRedis {
  private Random random = new Random();
  private DecimalFormat df = new DecimalFormat("##");

  private String generateP() {

    int i = 100;
    StringBuilder sb = new StringBuilder();

    sb.append('z');
    int r = random.nextInt(i - 10);
    sb.append(StringUtils.leftPad(String.valueOf(r), 2, '0'));
    i -= r;

    sb.append('a');
    r = random.nextInt(i);
    sb.append(StringUtils.leftPad(String.valueOf(r), 2, '0'));

    sb.append('b');
    sb.append(StringUtils.leftPad(String.valueOf(i - r), 2, '0'));
    return sb.toString();
  }

  @Test
  public void test() throws IOException, RedisOperationException, HashingException {
    BasicRedisOperation bro = new BasicRedisOperation("/redis.site.properties");

    String line, p;
    try (BufferedReader br = new BufferedReader(
      new InputStreamReader(new FileInputStream(new File("src/test/resources/fake_uid"))))) {
      while ((line = br.readLine()) != null) {
        p = generateP();
        System.out.println(line + " - " + p);
        bro.set(BDMD5.getInstance().toMD5(line), p);
      }
    }
  }

}
