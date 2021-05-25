package com.exatask.platform.api.interceptors;

import com.exatask.platform.utilities.constants.ContextHeader;
import com.exatask.platform.utilities.contexts.AppContext;
import com.exatask.platform.utilities.contexts.AppContextProvider;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiContextInterceptor extends AppInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    AppContext.AppContextBuilder appContextBuilder = AppContext.builder()
        .startTime(new Date())
        .traceId(request.getHeader(ContextHeader.TRACE_ID))
        .spanId(UUID.randomUUID().toString());

    Optional.ofNullable(request.getHeader(ContextHeader.SESSION_ID)).ifPresent(appContextBuilder::sessionId);

    Optional.ofNullable(request.getHeader(ContextHeader.ORGANIZATION_ID)).ifPresent((organizationId) ->
        appContextBuilder
          .organizationId(organizationId)
          .organizationName(request.getHeader(ContextHeader.ORGANIZATION_NAME)));

    Optional.ofNullable(request.getHeader(ContextHeader.USER_ID)).ifPresent((userId) ->
        appContextBuilder
          .userId(userId)
          .userName(request.getHeader(ContextHeader.USER_NAME))
          .userEmailId(request.getHeader(ContextHeader.USER_EMAIL_ID)));

    AppContextProvider.setContext(appContextBuilder.build());
    return true;
  }
}