package com.exatask.platform.api.configurations;

import com.exatask.platform.api.interceptors.ApiContextInterceptor;
import com.exatask.platform.api.interceptors.AuthenticationInterceptor;
import com.exatask.platform.api.interceptors.ServiceEnabledInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

  @Autowired
  private ServiceEnabledInterceptor serviceEnabledInterceptor;

  @Autowired
  private ApiContextInterceptor apiContextInterceptor;

  @Autowired
  private AuthenticationInterceptor authenticationInterceptor;

  private static final List<String> fullAccessUrls = new ArrayList<>(Arrays.asList(
      "/health-check", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/favicon*"
  ));

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    registry.addInterceptor(serviceEnabledInterceptor);

    registry.addInterceptor(apiContextInterceptor)
        .excludePathPatterns(fullAccessUrls);

    registry.addInterceptor(authenticationInterceptor)
        .excludePathPatterns(fullAccessUrls);
  }
}
