import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-analytics-cards',
  templateUrl: './analytics-cards.component.html',
  styleUrls: ['./analytics-cards.component.scss']
})
export class AnalyticsCardsComponent {
  @Input() loading = false;

  cards = [
    { title: 'Total Orders', value: 1245, icon: 'receipt_long', color: 'primary' },
    { title: 'Pending', value: 54, icon: 'hourglass_top', color: 'warn' },
    { title: 'Delivered', value: 1080, icon: 'local_shipping', color: 'accent' },
    { title: 'Revenue', value: 156420, icon: 'currency_exchange', color: 'primary' }
  ];
}

