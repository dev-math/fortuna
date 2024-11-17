import { Component } from "@angular/core";

@Component({
  selector: 'login',
  standalone: true,
  templateUrl: './login.component.html',
})
export class LoginComponent {
  login(): void {
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
  }
}
