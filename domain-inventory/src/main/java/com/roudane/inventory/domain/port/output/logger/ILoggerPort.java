package com.roudane.inventory.domain.port.output.logger;

public interface ILoggerPort {



    // Log d'information
    void info(String message);

    // Log de débogage
    void debug(String message);

    // Log d'avertissement
    void warn(String message);

    // Log d'erreur
    void error(String message);

    // Log d'exception avec un message personnalisé
    void error(String message, Throwable exception);

    // Log conditionnel basé sur un paramètre
    void conditionalLog(String level, String message);
}
