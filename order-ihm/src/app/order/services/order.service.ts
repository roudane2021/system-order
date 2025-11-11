import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Criteria, Order, PageResult } from "../models/order.model";
import {  catchError, Observable, of, throwError } from "rxjs";
import { environment } from "src/environments/environment";
import { API_ENDPOINTS_ORDERS } from "../constants/api-endpoints.constant";
import { LogService } from '../../core/services/log.service';



@Injectable()
export class OrderService {

    private http: HttpClient = inject(HttpClient);
    private logService: LogService = inject(LogService);
  
    
    public getOrders(page: number = environment.pagination.defaultPage, filtres: Criteria[] = []): Observable<PageResult<Order>> {
      const size = environment.pagination.defaultSize;
      const url = API_ENDPOINTS_ORDERS.GET_ORDER;
      this.logService.debug('getOrders : ', page, filtres);
        const params = new HttpParams()
            .set('page', page.toString())
            .set('size', size.toString());
      return this.http.get<PageResult<Order>>(url, { params }).pipe(
        catchError(error => {
          this.logService.error('Erreur getOrders', error);
          return throwError(() => error); 
        })
      );
    }

  /**
   * Sauvegarde (crée ou met à jour) une commande
   * @param order L'objet Order à sauvegarder
   */
  public saveOrder(order: Order): Observable<PageResult<Order>> {
    this.logService.debug('saveOrder : ', order);
    return this.http.get<PageResult<Order>>(API_ENDPOINTS_ORDERS.SAVE_ORDER);
  }
    
  public deleteOrder(order: Order): Observable<void> {
  this.logService.debug('deleteOrder : ', order);
    //return this.http.get<PageResult<Order>>(`${environment.apiBaseUrl}${environment.endpoints.orders}`);
    return of(undefined);
  }
    
 

}