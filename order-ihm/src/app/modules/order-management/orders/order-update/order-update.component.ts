import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-order-update',
  templateUrl: './order-update.component.html',
  styleUrls: ['./order-update.component.scss']
})
export class OrderUpdateComponent implements OnInit {
  form!: FormGroup;
  order: any;

  constructor(private route: ActivatedRoute, private orderService: OrderService, private fb: FormBuilder, private router: Router) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.orderService.getOrderById(id).subscribe(o => {
      if (!o) {
        // handle missing order (navigate back or show message)
        return;
      }
      this.order = o;
      this.form = this.fb.group({ status: [o.status], notes: [o.notes] });
    });
  }

  save() {
    if (!this.order) return;
    const updated = { ...this.order, status: this.form.value.status, notes: this.form.value.notes };
    this.orderService.updateOrder(updated).subscribe(() => this.router.navigate([`/orders/${this.order.id}`]));
  }
}

