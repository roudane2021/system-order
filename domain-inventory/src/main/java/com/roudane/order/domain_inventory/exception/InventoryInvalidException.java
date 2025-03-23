package com.roudane.order.domain_inventory.exception;


/**
 * Exception lancée lorsque l'ordre est invalide.
 */
public class InventoryInvalidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur avec un message de détail.
     *
     * @param message le message de détail, sauvegardé pour une récupération ultérieure par la méthode {@link #getMessage()}.
     */
    public InventoryInvalidException(String message) {
        super(message);
    }
}
