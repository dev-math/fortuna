import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component'
import { SignUpComponent } from './signup/signup.component';
import { AuthGuard } from './auth/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignUpComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_UNKNOWN'] }},
];
