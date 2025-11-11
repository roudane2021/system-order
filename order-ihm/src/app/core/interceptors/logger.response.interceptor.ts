import { HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest, HttpResponse } from "@angular/common/http";
import { filter, Observable, tap } from "rxjs";
import {  LoggerService } from '../services/logger.service';
import { inject } from "@angular/core";


export const loggerResponseInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> => {
    const loggerService: LoggerService = inject(LoggerService);

    return next(req).pipe(
        filter(isHttpResponse),
        tap(response => loggerService.log(response))
    )
}

const  isHttpResponse = (event: HttpEvent<unknown>): event is HttpResponse<unknown> => {
  return event instanceof HttpResponse;
}