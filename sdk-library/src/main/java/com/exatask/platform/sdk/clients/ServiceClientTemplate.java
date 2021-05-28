package com.exatask.platform.sdk.clients;

import com.exatask.platform.sdk.authenticators.SdkAuthenticator;
import com.exatask.platform.sdk.authenticators.NoAuthSdkAuthenticator;
import com.exatask.platform.sdk.decoders.ServiceErrorDecoder;
import com.exatask.platform.sdk.interceptors.AppAuthenticationInterceptor;
import com.exatask.platform.sdk.interceptors.AppContextInterceptor;
import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceClientTemplate<T extends ServiceClient> {

  @Autowired
  private Client client;

  @Autowired
  private Encoder encoder;

  @Autowired
  private Decoder decoder;

  @Autowired
  private ServiceErrorDecoder errorDecoder;

  @Autowired
  private AppContextInterceptor appContextInterceptor;

  public T getServiceClient(Class<T> clazz, String baseUrl) {
    return getServiceClient(clazz, baseUrl, new NoAuthSdkAuthenticator.NoAuthCredentials());
  }

  public T getServiceClient(Class<T> clazz, String baseUrl, SdkAuthenticator.Credentials credentials) {
    return getServiceClient(clazz, baseUrl, credentials, null);
  }

  public T getServiceClient(Class<T> clazz, String baseUrl, SdkAuthenticator.Credentials credentials, List<RequestInterceptor> interceptors) {

    List<RequestInterceptor> interceptorList = new ArrayList<>();
    interceptorList.add(appContextInterceptor);
    interceptorList.add(new AppAuthenticationInterceptor(credentials));

    if (!CollectionUtils.isEmpty(interceptors)) {
      interceptorList.addAll(interceptors);
    }

    return Feign.builder()
        .encoder(encoder)
        .decoder(decoder)
        .logLevel(Logger.Level.BASIC)
        .retryer(Retryer.NEVER_RETRY)
        .errorDecoder(errorDecoder)
        .client(client)
        .requestInterceptors(interceptorList)
        .target(clazz, baseUrl);
  }
}
