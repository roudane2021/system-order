import { inject, Injectable } from "@angular/core";
import { MatSnackBar } from "@angular/material/snack-bar";

@Injectable({ providedIn: 'root' })
export class NotificationService {
    private snackBar: MatSnackBar = inject(MatSnackBar);

    success(message: string) {
    this.snackBar.open(message, 'OK', { duration: 3000, panelClass: ['success'] });
  }

  error(message: string) {
    this.snackBar.open(message, 'Fermer', { duration: 5000, panelClass: ['error'] });
  }
}