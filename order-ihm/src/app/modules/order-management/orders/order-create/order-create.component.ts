import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomerService } from '../../services/customer.service';
import { OrderService } from '../../services/order.service';
import { Router } from '@angular/router';
import { Order } from '../../models/order.model';

@Component({
  selector: 'app-order-create',
  templateUrl: './order-create.component.html',
  styleUrls: ['./order-create.component.scss']
})
export class OrderCreateComponent implements OnInit {
  form!: FormGroup;
  customers: any[] = [];

  constructor(private fb: FormBuilder, private customerService: CustomerService, private orderService: OrderService, private router: Router) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      customerId: ['', Validators.required],
      items: this.fb.array([]),
      shipping: this.fb.group({ address: ['', Validators.required], city: ['', Validators.required], postalCode: [''] }),
      paymentMethod: ['Card', Validators.required]
    });

    this.customerService.getCustomers().subscribe(c => this.customers = c);
    this.addItem();
  }

  get items(): FormArray { return this.form.get('items') as FormArray; }

  addItem() { this.items.push(this.fb.group({ productId: ['p1'], name: ['Premium Widget'], price: [49.9], quantity: [1, Validators.min(1)] })); }
  removeItem(i: number) { this.items.removeAt(i); }

  submit() {
    if (this.form.invalid) return;
    const customer = this.customers.find(c => c.id === this.form.value.customerId);
    const items = this.form.value.items.map((it: any) => ({ product: { id: it.productId, name: it.name, price: it.price }, quantity: it.quantity, price: it.price, total: it.price * it.quantity }));
    const total = items.reduce((s: number, it: any) => s + it.total, 0);
    const order: Order = {
      id: `ORD-${Math.floor(Math.random()*90000)+1000}`,
      customer: customer!,
      items,
      total,
      date: new Date().toISOString(),
      status: 'Pending',
      paymentMethod: this.form.value.paymentMethod,
      paymentStatus: 'Pending',
      delivery: { method: 'Carrier' },
      shipping: this.form.value.shipping
    } as Order;

    this.orderService.addOrder(order).subscribe(() => this.router.navigate(['/orders']));
  }
}

