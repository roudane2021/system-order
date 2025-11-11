import { Injectable } from '@angular/core';
import {  Observable, Subject } from 'rxjs';
import { ActionEvent } from '../models/action.model';

@Injectable({ providedIn: 'root' })
export class ActionService {
  private _action$ = new Subject<ActionEvent<unknown>>();

  get action$(): Observable<ActionEvent<unknown>> {
    return this._action$.asObservable();
  }

  emit<T>(action: ActionEvent<T>): void {
    this._action$.next(action);
  }

  clear(): void {
    this._action$.next({} as ActionEvent<unknown>);
  }
}
