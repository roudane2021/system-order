import { inject } from '@angular/core';
import { Subject, takeUntil, filter, tap, Observable, retry, switchMap, throwError, of } from 'rxjs';
import { ActionEvent, ActionType } from 'src/app/core/models/action.model';
import { ActionService } from 'src/app/core/services/action.service';
import { DynamicLoaderService } from 'src/app/core/services/dynamic-load.service';
import { DialogService } from 'src/app/core/services/dialog.service';
import { CommonDialogPresets } from '../../shared/dialogs/dialog-presets';
import { InfoDialogData } from '../../shared/interfaces/info-dialog-data.interface';
import { LogService } from '../services/log.service';


export abstract class BaseActionListener<T> {
    protected destroy$ = new Subject<void>();
    protected actionService: ActionService = inject(ActionService);
    protected dynamicLoaderService: DynamicLoaderService =
      inject(DynamicLoaderService);
  protected dialogService: DialogService = inject(DialogService);
  protected logService: LogService = inject(LogService);

    public init(): void {
      this.listenActions();
  }

  protected abstract getSupportedActionTypes(): ActionType[];

  protected abstract handleAction(action: ActionEvent<T>): void;

  protected listenActions(): void {
    this.actionService.action$
      .pipe(
        takeUntil(this.destroy$),
        filter((action): action is ActionEvent<T> =>
          this.getSupportedActionTypes().includes(action.type)
        ),
        tap((action) => {
          this.logService.debug(`Handling action : ${action.type}`);
          this.handleAction(action)
        })
      )
      .subscribe();
  }

  protected retryWithUserConfirmation<T>(source$: Observable<T>, message : string, maxRetries: number) {
      
       return source$.pipe(
         retry({
           count: maxRetries,
           delay: (error) => {
             const dialog: InfoDialogData = CommonDialogPresets.confirmGeneric(message);
              return this.dialogService.confirm(dialog).pipe(
            switchMap(retry => retry ? of(undefined) : throwError(() => error))
          );
           }
        })
      )
    }

  protected destroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
