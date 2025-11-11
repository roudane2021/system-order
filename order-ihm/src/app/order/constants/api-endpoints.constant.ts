import { environment } from "src/environments/environment";


export const API_ENDPOINTS_ORDERS = {
  GET_ORDER: `${environment.apiBaseUrl}${environment.endpoints.orders}`,
  SAVE_ORDER: `${environment.apiBaseUrl}${environment.endpoints.orders}`,
  DELETE_ORDER: `${environment.apiBaseUrl}${environment.endpoints.orders}`,
};
