package com.exatask.platform.api.exceptions;

import com.exatask.platform.utilities.errors.AppError;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter(AccessLevel.PRIVATE)
public class HttpException extends RuntimeException {

  private HttpStatus httpStatus;

  private AppError error;

  private Map<String, String> invalidAttributes;

  private Map<String, Object> extraParams;

  protected HttpException(String message) {
    super(message);
  }

  protected HttpException(String message, Throwable cause) {
    super(message, cause);
  }

  protected static HttpException buildException(
      HttpStatus httpStatus,
      String message,
      AppError appError,
      List<String> errorArguments,
      Exception exception,
      Map<String, String> invalidAttributes,
      Map<String, Object> extraParams) {

    String exceptionMessage = StringUtils.isNotEmpty(message) ? message : "";

    if (appError != null) {
      exceptionMessage = appError.toLocale(errorArguments.toArray(new String[]{}));
    } else if (exception != null) {
      exceptionMessage = exception.getMessage();
    } else if (httpStatus != null) {
      exceptionMessage = httpStatus.getReasonPhrase();
    }

    HttpException httpException;

    if (ObjectUtils.isEmpty(exception)) {
      httpException = new HttpException(exceptionMessage);
    } else {
      httpException = new HttpException(exceptionMessage, exception);
    }
    Optional.ofNullable(appError).ifPresent(httpException::setError);
    Optional.ofNullable(httpStatus).ifPresent(httpException::setHttpStatus);
    Optional.ofNullable(invalidAttributes).ifPresent(httpException::setInvalidAttributes);
    Optional.ofNullable(extraParams).ifPresent(httpException::setExtraParams);

    return httpException;
  }
}
