import { Order } from "../models/order.model";


export const OrderMessages = {
    deleteSuccess: (order: Order): string => `Commande #${order.orderNumber} est supprimée`,
    deleteError: (order: Order) => `Commande #${order.orderNumber} n'est pas supprimée`,
    createSuccess: (order: Order): string => `Commande #${order.orderNumber} est créé`,
    createError: (order: Order) => `Commande #${order.orderNumber} n'est pas créé`,
    deleteRetry:  (order: Order ) => `Erreur lors de la suppression de la commande ${order.orderNumber}. Voulez-vous réessayer ?`,
    confirmDelete: (order: Order) => `Voulez-vous vraiment supprimer la commande #${order.orderNumber} ?`,
    dowloadListOrder: () => `Impossible de charger la liste des commandes`
}