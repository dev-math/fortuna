import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, _state: RouterStateSnapshot)  {
    const allowedRoles = route.data['roles'] as string[];
    return this.authService.isAuthorized(allowedRoles).pipe(
      tap(isAuthorized => {
        if (!isAuthorized) {
          this.router.navigate(["/login"]);
        }
      })
    )
  }
}
