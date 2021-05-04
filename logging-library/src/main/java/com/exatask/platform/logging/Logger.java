package com.exatask.platform.logging;

import com.exatask.platform.logging.serializers.LogSerializer;
import com.exatask.platform.logging.serializers.LogSerializerFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.util.Date;

public class Logger {

  private static final String DEFAULT_SERVICE = "platform-logging";

  private final org.apache.logging.log4j.Logger log4jLogger;

  private final LogSerializer serializer;

  private final String serviceName;

  Logger(Class<?> clazz) {
    this(clazz, DEFAULT_SERVICE);
  }

  Logger(Class<?> clazz, String service) {

    log4jLogger = LogManager.getLogger(clazz);
    serviceName = service;

    LoggerContext loggerContext = ((org.apache.logging.log4j.core.Logger) log4jLogger).getContext();
    String style = loggerContext.getConfiguration().getStrSubstitutor().getVariableResolver().lookup("style");
    serializer = LogSerializerFactory.getLogSerializer(style.toUpperCase());
  }

  public void trace(String message) {
    log(Level.TRACE, message);
  }

  public void trace(LogMessage logMessage) {
    log(Level.TRACE, logMessage);
  }

  public void debug(String message) {
    log(Level.DEBUG, message);
  }

  public void debug(LogMessage logMessage) {
    log(Level.DEBUG, logMessage);
  }

  public void info(String message) {
    log(Level.INFO, message);
  }

  public void info(LogMessage logMessage) {
    log(Level.INFO, logMessage);
  }

  public void warn(String message) {
    log(Level.WARN, message);
  }

  public void warn(LogMessage logMessage) {
    log(Level.WARN, logMessage);
  }

  public void error(String message) {
    log(Level.ERROR, message);
  }

  public void error(LogMessage logMessage) {
    log(Level.ERROR, logMessage);
  }

  public void fatal(String message) {
    log(Level.FATAL, message);
  }

  public void fatal(LogMessage logMessage) {
    log(Level.FATAL, logMessage);
  }

  public void log(Level level, String message) {

    LogMessage logMessage = LogMessage.builder().message(message).build();
    this.log(level, logMessage);
  }

  public void log(Level level, LogMessage logMessage) {

    logMessage.setLevel(level.toString().toLowerCase());
    logMessage.setServiceName(serviceName);
    logMessage.setTimestamp(new Date());
    log4jLogger.log(level, serializer.serialize(logMessage));
  }
}
