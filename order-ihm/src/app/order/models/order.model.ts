export enum OrderStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED'
}

export interface Order {
  orderNumber: string;
  orderDate: string; 
  firstName: string;
  lastName: string;
  city: string;
  nationality: string;
  email: string;
  phone: string;
  userName: string;
  password: string;
  status: OrderStatus;
  items: OrderItem[];
}

export interface OrderItem {
  productId: number;
  quantity: number;
  price: number;
}

export interface PageResult<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface Page {
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface Criteria {
  name: string;
  value?: unknown;
  listValue?: unknown[];
  operator: Operator;
}

export enum Operator {
  EQUALS = 'EQUALS',
  NOT_EQUALS = 'NOT_EQUALS',
  LESS_THAN = 'LESS_THAN',
  LESS_OR_EQUAL = 'LESS_OR_EQUAL',
  GREATER_THAN = 'GREATER_THAN',
  GREATER_OR_EQUAL = 'GREATER_OR_EQUAL',
  IN = 'IN',
  NOT_IN = 'NOT_IN',
  LIKE = 'LIKE'
}

export interface AppError extends Error {
  code?: string;
  context?: string;
}

