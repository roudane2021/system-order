import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { catchError, Observable, of, throwError } from "rxjs";
import { environment } from "src/environments/environment";
import { LogService } from '../../core/services/log.service';
import { Criteria, Inventory, PageResult } from "../models/inventory.model";
import { API_ENDPOINTS_INVENTORY } from "../constants/api-endpoints.constant";

@Injectable()
export class InventoryService {

    private http: HttpClient = inject(HttpClient);
    private logService: LogService = inject(LogService);


    public getItems(page: number = environment.pagination.defaultPage, filtres: Criteria[] = []): Observable<PageResult<Inventory>> {
        const size = environment.pagination.defaultSize;
        const url = API_ENDPOINTS_INVENTORY.GET_INVENTORY;
        this.logService.debug('getItems : ', page, filtres);
        const params = new HttpParams()
            .set('page', page.toString())
            .set('size', size.toString());
        return this.http.get<PageResult<Inventory>>(url, { params }).pipe(
            catchError(error => {
                this.logService.error('Erreur getItems', error);
                return throwError(() => error);
            })
        );
    }

    /**
     * Sauvegarde (crée ou met à jour) un item
     * @param item L'objet Inventory à sauvegarder
     */
    public saveItem(item: Inventory): Observable<PageResult<Inventory>> {
        this.logService.debug('saveItem : ', item);
        return this.http.get<PageResult<Inventory>>(API_ENDPOINTS_INVENTORY.SAVE_INVENTORY);
    }

    public deleteItem(item: Inventory): Observable<void> {
        this.logService.debug('deleteItem : ', item);
        return of(undefined);
    }
}
