package com.exatask.platform.crypto.ciphers;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PlainText implements AppCipher {

  @Override
  public String encrypt(String data) {
    return data;
  }

  @Override
  public String decrypt(String data) {
    return data;
  }
}
