import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OrderManagementShellComponent } from './order-management-shell/order-management-shell.component';
import { DashboardPageComponent } from './dashboard/dashboard-page/dashboard-page.component';
import { OrdersListComponent } from './orders/order-list/order-list.component';
import { OrderDetailsComponent } from './orders/order-details/order-details.component';
import { OrderCreateComponent } from './orders/order-create/order-create.component';
import { CustomersListComponent } from './customers/customer-list/customer-list.component';
import { CustomerDetailsComponent } from './customers/customer-details/customer-details.component';

const routes: Routes = [
  {
    path: '',
    component: OrderManagementShellComponent,
    children: [
      { path: '', component: DashboardPageComponent },
      { path: 'dashboard', component: DashboardPageComponent },
      { path: 'orders', component: OrdersListComponent },
      { path: 'orders/create', component: OrderCreateComponent },
      { path: 'orders/:id', component: OrderDetailsComponent },
      { path: 'customers', component: CustomersListComponent },
      { path: 'customers/:id', component: CustomerDetailsComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OrderManagementRoutingModule { }

