import { Customer } from './customer.model';
import { Product } from './product.model';

export interface OrderItem {
  product: Product;
  quantity: number;
  price: number;
  total: number;
}

export interface Shipping {
  address: string;
  city: string;
  postalCode: string;
  country?: string;
}

export interface Order {
  id: string;
  customer: Customer;
  items: OrderItem[];
  total: number;
  date: string;
  status: 'Pending'|'Confirmed'|'Processing'|'Shipped'|'Delivered'|'Canceled';
  paymentMethod: string;
  paymentStatus: 'Paid'|'Pending'|'Failed';
  delivery: { method: string; tracking?: string };
  shipping: Shipping;
  notes?: string;
}

