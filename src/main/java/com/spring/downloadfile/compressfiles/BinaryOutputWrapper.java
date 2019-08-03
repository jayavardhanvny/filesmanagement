package com.spring.downloadfile.compressfiles;

import org.springframework.http.HttpHeaders;

public class BinaryOutputWrapper {
  private byte[] bytes;

  public byte[] getBytes() {
    return bytes;
  }

  public void setBytes(byte[] bytes) {
    this.bytes = bytes;
  }

  public HttpHeaders getHeaders() {
    return headers;
  }

  public void setHeaders(HttpHeaders headers) {
    this.headers = headers;
  }

  private HttpHeaders headers;
  public BinaryOutputWrapper(byte[] bytes, HttpHeaders headers) {
    this.bytes = bytes;
    this.headers = headers;
  }

}
