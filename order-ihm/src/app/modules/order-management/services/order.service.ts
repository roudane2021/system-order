import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { delay } from 'rxjs/operators';
import { Order } from '../models/order.model';
import { Customer } from '../models/customer.model';
import { Product } from '../models/product.model';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private orders: Order[] = [];

  constructor() {
    // populate mock data
    const customers: Customer[] = [
      { id: 'c1', name: 'Acme Corp', email: 'hello@acme.com', phone: '+33 1 23 45 67 89'},
      { id: 'c2', name: 'Globex', email: 'contact@globex.com', phone: '+33 6 11 22 33 44'},
      { id: 'c3', name: 'Stark Industries', email: 'sales@stark.com', phone: '+1 555 5555' }
    ];

    const products: Product[] = [
      { id: 'p1', name: 'Premium Widget', price: 49.9, sku: 'W-100', stock: 120 },
      { id: 'p2', name: 'Enterprise Gadget', price: 129.0, sku: 'G-200', stock: 30 },
      { id: 'p3', name: 'Accessory Pack', price: 9.99, sku: 'A-300', stock: 500 }
    ];

    const sample = (i: number, cust: Customer): Order => {
      const items = [
        { product: products[i % products.length], quantity: (i % 3) + 1, price: products[i % products.length].price, total: products[i % products.length].price * ((i % 3) + 1) }
      ];
      const total = items.reduce((s, it) => s + it.total, 0);
      const statuses = ['Pending','Confirmed','Processing','Shipped','Delivered','Canceled'] as const;
      return {
        id: `ORD-${1000 + i}`,
        customer: cust,
        items,
        total,
        date: new Date(Date.now() - i * 1000 * 60 * 60 * 24).toISOString(),
        status: statuses[i % statuses.length],
        paymentMethod: i % 2 === 0 ? 'Card' : 'Invoice',
        paymentStatus: i % 3 === 0 ? 'Paid' : 'Pending',
        delivery: { method: i % 2 === 0 ? 'FedEx' : 'DHL', tracking: `TRK${10000 + i}` },
        shipping: { address: '10 rue de la Paix', city: 'Paris', postalCode: '75001' },
        notes: i % 4 === 0 ? 'Gift wrap' : ''
      };
    };

    for (let i = 0; i < 30; i++) {
      this.orders.push(sample(i, customers[i % customers.length]));
    }
  }

  getOrders(): Observable<Order[]> {
    return of(this.orders).pipe(delay(200));
  }

  getOrderById(id: string): Observable<Order | undefined> {
    const o = this.orders.find(x => x.id === id);
    return of(o).pipe(delay(200));
  }

  addOrder(order: Order): Observable<Order> {
    this.orders.unshift(order);
    return of(order).pipe(delay(150));
  }

  updateOrder(order: Order): Observable<Order> {
    const idx = this.orders.findIndex(o => o.id === order.id);
    if (idx >= 0) this.orders[idx] = order;
    return of(order).pipe(delay(150));
  }

  getStatuses(): string[] {
    return ['Pending','Confirmed','Processing','Shipped','Delivered','Canceled'];
  }
}

