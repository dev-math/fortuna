import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';
import { MatCommonModule } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'report-submit',
  standalone: true,
  templateUrl: './report-submit.component.html',
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatCommonModule, MatInputModule, MatButtonModule]
})
export class SubmitReportComponent {
  reportForm: FormGroup;
  isSubmitted = false;
  errorMessage: string | null = null;

  constructor(private fb: FormBuilder, private http: HttpClient, private authService: AuthService) {
    this.reportForm = this.fb.group({
      articlesInWriting: [0, [Validators.required, Validators.min(0)]],
      articlesSubmitted: [0, [Validators.required, Validators.min(0)]],
      articlesAccepted: [0, [Validators.required, Validators.min(0)]],
      academicActivities: ['', Validators.required],
      researchSummary: ['', Validators.required],
      additionalDeclaration: ['', Validators.required],
      difficultyDetails: ['']
    });
  }

  onSubmit(): void {
    this.isSubmitted = true;

    if (this.reportForm.invalid) {
      this.errorMessage = 'Por favor, preencha todos os campos obrigatórios corretamente.';
      return;
    }

    this.http.post('//localhost:8080/reports', { ...this.reportForm.value, studentEmail: this.authService.currentUser()?.email }, { withCredentials: true }).subscribe({
      next: () => {
        alert('Relatório enviado com sucesso!');
        this.reportForm.reset();
        this.isSubmitted = false;
        this.errorMessage = null;
      },
      error: (err) => {
        console.error('Erro ao enviar relatório:', err);
        this.errorMessage = 'Ocorreu um erro ao enviar o relatório. Tente novamente.';
      }
    });
  }
}
