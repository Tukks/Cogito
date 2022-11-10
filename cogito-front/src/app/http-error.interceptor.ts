import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {NzMessageService} from "ng-zorro-antd/message";

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private message: NzMessageService) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          window.location.href = "http://localhost:9090/oauth2/authorization/google"
        }
        let errorMsg = '';
        if (error.error instanceof ErrorEvent) {
          errorMsg = `${error.error.message}`;
        } else {
          errorMsg = `Code: ${error.status},  Message: ${error.message}`;
        }
        this.message.error(`An error occurred, detail: ${errorMsg}`);
        return throwError(() => errorMsg);
      })
    );
  }
}
