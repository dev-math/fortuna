import { Component } from "@angular/core";
import { MatToolbarModule } from '@angular/material/toolbar'
import { MatButtonModule } from '@angular/material/button'
import { MatIconModule } from '@angular/material/icon'

@Component({
  selector: 'login',
  standalone: true,
  imports: [MatToolbarModule, MatButtonModule, MatIconModule],
  templateUrl: './login.component.html',
})
export class LoginComponent {
  login(): void {
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
  }
}
