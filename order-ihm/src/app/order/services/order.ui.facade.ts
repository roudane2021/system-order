import { inject, Injectable } from "@angular/core";
import { BehaviorSubject, catchError, combineLatest, EMPTY, map, Observable, Subject, switchMap, takeUntil, tap, throwError } from "rxjs";
import { environment } from "src/environments/environment";
import { AppError, Criteria, Order, OrderItem, OrderStatus, Page, PageResult} from "../models/order.model";
import { FormGroup } from "@angular/forms";
import { OrderBusinessFacade } from "./order.business.facade";
import { DialogService } from "src/app/core/services/dialog.service";
import { OrderMessages } from "../constants/order-messages";


@Injectable()
export class OrderUiFacade { 

    private _state$ = new BehaviorSubject<{ page: number; criteria: Criteria[] }>({
  page: environment.pagination.defaultPage,
  criteria: []
    });
     private _ordersSubject$ = new BehaviorSubject<PageResult<Order>>(this.getDefaultResult());
    private _errorSubject$ = new Subject<AppError>();
    private _refresh$ = new BehaviorSubject<void>(void 0);
    private destroy$ = new Subject<void>();
    private orderBusinessFacade = inject(OrderBusinessFacade);
    private dialogService: DialogService = inject(DialogService);
  
    
    public init(): void {
   combineLatest([
            this._state$,
            this._refresh$
   ]).pipe(
       takeUntil(this.destroy$),
       switchMap(([state]) => this.orderBusinessFacade.getOrders(state.page, state.criteria).pipe(          
        tap(result => {
          this._ordersSubject$.next(result);
    
        }),
          catchError(error => {
            this._errorSubject$.next(error);
            return EMPTY;
          })
        )
       )
        
        ).subscribe();
    }
  
        public deleteOrder(order: Order): Observable<boolean> {
            return this.orderBusinessFacade.deleteOrder(order).pipe(
                takeUntil(this.destroy$),
                switchMap(() => throwError(() => new Error('Erreur suppression'))),
                tap(() => this.refresh()),
                catchError((error) => {
                    return throwError(() => error);
                })
            )
    
        }
  
      public saveOrder(order: Order): void {
        this.orderBusinessFacade.saveOrder(order).pipe(
          tap(() => {
            this.refresh();
            this.dialogService.success(OrderMessages.createSuccess(order))
          }),
          catchError(() => {
          this.dialogService.error(OrderMessages.createError(order))
          return EMPTY;
          })
        ).subscribe();
    }
  
   public get order$(): Observable<Order[]> {
            return this._ordersSubject$.asObservable()
                .pipe(
                    map(result => result?.content)
                );
        }
    
    public get page$(): Observable<Page> {
             return this._ordersSubject$.asObservable()
                .pipe(
                    map(this.extractPage)
                );
    }
   
  public get error$(): Observable<AppError> {
    return this._errorSubject$.asObservable();
  }
    
    public setPage(page: number): void {
        const current = this._state$.value;
        this._state$.next({...current, page});
    }

    public setCriteria(criteria: Criteria[]): void {
        this._state$.next({page:environment.pagination.defaultPage, criteria})
    }

    public refresh(): void {
        this._refresh$.next();
    }

     public mapFormToOrder = (orderForm : FormGroup, order: Order | undefined = undefined): Order =>  {
        const formValue = orderForm.value;
        const { status = OrderStatus.CONFIRMED, orderNumber = '', orderDate = '' } = order ?? {};
  
    return {
      orderNumber: orderNumber,
      orderDate: orderDate,
      firstName: formValue.personalInfo.prenom,
      lastName: formValue.personalInfo.nom,
      city: formValue.personalInfo.ville,
      nationality: formValue.personalInfo.nationalite,
      email:
        formValue.contactPreference === 'email'
          ? formValue.email.email
          : '',
      phone:
        formValue.contactPreference === 'phone'
          ? formValue.phone
          : '',
      userName: formValue.loginInfo.username,
      password: formValue.loginInfo.password,
      status: status, 
      items: formValue.items?.map((item: OrderItem) => ({
        productId: item.productId,
        quantity: item.quantity,
        price: item.price,
      })),
    };
     }
public destroy(): void {
  this.destroy$.next();
  this.destroy$.complete();
    }

    private extractPage(result: PageResult<Order>): Page {
        return {
            page: result?.page,
            size: result?.size,
            totalElements: result?.totalElements,
            totalPages: result?.totalPages
        };
    }
        
    private getDefaultResult(): PageResult<Order> {
        return {
            content: [],
            page: 0,
            size: 0,
            totalElements: 0,
            totalPages: 0
        };
    }
  

}