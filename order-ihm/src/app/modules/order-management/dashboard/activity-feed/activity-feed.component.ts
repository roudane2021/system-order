import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../../services/dashboard.service';

@Component({
  selector: 'app-activity-feed',
  templateUrl: './activity-feed.component.html',
  styleUrls: ['./activity-feed.component.scss']
})
export class ActivityFeedComponent implements OnInit {
  activities: any[] = [];

  constructor(private dashboard: DashboardService) { }

  ngOnInit(): void {
    this.dashboard.getRecentActivity().subscribe(a => this.activities = a);
  }
}

