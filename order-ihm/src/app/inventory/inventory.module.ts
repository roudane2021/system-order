import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { InventoryComponent } from './inventory.component';
import { InventoryRoutingModule } from './inventory-routing.module';

@NgModule({
  declarations: [
    InventoryComponent,
  ],
  imports: [
    SharedModule,
    InventoryRoutingModule
  ],
  providers: [
  ]
})
export class InventoryModule { }
