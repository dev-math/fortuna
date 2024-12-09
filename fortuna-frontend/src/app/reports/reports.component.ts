import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatListModule } from '@angular/material/list';
import { RouterLink } from '@angular/router';
import { CommonModule, DatePipe } from '@angular/common';
import { AuthService } from '../auth/auth.service';

interface Report {
  id: string;
  articlesInWriting: number;
  articlesSubmitted: number;
  articlesAccepted: number;
  academicActivities: string;
  createdAt: string;
}

@Component({
  imports: [MatPaginator, RouterLink, DatePipe, CommonModule, MatListModule],
  standalone: true,
  selector: 'reports',
  templateUrl: './reports.component.html',
})
export class ReportsComponent implements OnInit {
  reports: Report[] = [];
  totalReports = 0;
  pageSize = 10;
  currentPage = 0;
  isLoading = false;

  constructor(private http: HttpClient, private authService: AuthService) {}

  ngOnInit(): void {
    this.loadReports();
  }

  loadReports(): void {
    this.isLoading = true;

    const studentEndpoint = '/reports';
    const professorEndpoint = '/api/professor/reports';
    const ccpEndpoint = '/api/ccp/reports';

    const roles = this.authService.currentUser()?.roles

    console.log("testando roles", roles)
    if (roles?.includes('ROLE_STUDENT')) {
      this.fetchReports(studentEndpoint);
    } else if (roles?.includes('ROLE_PROFESSOR') && roles.includes('ROLE_CCP')) {
      this.fetchReportsWithAggregation([professorEndpoint, ccpEndpoint]);
    } else if (roles?.includes('ROLE_PROFESSOR')) {
      this.fetchReports(professorEndpoint);
    } else if (roles?.includes('ROLE_CCP')) {
      this.fetchReports(ccpEndpoint);
    }
  }

  fetchReports(endpoint: string): void {
    const params = {
      page: this.currentPage.toString(),
      size: this.pageSize.toString(),
    };

    this.http.get<{ content: Report[]; totalElements: number }>("//localhost:8080" + endpoint, { params, withCredentials: true }).subscribe({
      next: (data) => {
        this.reports = data.content;
        this.totalReports = data.totalElements;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar relatórios', error);
        this.isLoading = false;
      },
    });
  }

  fetchReportsWithAggregation(endpoints: string[]): void {
    const params = {
      page: this.currentPage.toString(),
      size: Math.ceil(this.pageSize / endpoints.length).toString(),
    };

    const requests = endpoints.map((endpoint) =>
      this.http.get<{ content: Report[] }>("//localhost:8080" + endpoint, { params, withCredentials: true })
    );

    Promise.all(requests)
      .then((responses) => {
        this.reports = [];
        responses.forEach((response: any) => {
          this.reports.push(...response.content);
        });
        this.totalReports = this.reports.length;
        this.isLoading = false;
      })
      .catch((error) => {
        console.error('Erro ao carregar relatórios agregados', error);
        this.isLoading = false;
      });
  }

  onPageChange(event: PageEvent): void {
    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;
    this.loadReports();
  }
}
