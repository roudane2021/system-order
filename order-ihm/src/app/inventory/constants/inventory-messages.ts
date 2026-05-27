
import { Inventory } from "../models/inventory.model";

export const InventoryMessages = {
  deleteSuccess: (inventory: Inventory): string => `Commande #${inventory.itemNumber} est supprimée`,
  deleteError: (inventory: Inventory): string => `Commande #${inventory.itemNumber} n'est pas supprimée`,
  createSuccess: (inventory: Inventory): string => `Commande #${inventory.itemNumber} a été créée`,
  createError: (inventory: Inventory): string => `Commande #${inventory.itemNumber} n'a pas pu être créée`,
  deleteRetry: (inventory: Inventory): string => `Erreur lors de la suppression de la commande ${inventory.itemNumber}. Voulez-vous réessayer ?`,
  confirmDelete: (inventory: Inventory): string => `Voulez-vous vraiment supprimer la commande #${inventory.itemNumber} ?`,
  downloadListOrder: (): string => `Impossible de charger la liste des commandes`
};
