import { HttpClient } from "@angular/common/http";
import { CookieService } from "ngx-cookie-service"
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { map, tap } from "rxjs/operators";
import { User } from "./user";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private userSubject = new BehaviorSubject<User | null>(null);

  constructor(private http: HttpClient, private cookies: CookieService) { }

  getUserDetails(): Observable<User | null> {
    if (!this.userSubject.value) {
      this.http.get<User>("http://localhost:8080/me", { withCredentials: true })
        .pipe(
          tap((user: User) => {
            console.log("Current user - GET /me", user);
            this.userSubject.next(user);
          }),
          tap({
            error: (error) => {
              console.error("Login error: ", error);
              this.userSubject.next(null);
            }
          })
        )
        .subscribe();
    }
    
    return this.userSubject.asObservable();
  }

  public isAuthorized(allowedRoles: string[]): Observable<boolean> {
    if (this.cookies.get("JSESSIONID") === null) {
      console.log("Usuario nao autenticado");
    }

    return this.getUserDetails().pipe(
      map(user => {
        console.log("User in isAuthorized: ", user);
        return user !== null && user.hasAnyRole(allowedRoles);
      })
    );
  }
}
