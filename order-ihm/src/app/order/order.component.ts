import {
  Component,
  ComponentRef,
  inject,
  OnDestroy,
  OnInit,
  ViewChild,
  ViewContainerRef,
} from '@angular/core';
import {   filter, switchMap, takeUntil } from 'rxjs';
import { Criteria, Order } from './models/order.model';
import { MatSidenav } from '@angular/material/sidenav';
import { ActionEvent, ActionType } from '../core/models/action.model';
import { OrderUiFacade } from './services/order.ui.facade';
import { CommonDialogPresets } from '../shared/dialogs/dialog-presets';
import { InfoDialogData, BaseComponent } from '../shared/interfaces/index.interface';
import { OrderMessages } from './constants/order-messages';
import { BaseActionListener } from '../core/base/base-action.listener';
import { COMPONENT_MAP } from './constants/component-map.constant';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
})
export class OrderComponent extends BaseActionListener<Order> implements OnInit, OnDestroy {
  
  private orderUiFacade: OrderUiFacade = inject(OrderUiFacade);


  @ViewChild('sidePanel') sidePanel!: MatSidenav;
  @ViewChild('dynamicHost', { read: ViewContainerRef })
  dynamicHost!: ViewContainerRef;
  currentComponentRef?: ComponentRef<BaseComponent>;
 

  public  ngOnInit(): void {
    super.init();
    this.loadOrders();
    
  }

  private loadOrders(): void {
    this.orderUiFacade.init();
  }

    // ------------------------------------
  // 🔹 HANDLE ACTIONS
  // --------------------------------------

    protected  getSupportedActionTypes(): ActionType[] {
    return [
      ActionType.DELETE_ORDER,
      ActionType.UPDATE_ORDER,
      ActionType.VIEW_ORDER,
      ActionType.CREATE_ORDER,
    ];
    }
  
  protected override handleAction(action: ActionEvent<Order>): void {

    switch (action.type) {
      case ActionType.DELETE_ORDER:
        this.confirmAndDelete(action.payload!);
        break;
      default:
        this.openDynamicComponent(action)
        break;
    }
    
  }

    // ------------------------------------
  // 🔹 CONFIRM DELETE (CLEAN + FACADE)
  // ------------------------------------
  private confirmAndDelete(order: Order): void {
   const dialog: InfoDialogData = CommonDialogPresets
        .confirmGeneric(OrderMessages.confirmDelete(order));
      
      this.dialogService.confirm(dialog).pipe(
        takeUntil(this.destroy$),
        filter(confirmed => confirmed),
        switchMap(() => this.retryWithUserConfirmation(
          this.orderUiFacade.deleteOrder(order),
          OrderMessages.deleteRetry(order),
          3
        )),
  
      )
        .subscribe({
          next: () => this.dialogService.success(OrderMessages.deleteSuccess(order)),
          error: () => this.dialogService.error(OrderMessages.deleteError(order))
        });
  }

  // ------------------------------------
  // 🔹 OPEN PANEL (CREATE / UPDATE / VIEW)
  // ------------------------------------
  private openDynamicComponent(action: ActionEvent<Order>): void {
    const componentType = COMPONENT_MAP[action.type];
    if (!componentType) return;

    this.currentComponentRef = this.dynamicLoaderService.createComponent(
      this.dynamicHost,
      componentType,
      { data: action.payload },
      () => this.closePanel()
    );

    this.sidePanel.open();
  }


    // ------------------------------------
  // 🔹 FILTER / PAGINATION / DESTROY
  // ------------------------------------
  public onPageReceived(page: number) {
    this.orderUiFacade.setPage(page);
  }
  public onCriteriaReceived(criteria: Criteria[]) {
    this.orderUiFacade.setCriteria(criteria);
  }

  public closePanel() {
    this.currentComponentRef?.destroy();
    this.currentComponentRef = undefined;
    this.sidePanel.close();
    this.dynamicHost.clear();
  }

  

  public  ngOnDestroy(): void {
    this.orderUiFacade.destroy();
    super.destroy();
  }
}
