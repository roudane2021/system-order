package com.roudane.order.domain_inventory.exception;

public class InventoryNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur avec un message de détail.
     *
     * @param message le message de détail, sauvegardé pour une récupération ultérieure par la méthode {@link #getMessage()}.
     */
    public InventoryNotFoundException(String message) {
        super(message);
    }
}
