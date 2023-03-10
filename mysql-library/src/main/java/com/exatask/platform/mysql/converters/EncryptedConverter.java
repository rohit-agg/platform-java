package com.exatask.platform.mysql.converters;

import javax.persistence.AttributeConverter;

public abstract class EncryptedConverter implements AttributeConverter<String, String> {

  public abstract String encrypt(String data);

  public abstract String decrypt(String data);

  @Override
  public String convertToDatabaseColumn(String data) {
    return encrypt(data);
  }

  @Override
  public String convertToEntityAttribute(String data) {
    return decrypt(data);
  }
}
