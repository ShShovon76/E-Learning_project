import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CommonFooterComponent } from './common-footer/common-footer.component';
import { StudentFooterComponent } from './student-footer/student-footer.component';
import { InstructorFooterComponent } from './instructor-footer/instructor-footer.component';
import { AuthFooterComponent } from './auth-footer/auth-footer.component';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [
     CommonFooterComponent,
    StudentFooterComponent,
    InstructorFooterComponent,
    AuthFooterComponent
  ],
  imports: [
    CommonModule,
    RouterModule
  ],
   exports: [
    CommonFooterComponent,
    StudentFooterComponent,
    InstructorFooterComponent,
    AuthFooterComponent
  ]
})
export class FooterModule { }
