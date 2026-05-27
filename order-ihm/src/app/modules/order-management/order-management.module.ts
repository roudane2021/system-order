import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { OrderManagementRoutingModule } from './order-management-routing.module';
import { MaterialModule } from './shared/material.module';
import { OrderManagementShellComponent } from './order-management-shell/order-management-shell.component';
import { DashboardPageComponent } from './dashboard/dashboard-page/dashboard-page.component';
import { AnalyticsCardsComponent } from './dashboard/analytics-cards/analytics-cards.component';
import { ActivityFeedComponent } from './dashboard/activity-feed/activity-feed.component';
import { OrdersListComponent } from './orders/order-list/order-list.component';
import { OrderDetailsComponent } from './orders/order-details/order-details.component';
import { OrderCreateComponent } from './orders/order-create/order-create.component';
import { OrderUpdateComponent } from './orders/order-update/order-update.component';
import { OrderStatusComponent } from './orders/order-status/order-status.component';
import { OrderTrackingComponent } from './orders/order-tracking/order-tracking.component';
import { CustomersListComponent } from './customers/customer-list/customer-list.component';
import { CustomerDetailsComponent } from './customers/customer-details/customer-details.component';
import { StatusBadgeComponent } from './shared/components/status-badge/status-badge.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    OrderManagementShellComponent,
    DashboardPageComponent,
    AnalyticsCardsComponent,
    ActivityFeedComponent,
    OrdersListComponent,
    OrderDetailsComponent,
    OrderCreateComponent,
    OrderUpdateComponent,
    OrderStatusComponent,
    OrderTrackingComponent,
    CustomersListComponent,
    CustomerDetailsComponent,
    StatusBadgeComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    OrderManagementRoutingModule,
    RouterModule
  ]
})
export class OrderManagementModule { }

