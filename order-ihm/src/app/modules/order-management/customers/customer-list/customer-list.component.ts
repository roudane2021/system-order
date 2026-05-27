import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-customers-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.scss']
})
export class CustomersListComponent implements OnInit {
  customers: any[] = [];
  constructor(private customerService: CustomerService) { }

  ngOnInit(): void {
    this.customerService.getCustomers().subscribe(c => this.customers = c);
  }
}

