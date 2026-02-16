import { Component, inject, OnInit } from '@angular/core';
import { InventoryUiFacade } from './services/inventory.ui.facade';
import { Criteria } from './models/inventory.model';

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.scss']
})
export class InventoryComponent implements OnInit {
  private inventoryUiFacade: InventoryUiFacade = inject(InventoryUiFacade);

  ngOnInit(): void {
    this.inventoryUiFacade.init();
  }

  onCriteriaChange(criteria: Criteria[]): void {
    this.inventoryUiFacade.setCriteria(criteria);
  }

  onPageChange(page: number): void {
    this.inventoryUiFacade.setPage(page);
  }
}
