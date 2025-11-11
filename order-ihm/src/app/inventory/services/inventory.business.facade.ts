import { inject, Injectable } from "@angular/core";
import { InventoryService } from "./inventory.service";
import { LogService } from "src/app/core/services/log.service";
import { AppError, Criteria, Inventory, PageResult } from "../models/inventory.model";
import { catchError, Observable, tap, throwError } from "rxjs";

@Injectable()
export class InventoryBusinessFacade {

    private inventoryService: InventoryService = inject(InventoryService);
    private logService: LogService = inject(LogService);

    /** 🔹 Récupère les items */
    getItems(page: number, criteria: Criteria[]): Observable<PageResult<Inventory>> {
        return this.inventoryService.getItems(page, criteria).pipe(
            tap(() => this.logService.info(`Chargement items page ${page}`)),
            catchError((error: AppError) => {
                this.logService.error('Erreur lors du chargement des items', error);
                return throwError(() => error);
            })
        );
    }

    /** 🔹 Supprime un item */
    public deleteItem(item: Inventory): Observable<void> {
        return this.inventoryService.deleteItem(item).pipe(
            tap(() => this.logService.info(`Suppression item ${item.itemNumber}`)),
            catchError((error: AppError) => {
                this.logService.error('Erreur suppression item', error);
                return throwError(() => error);
            })
        );
    }
    /** 🔹 Enregistrer un item */
    public saveItem(item: Inventory): Observable<PageResult<Inventory>> {
        return this.inventoryService.saveItem(item).pipe(
            tap(() => this.logService.info(`Enregistrer item ${item.itemNumber}`)),
            catchError((error: AppError) => {
                this.logService.error('Erreur Enregistrer item', error);
                return throwError(() => error);
            })
        );
    }
}
