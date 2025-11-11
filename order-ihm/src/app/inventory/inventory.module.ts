import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { InventoryComponent } from './inventory.component';
import { InventoryRoutingModule } from './inventory-routing.module';
import { InventoryService } from './services/inventory.service';
import { InventoryBusinessFacade } from './services/inventory.business.facade';
import { InventoryUiFacade } from './services/inventory.ui.facade';

@NgModule({
  declarations: [
    InventoryComponent,
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
