import { Component, OnInit } from '@angular/core';
import { EnrollmentService } from 'src/app/services/enrollment.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-student-footer',
  templateUrl: './student-footer.component.html',
  styleUrls: ['./student-footer.component.scss']
})
export class StudentFooterComponent implements OnInit {
   completedCourses = 0;

  constructor(
    private userService: UserService,
    private enrollmentService: EnrollmentService
  ) {}

  ngOnInit() {
    const currentUser = this.userService.getCurrentUser();
    if (currentUser) {
      this.enrollmentService.getStudentEnrollments(currentUser.id).subscribe({
        next: (enrollments) => {
          this.completedCourses = enrollments.filter(e => e.progress === 100).length;
        }
      });
    }
  }
}
