export interface Inventory {
    itemNumber: string;
    itemDate: string;
    firstName: string;
    lastName: string;
    city: string;
    nationality: string;
    email: string;
    phone: string;
    userName: string;
    password: string;
    items: InventoryItem[];
}

export interface InventoryItem {
    productId: string;
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
    key: string;
    value: string;
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

export interface AppError {
    message: string;
    code: number;
}
