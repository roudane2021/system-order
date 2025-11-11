import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { InventoryComponent } from './inventory.component';
import { InventoryRoutingModule } from './inventory-routing.module';
import { InventoryService } from './services/inventory.service';
import { InventoryBusinessFacade } from './services/inventory.business.facade';
import { InventoryUiFacade } from './services/inventory.ui.facade';
import { TableDisplayComponent } from './components/table-display/table-display.component';
import { TableFilterComponent } from './components/table-filter/table-filter.component';
import { TablePaginationComponent } from './components/table-pagination/table-pagination.component';
import { CreateItemComponent } from './components/create-item/create-item.component';
import { UpdateItemComponent } from './components/update-item/update-item.component';
import { InventoryItemComponent } from './components/inventory-item/inventory-item.component';

@NgModule({
  declarations: [
    InventoryComponent,
    TableDisplayComponent,
    TableFilterComponent,
    TablePaginationComponent,
    CreateItemComponent,
    UpdateItemComponent,
    InventoryItemComponent
  ],
  imports: [
    SharedModule,
    InventoryRoutingModule
  ],
  providers: [
    InventoryService,
    InventoryBusinessFacade,
    InventoryUiFacade
  ]
})
export class InventoryModule { }
