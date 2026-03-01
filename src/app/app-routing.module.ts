import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
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
import { AuthGuard } from './guards/auth.guard';
import { CourseCreationComponent } from './components/course-creation/course-creation.component';
import { StudentProfileComponent } from './components/student-profile/student-profile.component';
import { InstructorProfileComponent } from './components/instructor-profile/instructor-profile.component';
import { InstructorAnalyticsComponent } from './components/instructor-analytics/instructor-analytics.component';
import { EditCourseComponent } from './components/edit-course/edit-course.component';
import { AdminComponent } from './components/admin/admin.component';

const routes: Routes = [
   { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'courses', component: CoursesComponent },
  { path: 'course/:id', component: CourseDetailsComponent },
  
  // Protected routes - require authentication

  { 
    path: 'profile/student', 
    component: StudentProfileComponent, 
    canActivate: [AuthGuard] 
  },
  { 
    path: 'profile/instructor', 
    component: InstructorProfileComponent, 
    canActivate: [AuthGuard] 
  },
  { 
    path: 'learning/:id', 
    component: LearningComponent, 
    canActivate: [AuthGuard] 
  },
   {
    path: 'instructor/edit-course/:id',
    component: EditCourseComponent
  },
  { 
    path: 'admin', 
    component: AdminComponent, 
    canActivate: [AuthGuard] 
  },
  { 
    path: 'instructor', 
    component: InstructorDashboardComponent, 
    canActivate: [AuthGuard] 
  },
  { 
    path: 'student', 
    component: StudentDashboardComponent, 
    canActivate: [AuthGuard] 
  },
   { 
    path: 'instructor/create-course', 
    component: CourseCreationComponent, 
    canActivate: [AuthGuard] 
  },
   { path: 'instructor/analytics/:id', component: InstructorAnalyticsComponent },

  
  // Public routes - no authentication required
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'about', component: AboutComponent },
  { path: 'contact', component: ContactComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
