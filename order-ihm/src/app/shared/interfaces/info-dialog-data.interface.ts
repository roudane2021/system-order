export interface InfoDialogData {
  title?: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  type?: DialogType;
}

export enum DialogType {
  INFO = 'info',
  SUCCESS = 'success',
  WARNING = 'warning',
  ERROR = 'error'
}