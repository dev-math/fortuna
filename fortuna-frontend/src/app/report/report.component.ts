import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { CommonModule, DatePipe } from '@angular/common';

interface Report {
  id: string;
  studentUspNumber: string;
  articlesInWriting: number;
  articlesSubmitted: number;
  articlesAccepted: number;
  academicActivities: string;
  researchSummary: string;
  additionalDeclaration: string;
  difficultyDetails?: string;
  createdAt: string;
  feedbacks: any;
}

@Component({
  selector: 'report',
  templateUrl: './report.component.html',
  standalone: true,
  imports: [DatePipe, CommonModule]
})
export class ReportComponent implements OnInit {
  report: Report | null = null;
  isLoading = true;
  roles: string[] | null = null;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const reportId = this.route.snapshot.paramMap.get('id');

    if (reportId) {
      this.loadReport(reportId);
    }

    this.roles = this.authService.currentUser()!!.roles
  }

  navigateToFeedback(reportId: string): void {
    this.router.navigate(['/feedback', reportId]);
  }

  loadReport(reportId: string): void {
    this.isLoading = true;

    let url = '/report/' + reportId;

    if (this.roles?.includes('STUDENT')) {
      url = '/reports/' + reportId;
    }

    this.http.get<Report>("//localhost:8080" + url, { withCredentials: true }).subscribe({
      next: (data) => {
        this.report = data;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar os detalhes do relatório', error);
        this.isLoading = false;
      },
    });
  }
}

