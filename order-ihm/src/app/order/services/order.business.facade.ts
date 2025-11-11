import { inject, Injectable } from "@angular/core";
import { OrderService } from "./order.service";
import { LogService } from "src/app/core/services/log.service";
import { AppError, Criteria, Order, PageResult } from "../models/order.model";
import { catchError, Observable, tap, throwError } from "rxjs";

@Injectable()
export class OrderBusinessFacade {

    private orderService: OrderService = inject(OrderService);
    private logService: LogService = inject(LogService);
    
    /** 🔹 Récupère les commandes */
  getOrders(page: number, criteria: Criteria[]): Observable<PageResult<Order>> {
    return this.orderService.getOrders(page, criteria).pipe(
      tap(() => this.logService.info(`Chargement commandes page ${page}`)),
      catchError((error: AppError) => {
        this.logService.error('Erreur lors du chargement des commandes', error);
        return throwError(() => error);
      })
    );
  }

  /** 🔹 Supprime une commande */
  public deleteOrder(order: Order): Observable<void> {
    return this.orderService.deleteOrder(order).pipe(
      tap(() => this.logService.info(`Suppression commande ${order.orderNumber}`)),
      catchError((error: AppError) => {
        this.logService.error('Erreur suppression commande', error);
        return throwError(() => error);
      })
    );
  }
    /** 🔹 Enregistrer une commande */
    public saveOrder(order: Order): Observable<PageResult<Order>> {
      return this.orderService.saveOrder(order).pipe(
      tap(() => this.logService.info(`Enregistrer commande ${order.orderNumber}`)),
      catchError((error: AppError) => {
        this.logService.error('Erreur Enregistrer commande', error);
        return throwError(() => error);
      })
    );
    }
    

    
}