import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, map } from "rxjs";
import { User } from "./user";
import { Router } from "@angular/router";

interface RegisterStudentRequest {
  uspNumber: string;
  role: string;
  course: string;
  advisor: string;
  lattesProfile: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private userSubject = new BehaviorSubject<User | null>(this.getUserFromStorage());

  constructor(private http: HttpClient, private router: Router) {
    if (!this.userSubject.value) {
      this.loadUser();
    }
  }

  getUserDetails(): Observable<User | null> {
    return this.userSubject.asObservable();
  }
  
  currentUser() {
    return this.userSubject.value;
  }

  loadUser() {
    this.http.get<User>("//localhost:8080/me", { withCredentials: true }).subscribe({
      next: (user: User) => {
        console.debug("Dados do usuario carregados com sucesso", user);
        localStorage.setItem("user", JSON.stringify(user));
        this.userSubject.next(user);
      },
      error: (e) => {
        console.error("Erro ao buscar dados do usuario logado ", e);
        this.logout();
      }
    });
  }

  isAuthorized(allowedRoles: string[]): Observable<boolean> {
    return this.getUserDetails().pipe(
      map(user => {
        console.debug("Verificando autorizacoes do usuario - ", user)
        return user !== null && user.hasAnyRole(allowedRoles);
      })
    )
  }

  logout() {
    localStorage.removeItem("user");
    this.userSubject.next(null);
    this.router.navigate(["/login"])
  }

  registerStudent(request: RegisterStudentRequest): Observable<any> {
    return this.http.post("//localhost:8080/signup", request, { withCredentials: true }).pipe(
      map(() => {
        console.debug("Estudante cadastrado. Recarregando informacoes do usuario");
        this.loadUser();
      })
    );
  }

  private getUserFromStorage(): User | null {
    const user = localStorage.getItem("user");
    if (!user) {
      return null;
    }
    const userData = JSON.parse(user);
    return new User(userData.name, userData.email, userData.roles);
  }
}
