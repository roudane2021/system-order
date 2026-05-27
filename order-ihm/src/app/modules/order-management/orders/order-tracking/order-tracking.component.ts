import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-order-tracking',
  templateUrl: './order-tracking.component.html',
  styleUrls: ['./order-tracking.component.scss']
})
export class OrderTrackingComponent {
  @Input() tracking?: string;
  @Input() carrier?: string;
}

