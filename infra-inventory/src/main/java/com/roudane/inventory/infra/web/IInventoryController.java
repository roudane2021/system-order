package com.roudane.inventory.infra.web;

import com.roudane.inventory.infra.web.dto.InventoryItemDto;
import com.roudane.inventory.infra.web.dto.StockAdjustmentRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Interface définissant les opérations disponibles pour interagir avec l'inventaire.
 * Elle expose les méthodes de consultation et de modification du stock produit.
 */
public interface IInventoryController {

    /**
     * Récupère un article d'inventaire à partir de son identifiant produit.
     *
     * @param productId identifiant du produit
     * @return l'article d'inventaire correspondant
     * @throws org.springframework.web.server.ResponseStatusException si l'article n'est pas trouvé
     */
    ResponseEntity<InventoryItemDto> getInventoryByProductId(String productId);

    /**
     * Récupère la liste complète des articles d'inventaire.
     *
     * @return liste des articles d'inventaire
     */
    ResponseEntity<List<InventoryItemDto>> getAllInventoryItems();

    /**
     * Ajuste le stock d'un produit donné selon une requête d'ajustement.
     *
     * @param productId identifiant du produit
     * @param adjustmentRequest requête contenant la quantité et la raison de l'ajustement
     * @return une réponse vide avec statut HTTP 200 si l'ajustement est effectué
     */
    ResponseEntity<Void> adjustStock(String productId, StockAdjustmentRequestDto adjustmentRequest);
}
