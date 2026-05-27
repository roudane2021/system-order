import { Component } from '@angular/core';

@Component({
  selector: 'app-order-management-shell',
  templateUrl: './order-management-shell.component.html',
  styleUrls: ['./order-management-shell.component.scss']
})
export class OrderManagementShellComponent {
  isCollapsed = false;
  mobile = false;

  toggleSidenav() {
    this.isCollapsed = !this.isCollapsed;
  }
}

