import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { Order } from '../../models/order.model';
import {  Observable, Subject, takeUntil, tap } from 'rxjs';
import { ActionService } from 'src/app/core/services/action.service';
import { ActionType } from 'src/app/core/models/action.model';
import { DialogService } from '../../../core/services/dialog.service';
import { OrderUiFacade } from '../../services/order.ui.facade';
import { OrderMessages } from '../../constants/order-messages';
import { NotificationService } from '../../../core/services/notifiation.service';


export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}



@Component({
  selector: 'app-table-display',
  templateUrl: './table-display.component.html',
  styleUrls: ['./table-display.component.scss']
})
export class TableDisplayComponent implements OnInit, OnDestroy {

  private orderUiFacade: OrderUiFacade = inject(OrderUiFacade);
  private actionService: ActionService = inject(ActionService);
  private dialogService: DialogService = inject(DialogService);
  private notificationService: NotificationService = inject(NotificationService);
  private destroy$ = new Subject<void>();

  

  displayedColumns: string[] = ['orderNumber', 'orderDate', 'customerId', 'status', 'actions'];
  order$!: Observable<Order[]>;
  

    ngOnInit(): void {
      this.initializeOrders();
    }
  
   private initializeOrders(): void {
     this.order$ = this.orderUiFacade.order$.pipe(takeUntil(this.destroy$));
     this.orderUiFacade.error$
       .pipe(
         takeUntil(this.destroy$),
         tap(() => {
           this.dialogService.error(OrderMessages.dowloadListOrder());
           this.notificationService.error(OrderMessages.dowloadListOrder());
         })
       )
       .subscribe();
  }
  
  viewDetails(order: Order): void {
    this.actionService.emit({ type: ActionType.VIEW_ORDER, payload: order });
  }
  
  createDetails(): void {
    this.actionService.emit({ type: ActionType.CREATE_ORDER });
   }


  deleteOrder(order: Order): void {
  this.actionService.emit({ type: ActionType.DELETE_ORDER, payload: order });
  
  }
  
  ngOnDestroy(): void {
  this.destroy$.next();
  this.destroy$.complete();
  }

}
