import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { forkJoin, map } from 'rxjs';
import { CourseService } from 'src/app/services/courses.service';
import { EnrollmentService } from 'src/app/services/enrollment.service';
import { UserService } from 'src/app/services/user.service';
import { Course, } from 'src/models/course.model';
import { Enrollment } from 'src/models/enrollment.model';
import { User } from 'src/models/user.model';

@Component({
  selector: 'app-student-dashboard',
  templateUrl: './student-dashboard.component.html',
  styleUrls: ['./student-dashboard.component.scss']
})
export class StudentDashboardComponent implements OnInit {
   currentUser: User | null = null;
  enrolledCourses: (Enrollment & { course: Course })[] = [];
  recommendedCourses: Course[] = [];
  isLoading = true;
  stats = {
    totalCourses: 0,
    inProgress: 0,
    completed: 0,
    totalLearningTime: 0
  };

  constructor(
    private userService: UserService,
    private courseService: CourseService,
    private enrollmentService: EnrollmentService,
    private router: Router
  ) {}

  ngOnInit() {
    this.currentUser = this.userService.getCurrentUser();
    this.loadDashboardData();
  }

    loadDashboardData() {
    if (!this.currentUser) return;

    this.enrollmentService.getStudentEnrollments(this.currentUser.id).subscribe({
      next: enrollments => {
        this.stats.totalCourses = enrollments.length;

        const requests = enrollments.map(enrollment =>
          this.courseService.getCourse(Number(enrollment.courseId)).pipe(
            map(course => ({ ...enrollment, course }))
          )
        );

        forkJoin(requests).subscribe({
          next: enrolledCourses => {
            this.enrolledCourses = enrolledCourses;
            this.calculateStats();
          },
          error: err => console.error(err)
        });
      }
    });

    this.courseService.getFeaturedCourses().subscribe({
      next: courses => {
        this.recommendedCourses = courses.slice(0, 4);
      }
    });
  }

  calculateStats() {
    this.stats.inProgress = this.enrolledCourses.filter(
      ec => ec.progress > 0 && ec.progress < 100
    ).length;
    
    this.stats.completed = this.enrolledCourses.filter(
      ec => ec.progress === 100
    ).length;

    this.stats.totalLearningTime = this.enrolledCourses.reduce(
      (total, ec) => total + ec.course.duration, 0
    );
  }

  continueLearning(courseId: number) {
  this.router.navigate(['/learning', courseId]);
}


  getProgressColor(progress: number): string {
    if (progress < 30) return 'danger';
    if (progress < 70) return 'warning';
    return 'success';
  }

  openCourse(id: number) {
  this.router.navigate(['/course', id]);
}
}
