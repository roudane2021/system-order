import { environment } from "src/environments/environment";

export const API_ENDPOINTS_INVENTORY = {
    GET_INVENTORY: `${environment.apiBaseUrl}${environment.endpoints.inventory}`,
    SAVE_INVENTORY: `${environment.apiBaseUrl}${environment.endpoints.inventory}`,
    DELETE_INVENTORY: `${environment.apiBaseUrl}${environment.endpoints.inventory}`,
};
