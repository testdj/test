import { NgModule } from '@angular/core';
import { ActiveTaskComponent } from './active-task/active-task.component';
import { Routes, RouterModule } from '@angular/router';
import { RegistrationComponent } from './registration/registration.component';
import { UserTasksComponent } from './user-tasks/user-tasks.component';
import { LoginComponent } from './login/login.component';
import { ShowPdfComponent } from './show-pdf/show-pdf.component';
import { HomeComponent } from './home/home.component';
import { AddJournalComponent } from './add-journal/add-journal.component';
import { AuthorInputComponent } from './author-input/author-input.component';
import { AuthGuard, AdminGuard } from './_guards';
import { AuthorGuard } from './_guards/author.guard';
import { ActivateJournalComponent } from './activate-journal/activate-journal.component';
import { AuthorSelectJournalComponent } from './author-select-journal/author-select-journal.component';
import { AdminJournalsComponent } from './admin-journals/admin-journals.component';
import { SuccessPageComponent } from './success-page/success-page.component';
import { AdminRegistrationsComponent } from './admin-registrations/admin-registrations.component';
import { UrednikRegisterComponent } from './urednik-register/urednik-register.component';
import { UrednikJournalsComponent } from './urednik-journals/urednik-journals.component';


const routes: Routes = [
	{ path: '', component: HomeComponent },
  { path: 'urednik/journals', component: UrednikJournalsComponent, canActivate: [AuthGuard] },
  { path: 'reg/success', component: SuccessPageComponent },
	{ path: 'journal/activate/:processInstanceID', component: ActivateJournalComponent, canActivate: [AuthGuard] },
  { path: 'admin/users', component: AdminRegistrationsComponent, canActivate: [AdminGuard] },
	{ path: 'journal/add', component: AddJournalComponent, canActivate: [AuthGuard] },
  { path: 'author/select/journal', component: AuthorSelectJournalComponent, canActivate: [AuthorGuard] },
	{ path: 'admin/journals', component: AdminJournalsComponent, canActivate: [AdminGuard] },
  { path: 'input/paper/:processInstanceID', component: AuthorInputComponent, canActivate: [AuthorGuard] },
  { path: 'admin/register/urednik', component: UrednikRegisterComponent, canActivate: [AdminGuard] },
	{ path: 'register', component: RegistrationComponent },
  { path: 'pdf', component: ShowPdfComponent, canActivate: [AuthorGuard] },
  { path: 'task/:taskID', component: ActiveTaskComponent },
  { path: 'tasks', component: UserTasksComponent },
	{ path: 'login', component: LoginComponent },
	{ path: '**', redirectTo: '' }
];

@NgModule({
  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)]
})
export class AppRoutingModule { }
