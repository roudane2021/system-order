import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { delay } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AnalyticsService {
  constructor() { }

  getRevenueSeries(): Observable<any> {
    const data = {
      labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
      series: [12000, 15000, 18000, 14000, 20000, 22000]
    };
    return of(data).pipe(delay(250));
  }
}

