package com.roudane.order.infra_order.adapter.output.logger;

import com.roudane.order.domain_order.port.output.logger.ILoggerPort;
import org.springframework.stereotype.Component;

@Component
public class ILoggerAdapter implements ILoggerPort {
    /**
     * @param message
     */
    @Override
    public void info(String message) {

    }

    /**
     * @param message
     */
    @Override
    public void debug(String message) {

    }

    /**
     * @param message
     */
    @Override
    public void warn(String message) {

    }

    /**
     * @param message
     */
    @Override
    public void error(String message) {

    }

    /**
     * @param message
     * @param exception
     */
    @Override
    public void error(String message, Throwable exception) {

    }

    /**
     * @param level
     * @param message
     */
    @Override
    public void conditionalLog(String level, String message) {

    }
}
