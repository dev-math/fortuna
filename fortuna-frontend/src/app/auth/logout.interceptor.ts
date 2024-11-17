import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable, Injector } from "@angular/core";
import { Observable, catchError, throwError } from "rxjs";
import { AuthService } from "./auth.service";

@Injectable()
export class LogoutInterceptor implements HttpInterceptor {
  constructor(private injector: Injector) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError(err => {
      const auth = this.injector.get(AuthService);
      console.log("teste", request);
      console.log("err = ", err)
      if ([401, 403].includes(err.status) && auth.currentUser()) {
        // auth.logout();
      }

      const error = err.error?.message || err.statusText;
      console.error("Request not authorized", err);
      return throwError(() => error);
    }))
  }
}
