package com.roudane.order.infra_order.transverse.logging;

import com.roudane.order.domain_order.port.output.logger.ILoggerPort;
import org.springframework.stereotype.Component;

@Component
public class ILoggerAdapter implements ILoggerPort {
    @Override
    public void info(String message) {

    }

    @Override
    public void debug(String message) {

    }

    @Override
    public void warn(String message) {

    }

    @Override
    public void error(String message) {

    }

    @Override
    public void error(String message, Throwable exception) {

    }

    @Override
    public void conditionalLog(String level, String message) {

    }
}
