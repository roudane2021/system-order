import { HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from "@angular/common/http";
import { catchError, Observable, throwError} from "rxjs";
import {  LoggerService } from '../services/logger.service';
import { inject } from "@angular/core";


export const loggerErrorInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> => {
    const loggerService: LoggerService = inject(LoggerService);

    return next(req).pipe(
        catchError(error => {
            loggerService.logError(error);
            return throwError(() => error)
        })
    );
}

