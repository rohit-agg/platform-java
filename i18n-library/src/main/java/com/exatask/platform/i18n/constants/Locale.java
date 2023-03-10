package com.exatask.platform.i18n.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Locale {

  public static final String DEFAULT_ENCODING = "UTF-8";
  public static final String DEFAULT_LANGUAGE = "en";

  public static final String DEFAULT_COUNTRY = "India";
  public static final String DEFAULT_COUNTRY_CODE = "IN";
  public static final String DEFAULT_TIMEZONE = "Asia/Kolkata";
  public static final String DEFAULT_CURRENCY = "INR";

  public static final String DEFAULT_LOCALE = DEFAULT_LANGUAGE + "-" + DEFAULT_COUNTRY_CODE;
}
