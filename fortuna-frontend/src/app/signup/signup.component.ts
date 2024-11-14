import { CommonModule } from "@angular/common";
import { Component, Injectable, OnInit } from "@angular/core";
import { AuthService } from "../auth/auth.service";
import { User } from "../auth/user";

@Injectable({providedIn: 'root'})
@Component({
  selector: 'signup',
  standalone: true,
  imports: [CommonModule],
  template: `
  <h1>Signup screen</h1>
  <div *ngIf="user; else loading">
    <p>User email {{ user.email }}</p>
    <p>User name {{ user.name }}</p>
    <p>User roles {{ user.roles.join('","') }}</p>
  </div>
  <ng-template #loading>Carregando...</ng-template>
`
})
export class SignUpComponent implements OnInit {
  public user: User | null = null;

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.authService.getUserDetails().subscribe(user => {
      this.user = user
    })
  }
}
