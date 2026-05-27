import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { OrderService } from '../../services/order.service';
import { Order } from '../../models/order.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-orders-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.scss']
})
export class OrdersListComponent implements AfterViewInit {
  displayedColumns: string[] = ['id', 'customer', 'status', 'amount', 'date', 'payment', 'delivery', 'actions'];
  dataSource = new MatTableDataSource<Order>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  search = '';

  constructor(private orderService: OrderService, private router: Router) { }

  ngAfterViewInit(): void {
    this.orderService.getOrders().subscribe(orders => {
      this.dataSource.data = orders;
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  applyFilter(value: string) {
    this.dataSource.filter = value.trim().toLowerCase();
  }

  view(order: Order) {
    this.router.navigate([`/orders/${order.id}`]);
  }
}

