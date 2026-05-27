import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-status-badge',
  templateUrl: './status-badge.component.html',
  styleUrls: ['./status-badge.component.scss']
})
export class StatusBadgeComponent {
  @Input() status: string = '';

  get color() {
    switch (this.status) {
      case 'Pending': return 'warn';
      case 'Confirmed': return 'accent';
      case 'Processing': return 'primary';
      case 'Shipped': return 'accent';
      case 'Delivered': return 'primary';
      case 'Canceled': return 'warn';
      default: return '';
    }
  }
}

