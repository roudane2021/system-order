import { HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from "@angular/common/http";
import {  finalize, Observable } from "rxjs";
import { inject } from "@angular/core";
import { SpinnerService } from '../services/spinner.service';


export const spinnnerInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> => {
    
  const spinnerService: SpinnerService = inject(SpinnerService);
  spinnerService.show();
  return next(req)
    .pipe(
      finalize(() => spinnerService.hide())
    );
}