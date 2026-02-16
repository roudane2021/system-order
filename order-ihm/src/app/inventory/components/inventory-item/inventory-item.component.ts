import { Component, Input } from '@angular/core';
import { InventoryItem } from '../../models/inventory.model';

@Component({
  selector: 'app-inventory-item',
  templateUrl: './inventory-item.component.html',
  styleUrls: ['./inventory-item.component.scss']
})
export class InventoryItemComponent {
  @Input() item!: InventoryItem;
}
