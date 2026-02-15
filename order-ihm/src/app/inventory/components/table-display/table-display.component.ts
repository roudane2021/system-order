import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { Inventory } from '../../models/inventory.model';
import {  Observable, Subject, takeUntil, tap } from 'rxjs';
import { ActionService } from 'src/app/core/services/action.service';
import { ActionType } from 'src/app/core/models/action.model';
import { DialogService } from '../../../core/services/dialog.service';
import { InventoryUiFacade } from '../../services/inventory.ui.facade';
import { NotificationService } from '../../../core/services/notifiation.service';
import { InventoryMessages } from '../../constants/inventory-messages';

@Component({
  selector: 'app-table-display',
  templateUrl: './table-display.component.html',
  styleUrls: ['./table-display.component.scss']
})
export class TableDisplayComponent implements OnInit, OnDestroy {

  private inventoryUiFacade: InventoryUiFacade = inject(InventoryUiFacade);
  private actionService: ActionService = inject(ActionService);
  private dialogService: DialogService = inject(DialogService);
  private notificationService: NotificationService = inject(NotificationService);
  private destroy$ = new Subject<void>();

  displayedColumns: string[] = ['itemNumber', 'itemDate', 'customerId', 'actions'];
  item$!: Observable<Inventory[]>;

    ngOnInit(): void {
      this.initializeItems();
    }

   private initializeItems(): void {
     this.item$ = this.inventoryUiFacade.item$.pipe(takeUntil(this.destroy$));
     this.inventoryUiFacade.error$
       .pipe(
         takeUntil(this.destroy$),
         tap(() => {
           this.dialogService.error(InventoryMessages.dowloadListOrder());
           this.notificationService.error(InventoryMessages.dowloadListOrder());
         })
       )
       .subscribe();
  }

  viewDetails(item: Inventory): void {
    this.actionService.emit({ type: ActionType.VIEW_INVENTORY, payload: item });
  }

  createDetails(): void {
    this.actionService.emit({ type: ActionType.CREATE_INVENTORY });
   }

  deleteItem(item: Inventory): void {
    this.actionService.emit({ type: ActionType.DELETE_INVENTORY, payload: item });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
