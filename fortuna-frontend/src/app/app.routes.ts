import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component'
import { SignUpComponent } from './signup/signup.component';
import { AuthGuard } from './auth/auth.guard';
import { HomeComponent } from './home/home.component';
import { SubmitReportComponent } from './report-submit/report-submit.component';
import { ReportsComponent } from './reports/reports.component';
import { ReportComponent } from './report/report.component';
import { SendFeedbackComponent } from './send-feedback/send-feedback.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignUpComponent },
  { path: 'report', component: SubmitReportComponent },
  { path: 'report/:id', component: ReportComponent },
  { path: 'reports', component: ReportsComponent },
  { path: 'feedback/:id', component: SendFeedbackComponent },
  // { path: '', component: HomeComponent },
  // { path: 'login', component: LoginComponent },
  // { path: 'signup', component: SignUpComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_UNKNOWN'] }},
  // { path: 'report', component: SubmitReportComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_STUDENT'] }},
  // { path: 'reports', component: ReportsComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_STUDENT'] }},
];
