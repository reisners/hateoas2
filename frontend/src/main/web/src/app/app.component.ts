import { Component, OnInit } from '@angular/core';
import { Restangular } from 'ngx-restangular';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'hateoas2-frontend';
  customers: any[] = [];

  constructor(private restangular: Restangular) {}

  ngOnInit() {
    this.loadCustomers();
  }

  loadCustomers() {
    this.restangular.all('customers').getList().subscribe(customers => {
      this.customers = customers;
    }, error => {
      console.error('Error loading customers', error);
    });
  }

  addCustomer(name: string, email: string) {
    const newCustomer = { name, email };
    this.restangular.all('customers').post(newCustomer).subscribe(() => {
      this.loadCustomers();
    }, error => {
      console.error('Error adding customer', error);
    });
  }
}
