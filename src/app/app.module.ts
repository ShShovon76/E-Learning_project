import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { CoursesComponent } from './components/courses/courses.component';
import { CourseDetailsComponent } from './components/course-details/course-details.component';
import { LearningComponent } from './components/learning/learning.component';
import { InstructorDashboardComponent } from './components/instructor-dashboard/instructor-dashboard.component';
import { StudentDashboardComponent } from './components/student-dashboard/student-dashboard.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AboutComponent } from './components/about/about.component';
import { ContactComponent } from './components/contact/contact.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { CourseCreationComponent } from './components/course-creation/course-creation.component';
import { StudentProfileComponent } from './components/student-profile/student-profile.component';
import { InstructorProfileComponent } from './components/instructor-profile/instructor-profile.component';
import { FooterModule } from './components/footer/footer.module';
import { InstructorAnalyticsComponent } from './components/instructor-analytics/instructor-analytics.component';
import { EditCourseComponent } from './components/edit-course/edit-course.component';
import { AdminComponent } from './components/admin/admin.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    CoursesComponent,
    CourseDetailsComponent,
    LearningComponent,
    InstructorDashboardComponent,
    StudentDashboardComponent,
    LoginComponent,
    RegisterComponent,
    AboutComponent,
    ContactComponent,
    NavbarComponent,
    CourseCreationComponent,
    StudentProfileComponent,
    InstructorProfileComponent,
    InstructorAnalyticsComponent,
    EditCourseComponent,
    AdminComponent
   
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    FooterModule,
    RouterModule.forRoot([
      { path: '', redirectTo: '/home', pathMatch: 'full' },
      { path: 'home', component: HomeComponent },
      { path: 'courses', component: HomeComponent }, // Temporary
      { path: 'about', component: HomeComponent },   // Temporary
      { path: 'contact', component: HomeComponent }  
    ])
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
