import { Injectable } from "@angular/core";
import { environment } from '../../../environments/environment';

@Injectable({providedIn: 'root'})
export class LogService {

    private enableDebug = environment.enableDebug;

    debug(message: string, ...args: unknown[]): void {
        if (this.enableDebug) {
      console.log('[DEBUG]', message, ...args);
    }
  }

  info(message: string, ...args: unknown[]): void {
    console.info('[INFO]', message, ...args);
  }

  warn(message: string, ...args: unknown[]): void {
    console.warn('[WARN]', message, ...args);
  }

  error(message: string, ...args: unknown[]): void {
    console.error('[ERROR]', message, ...args);
  }
}