import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { RegistrationComponent } from './registration/registration.component';
import { SuccessPageComponent } from './success-page/success-page.component';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NotifierModule } from "angular-notifier";
import { HomeComponent } from './home/home.component';
import { NavbarComponent } from './_navbar/navbar.component';
import { ActiveTaskComponent } from './active-task/active-task.component';
import { AdminRegistrationsComponent } from './admin-registrations/admin-registrations.component';
import { JwtInterceptor, ErrorInterceptor } from './_helpers';
import { AddJournalComponent } from './add-journal/add-journal.component';
import { AuthorSelectJournalComponent } from './author-select-journal/author-select-journal.component';
import { ActivateJournalComponent } from './activate-journal/activate-journal.component';
import { FileUploadModule } from 'ng2-file-upload';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { ShowPdfComponent } from './show-pdf/show-pdf.component';
import { AdminJournalsComponent } from './admin-journals/admin-journals.component';
import { UrednikJournalsComponent } from './urednik-journals/urednik-journals.component';
import { UserTasksComponent } from './user-tasks/user-tasks.component';
import { UrednikRegisterComponent } from './urednik-register/urednik-register.component';
import { AuthorInputComponent } from './author-input/author-input.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';






@NgModule({
  declarations: [
    AppComponent,
    AdminJournalsComponent,
    RegistrationComponent,
    ShowPdfComponent,
    NavbarComponent,
    ActiveTaskComponent,
    LoginComponent,
    UrednikRegisterComponent,
    HomeComponent,
    AddJournalComponent,
    AuthorSelectJournalComponent,
    ActivateJournalComponent,
    AdminRegistrationsComponent,
    UrednikJournalsComponent,
    SuccessPageComponent,
    AuthorInputComponent,
    UserTasksComponent
  ],
  imports: [
    NgMultiSelectDropDownModule.forRoot(),
    BrowserModule,
    NotifierModule,
    NgbModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FileUploadModule,
    FormsModule,
    HttpClientModule,
    NgxExtendedPdfViewerModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
