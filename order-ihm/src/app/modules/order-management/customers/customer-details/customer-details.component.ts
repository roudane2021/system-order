import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-customer-details',
  templateUrl: './customer-details.component.html',
  styleUrls: ['./customer-details.component.scss']
})
export class CustomerDetailsComponent implements OnInit {
  customer: any;
  loading = true;
  constructor(private route: ActivatedRoute, private customerService: CustomerService) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.customerService.getCustomerById(id).subscribe(c => { this.customer = c; this.loading = false; });
  }
}

