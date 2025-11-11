import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LoggerService {

  private _events: HttpEvent<unknown>[] = [];
  private _errors: HttpErrorResponse[] = [];

  log(event: HttpResponse<unknown>): void {
    if (this._events.length === 10) {
      this._events.pop();
    }
    this._events.unshift(event);
    console.log('%cEvents', 'color: blue; font-size: 24px; font-weight: bold;');
    console.table(this._events);
  }

  logError(error: HttpErrorResponse): void {
    if (this._errors.length === 10) {
      this._errors.pop();
    }
    this._errors.unshift(error);
    console.log('%cErrors', 'color: red; font-size: 24px; font-weight: bold;');
    console.table(this._errors);
  }
}