import { DialogType, InfoDialogData } from '../interfaces/index.interface';

export const CommonDialogPresets = {
  confirmGeneric: (message: string): InfoDialogData => ({
    title: 'Confirmation',
    message,
    confirmText: 'Oui',
    cancelText: 'Non',
    type: DialogType.WARNING
  }),

  successGeneric: (message: string): InfoDialogData => ({
    title: 'Succès',
    message,
    confirmText: 'OK',
    type: DialogType.SUCCESS
  }),

  errorGeneric: (message: string): InfoDialogData => ({
    title: 'Erreur',
    message,
    confirmText: 'Fermer',
    type: DialogType.ERROR
  }),
};
