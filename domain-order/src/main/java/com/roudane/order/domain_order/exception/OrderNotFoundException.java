package com.roudane.order.domain_order.exception;

public class OrderNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur avec un message de détail.
     *
     * @param message le message de détail, sauvegardé pour une récupération ultérieure par la méthode {@link #getMessage()}.
     */
    public OrderNotFoundException(String message) {
        super(message);
    }
}
