package com.exatask.platform.mongodb.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@Jacksonized
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FullDate {

  @Min(1)
  @Max(31)
  @NotEmpty
  @Field("date")
  private Integer date;

  @Min(1)
  @Max(12)
  @NotEmpty
  @Field("month")
  private Integer month;

  @Min(1900)
  @NotEmpty
  @Field("year")
  private Integer year;
}
