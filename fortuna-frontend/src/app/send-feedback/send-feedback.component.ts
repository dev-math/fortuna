import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';
import { MatCommonModule } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'send-feedback',
  standalone: true,
  templateUrl: './send-feedback.component.html',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatCommonModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
  ],
})
export class SendFeedbackComponent {
  feedbackForm: FormGroup;
  isSubmitted = false;
  errorMessage: string | null = null;
  reportId: string | null = null;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {
    this.feedbackForm = this.fb.group({
      feedbackDetails: ['', Validators.required],
      reportSituation: ['', Validators.required],
    });

    this.reportId = this.route.snapshot.paramMap.get('id');
  }

  onSubmit(): void {
    this.isSubmitted = true;

    if (this.feedbackForm.invalid || !this.reportId) {
      this.errorMessage = 'Por favor, preencha todos os campos obrigatórios corretamente.';
      return;
    }

    const feedbackRequest = {
      authorEmail: this.authService.currentUser()?.email,
      reportId: this.reportId,
      ...this.feedbackForm.value,
    };

    this.http
      .post('//localhost:8080/feedbacks', feedbackRequest, { withCredentials: true })
      .subscribe({
        next: () => {
          alert('Feedback enviado com sucesso!');
          this.feedbackForm.reset();
          this.isSubmitted = false;
          this.errorMessage = null;
        },
        error: (err) => {
          console.error('Erro ao enviar feedback:', err);
          this.errorMessage = 'Ocorreu um erro ao enviar o feedback. Tente novamente.';
        },
      });
  }
}
