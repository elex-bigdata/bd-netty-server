package com.elex.bigdata.nettyserver;

/**
 * User: Z J Wu Date: 14-3-6 Time: 下午2:24 Package: com.elex.bigdata.nettyserver
 */
public enum Errors {
  NO_SUCH_P_FOR_THIS_USER("0"), ERROR_RESULT("-1");

  private String returnContent;

  private byte[] returnContentBytes;

  Errors(String returnContent) {
    this.returnContent = returnContent;
    this.returnContentBytes = this.returnContent.getBytes();
  }

  public String getReturnContent() {
    return returnContent;
  }

  public byte[] getReturnContentBytes() {
    return returnContentBytes;
  }
}
