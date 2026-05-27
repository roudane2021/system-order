import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.scss']
})
export class OrderDetailsComponent implements OnInit {
  order: any;
  loading = true;

  constructor(private route: ActivatedRoute, private orderService: OrderService) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.orderService.getOrderById(id).subscribe(o => { this.order = o; this.loading = false; });
  }
}

