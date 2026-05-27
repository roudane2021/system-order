import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { delay } from 'rxjs/operators';
import { DashboardSummary, Activity } from '../models/dashboard.model';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  constructor() { }

  getSummary(): Observable<DashboardSummary> {
    const summary: DashboardSummary = {
      totalOrders: 1245,
      pendingOrders: 54,
      deliveredOrders: 1080,
      canceledOrders: 30,
      revenue: 156420
    };
    return of(summary).pipe(delay(200));
  }

  getRecentActivity(): Observable<Activity[]> {
    const items: Activity[] = [
      { id: 'a1', title: 'New order ORD-1001', subtitle: 'by Acme Corp', time: '2h ago' },
      { id: 'a2', title: 'Order shipped ORD-998', subtitle: 'Tracking TRK10055', time: '5h ago' },
      { id: 'a3', title: 'Payment received ORD-995', subtitle: 'Card • $1,290', time: '1d ago' }
    ];
    return of(items).pipe(delay(220));
  }
}

