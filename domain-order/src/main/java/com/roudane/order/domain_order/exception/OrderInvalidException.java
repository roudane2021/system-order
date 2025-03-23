package com.roudane.order.domain_order.exception;


/**
 * Exception lancée lorsque l'ordre est invalide.
 */
public class OrderInvalidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur avec un message de détail.
     *
     * @param message le message de détail, sauvegardé pour une récupération ultérieure par la méthode {@link #getMessage()}.
     */
    public OrderInvalidException(String message) {
        super(message);
    }
}
