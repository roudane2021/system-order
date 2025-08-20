package com.roudane.order.infra_order.transverse.logging;

import com.roudane.order.domain_order.port.output.logger.ILoggerPort;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContext;

@Slf4j
@Component
public class ILoggerAdapter implements ILoggerPort {



    @Override
    public void info(String message) {
        log.info(message);
    }

    @Override
    public void debug(String message) {
        log.debug(message);
    }

    @Override
    public void warn(String message) {
        log.warn(message);
    }

    @Override
    public void error(String message) {
         log.error(message);
    }

    @Override
    public void error(String message, Throwable exception) {
        log.error(message, exception);
    }

    @Override
    public void conditionalLog(String level, String message) {

    }
}
