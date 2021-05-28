package com.exatask.platform.sdk.authenticators;

import com.exatask.platform.utilities.constants.ServiceAuth;

public class NoAuthSdkAuthenticator implements SdkAuthenticator {

  @Override
  public ServiceAuth getAuthentication() {
    return ServiceAuth.NO_AUTH;
  }

  @Override
  public String generate() {
    return null;
  }

  public static class NoAuthCredentials implements Credentials {

    @Override
    public ServiceAuth getAuthentication() {
      return ServiceAuth.NO_AUTH;
    }
  }
}