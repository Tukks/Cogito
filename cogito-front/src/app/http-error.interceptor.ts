import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {NzMessageService} from "ng-zorro-antd/message";
import {AuthService} from "./service/auth-service.service";
import {ActivatedRoute, Router} from "@angular/router";


@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private message: NzMessageService, private authService: AuthService,
              private router: Router, private activatedRoute: ActivatedRoute) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    let headers = new HttpHeaders();
    headers = headers.set('Authorization', "Bearer " + this.authService.getCookie());
    const requestClone = request.clone({headers});
    return next.handle(requestClone).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          this.authService.removeCookie();

          this.router.navigateByUrl(`/login?redirectTo=${this.router.url}`).then();
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
