import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../../services/dashboard.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.scss']
})
export class DashboardPageComponent implements OnInit {
  stats$!: Observable<any>;

  constructor(private dashboard: DashboardService) { }

  ngOnInit(): void {
    this.stats$ = this.dashboard.getSummary();
  }
}

