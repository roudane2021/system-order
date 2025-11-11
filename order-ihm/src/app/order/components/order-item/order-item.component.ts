import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-order-item',
  templateUrl: './order-item.component.html',
  styleUrls: ['./order-item.component.scss']
})
export class OrderItemComponent {

  @Input() itemForm!: FormGroup;
  @Input() index!: number;
  @Output() remove = new EventEmitter<number>();

  public onRemove(): void {
    this.remove.emit(this.index);
  }

}
