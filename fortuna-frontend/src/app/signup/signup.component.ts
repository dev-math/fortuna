import { CommonModule } from "@angular/common";
import { Component, Injectable, OnInit } from "@angular/core";
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import {MatError, MatFormFieldModule} from '@angular/material/form-field';
import {MatRadioModule} from '@angular/material/radio';
import {MatButtonModule} from '@angular/material/button';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { AuthService } from "../auth/auth.service";

@Injectable({providedIn: 'root'})
@Component({
  selector: 'signup',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatSelectModule, MatRadioModule, MatButtonModule, CommonModule, ReactiveFormsModule, MatError],
  templateUrl: './signup.component.html'
})
export class SignUpComponent {
  signupForm: FormGroup;
  isSubmitted = false;
  errorMessage = '';

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.signupForm = this.fb.group({
      uspNumber: ['', [Validators.required, Validators.pattern('^[0-9]{8}$')]],
      role: ['STUDENT', Validators.required],
      course: ['', [Validators.required, Validators.pattern('Mestrado|Doutorado')]],
      advisor: ['', [Validators.required, Validators.minLength(3)]],
      lattesProfile: ['', [Validators.required, Validators.pattern('^(http|https)://.*$')]]
    });
  }

  onSubmit() {
    console.log(this.signupForm.value.uspNumber)
    this.isSubmitted = true;
    if (!this.signupForm.valid) {
      this.errorMessage = "Atente-se aos campos invalidos";
      return;
    }
    const request = {...this.signupForm.value, role: 'STUDENT'};
    console.log("Request ", request);
    this.authService.registerStudent(request).subscribe({
      next: () => {
        console.debug("Estudante registrado com sucesso. Redirecionando para home");
      },
      error: (e) => {
        this.errorMessage = "Erro no cadastro. Tente novamente mais tarde";
        console.error("Erro ao registrar o estudante. Error ", e);
      }
    })
  }
}
