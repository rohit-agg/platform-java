package com.exatask.platform.sdk;

import feign.Client;
import feign.Contract;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableFeignClients
@Import(FeignClientsConfiguration.class)
public class AppFeignConfiguration {

  @Bean
  public Contract contract() {
    return new SpringMvcContract();
  }

  @Bean
  public Client client() {
    return new OkHttpClient();
  }

  @Bean
  public Encoder encoder() {
    return new JacksonEncoder();
  }

  @Bean
  public Decoder decoder() {
    return new JacksonDecoder();
  }
}
