import { inject, Injectable } from "@angular/core";
import { BehaviorSubject, catchError, combineLatest, EMPTY, map, Observable, Subject, switchMap, takeUntil, tap, throwError } from "rxjs";
import { environment } from "src/environments/environment";
import { AppError, Criteria, Inventory, InventoryItem, Page, PageResult } from "../models/inventory.model";
import { FormGroup } from "@angular/forms";
import { InventoryBusinessFacade } from "./inventory.business.facade";
import { DialogService } from "src/app/core/services/dialog.service";
import { InventoryMessages } from "../constants/inventory-messages";

@Injectable()
export class InventoryUiFacade {

    private _state$ = new BehaviorSubject<{ page: number; criteria: Criteria[] }>({
        page: environment.pagination.defaultPage,
        criteria: []
    });
    private _itemsSubject$ = new BehaviorSubject<PageResult<Inventory>>(this.getDefaultResult());
    private _errorSubject$ = new Subject<AppError>();
    private _refresh$ = new BehaviorSubject<void>(void 0);
    private destroy$ = new Subject<void>();
    private inventoryBusinessFacade = inject(InventoryBusinessFacade);
    private dialogService: DialogService = inject(DialogService);


    public init(): void {
        combineLatest([
            this._state$,
            this._refresh$
        ]).pipe(
            takeUntil(this.destroy$),
            switchMap(([state]) => this.inventoryBusinessFacade.getItems(state.page, state.criteria).pipe(
                tap(result => {
                    this._itemsSubject$.next(result);

                }),
                catchError(error => {
                    this._errorSubject$.next(error);
                    return EMPTY;
                })
            )
            )

        ).subscribe();
    }

    public deleteItem(item: Inventory): Observable<boolean> {
        return this.inventoryBusinessFacade.deleteItem(item).pipe(
            takeUntil(this.destroy$),
            switchMap(() => throwError(() => new Error('Erreur suppression'))),
            tap(() => this.refresh()),
            catchError((error) => {
                return throwError(() => error);
            })
        )
    }

    public saveItem(item: Inventory): void {
        this.inventoryBusinessFacade.saveItem(item).pipe(
            tap(() => {
                this.refresh();
                this.dialogService.success(InventoryMessages.createSuccess(item))
            }),
            catchError(() => {
                this.dialogService.error(InventoryMessages.createError(item))
                return EMPTY;
            })
        ).subscribe();
    }

    public get item$(): Observable<Inventory[]> {
        return this._itemsSubject$.asObservable()
            .pipe(
                map(result => result?.content)
            );
    }

    public get page$(): Observable<Page> {
        return this._itemsSubject$.asObservable()
            .pipe(
                map(this.extractPage)
            );
    }

    public get error$(): Observable<AppError> {
        return this._errorSubject$.asObservable();
    }

    public setPage(page: number): void {
        const current = this._state$.value;
        this._state$.next({ ...current, page });
    }

    public setCriteria(criteria: Criteria[]): void {
        this._state$.next({ page: environment.pagination.defaultPage, criteria })
    }

    public refresh(): void {
        this._refresh$.next();
    }

    public destroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    private extractPage(result: PageResult<Inventory>): Page {
        return {
            page: result?.page,
            size: result?.size,
            totalElements: result?.totalElements,
            totalPages: result?.totalPages
        };
    }

    private getDefaultResult(): PageResult<Inventory> {
        return {
            content: [],
            page: 0,
            size: 0,
            totalElements: 0,
            totalPages: 0
        };
    }
}
