package com.exatask.platform.api.utilities;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.constants.Environment;
import com.exatask.platform.utilities.exceptions.RuntimePropertyNotFoundException;

public class ApiServiceUtility {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private static Environment environment = null;

  public static Environment getServiceEnvironment() {

    if (environment != null) {
      return environment;
    }

    try {
      environment = Environment.valueOf(ServiceUtility.getServiceEnvironment().toUpperCase());

    } catch (RuntimePropertyNotFoundException | IllegalArgumentException exception) {

      LOGGER.debug(exception);
      LOGGER.debug("Setting service environment as DEBUG");
      environment = Environment.DEBUG;
    }

    return environment;
  }
}
