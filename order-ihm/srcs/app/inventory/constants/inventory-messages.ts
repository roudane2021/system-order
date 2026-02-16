import { Inventory } from "../models/inventory.model";

export const InventoryMessages = {
    deleteSuccess: (item: Inventory): string => `Item #${item.itemNumber} est supprimée`,
    deleteError: (item: Inventory) => `Item #${item.itemNumber} n'est pas supprimée`,
    createSuccess: (item: Inventory): string => `Item #${item.itemNumber} est créé`,
    createError: (item: Inventory) => `Item #${item.itemNumber} n'est pas créé`,
    deleteRetry: (item: Inventory) => `Erreur lors de la suppression de la item ${item.itemNumber}. Voulez-vous réessayer ?`,
    confirmDelete: (item: Inventory) => `Voulez-vous vraiment supprimer la item #${item.itemNumber} ?`,
    dowloadListOrder: () => `Impossible de charger la liste des items`
}
