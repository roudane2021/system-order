import { NgModule } from '@angular/core';


import { OrderRoutingModule } from './order-routing.module';
import { OrderComponent } from './order.component';
import { SharedModule } from '../shared/shared.module';
import { TableDisplayComponent } from './components/table-display/table-display.component';
import { TableFilterComponent } from './components/table-filter/table-filter.component';
import { TablePaginationComponent } from './components/table-pagination/table-pagination.component';
import { OrderService } from './services/order.service';
import { UpdateOrderComponent } from './components/update-order/updat-order.component';
import { OrderUiFacade } from './services/order.ui.facade';
import { CreateOrderComponent } from './components/create-order/create-order.component';
import { OrderItemComponent } from './components/order-item/order-item.component';
import { OrderBusinessFacade } from './services/order.business.facade';



@NgModule({
  declarations: [
    OrderComponent,
    TableDisplayComponent,
    TableFilterComponent,
    TablePaginationComponent,
    UpdateOrderComponent,
    CreateOrderComponent,
    OrderItemComponent
  ],
  imports: [
    SharedModule,
    OrderRoutingModule
  ],
  providers: [
    OrderService,
    OrderBusinessFacade,
    OrderUiFacade
  ]
})
export class OrderModule { }
