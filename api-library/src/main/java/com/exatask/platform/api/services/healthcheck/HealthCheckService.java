package com.exatask.platform.api.services.healthcheck;

import com.exatask.platform.api.configurations.ApiServiceConfig;
import com.exatask.platform.api.requests.AppRequest;
import com.exatask.platform.api.services.AppService;
import com.exatask.platform.api.services.healthcheck.responses.HealthCheckResponse;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheck;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheckData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component("healthCheckService")
public class HealthCheckService extends AppService {

  @Autowired
  private ApiServiceConfig apiServiceConfig;

  @Autowired
  private Set<ServiceHealthCheck> platformHealthChecks;

  @Override
  public HealthCheckResponse process(AppRequest request) {

    HealthCheckResponse healthCheckResponse = new HealthCheckResponse();
    Map<String, Set<ServiceHealthCheckData>> serviceHealthCheckData = new HashMap<>();

    healthCheckResponse.setName(apiServiceConfig.getName());
    healthCheckResponse.setCopyright(apiServiceConfig.getCopyright());
    healthCheckResponse.setEnvironment(apiServiceConfig.getEnvironment());
    healthCheckResponse.setLicense(apiServiceConfig.getLicense());
    healthCheckResponse.setVersion(apiServiceConfig.getVersion());

    for (ServiceHealthCheck healthCheck : platformHealthChecks) {
      serviceHealthCheckData.put(healthCheck.getName(), healthCheck.healthCheck());
    }
    healthCheckResponse.setServices(serviceHealthCheckData);

    return healthCheckResponse;
  }
}
