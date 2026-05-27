import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { delay } from 'rxjs/operators';
import { Customer } from '../models/customer.model';

@Injectable({ providedIn: 'root' })
export class CustomerService {
  private customers: Customer[] = [
    { id: 'c1', name: 'Acme Corp', email: 'hello@acme.com', phone: '+33 1 23 45 67 89', company: 'Acme' },
    { id: 'c2', name: 'Globex', email: 'contact@globex.com', phone: '+33 6 11 22 33 44', company: 'Globex' },
    { id: 'c3', name: 'Stark Industries', email: 'sales@stark.com', phone: '+1 555 5555', company: 'Stark' }
  ];

  getCustomers(): Observable<Customer[]> { return of(this.customers).pipe(delay(180)); }
  getCustomerById(id: string): Observable<Customer | undefined> { return of(this.customers.find(c => c.id === id)).pipe(delay(140)); }
}

