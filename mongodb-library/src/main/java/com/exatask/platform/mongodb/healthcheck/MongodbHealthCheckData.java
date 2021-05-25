package com.exatask.platform.mongodb.healthcheck;

import com.exatask.platform.utilities.healthcheck.ServiceHealthCheckData;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MongodbHealthCheckData extends ServiceHealthCheckData {

  private final String database;
}