export enum ActionType {
  NONE = 'NONE',
  OPEN_PANEL = 'OPEN_PANEL',
  CLOSE_PANEL = 'CLOSE_PANEL',
  VIEW_ORDER = 'VIEW_ORDER',
  DELETE_ORDER = 'DELETE_ORDER',
  UPDATE_ORDER = 'UPDATE_ORDER',
  CREATE_ORDER = 'CREATE_ORDER'
}

export interface ActionEvent<T> {
  type: ActionType;
  payload?: T;
}