import { inject, Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { InfoDialogComponent } from '../../shared/components/info-dialog/info-dialog.component';
import { DialogType, InfoDialogData } from '../../shared/interfaces/index.interface';



@Injectable({
  providedIn: 'root'
})
export class DialogService {

  private dialog = inject(MatDialog);

  /** 🔹 Ouvre un simple message d'information */
  info(message: string, title = 'Information'): void {

    this.dialog.open(InfoDialogComponent, {
      width: '400px',
      data: {
        title,
        message,
        type: 'info',
        confirmText: 'OK'
      } as InfoDialogData,
    });
  }

  /** 🔹 Ouvre un message de succès */
  success(message: string, title = 'Succès'): void {
    this.dialog.open(InfoDialogComponent, {
      width: '400px',
      data: {
        title,
        message,
        type: DialogType.SUCCESS,
        confirmText: 'Fermer'
      } as InfoDialogData,
    });
  }

  /** 🔹 Ouvre un message d’erreur */
  error(message: string, title = 'Erreur'): void {
    this.dialog.open(InfoDialogComponent, {
      width: '400px',
      data: {
        title,
        message,
        type: DialogType.ERROR,
        confirmText: 'Fermer'
      } as InfoDialogData,
    });
  }

  /** 🔹 Demande une confirmation (Oui / Non) */
  confirm(data: InfoDialogData ): Observable<boolean> {
    const dialogRef = this.dialog.open(InfoDialogComponent, {
      width: '400px',
      data: {
        title: data.title ?? 'Confirmation',
        message: data.message,
        type: data.type ?? DialogType.WARNING,
        confirmText: data.confirmText ?? 'Oui',
        cancelText: data.cancelText ?? 'Non'
      } as InfoDialogData,
    });

    return dialogRef.afterClosed(); 
  }
}

