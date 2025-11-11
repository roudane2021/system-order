import { ActionType } from '../../core/models/action.model';
import { DynamicComponentType } from '../../shared/interfaces/base-component.interface';
import { CreateOrderComponent } from '../components/create-order/create-order.component';
import { UpdateOrderComponent } from '../components/update-order/updat-order.component';


export const COMPONENT_MAP: Partial<Record<ActionType, DynamicComponentType>> = {
  VIEW_ORDER: UpdateOrderComponent,
  CREATE_ORDER: CreateOrderComponent
};
