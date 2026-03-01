import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseService } from 'src/app/services/courses.service';
import { UserService } from 'src/app/services/user.service';
import { Course } from 'src/models/course.model';
import { Lesson } from 'src/models/lesson.model';

@Component({
  selector: 'app-course-details',
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.scss']
})
export class CourseDetailsComponent implements OnInit {
    course: Course | null = null;
  lessons: Lesson[] = [];
  relatedCourses: Course[] = [];
  isLoading = true;
  instructorCoursesCount = 0;
  totalStudents = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private courseService: CourseService,
    private userService: UserService
  ) {}

 ngOnInit() {
  this.route.paramMap.subscribe(params => {
    const courseId = Number(params.get('id'));
    this.loadCourseDetails(courseId);
  });
}

  loadCourseDetails(courseId: number) {
  if (isNaN(courseId)) {
    this.router.navigate(['/courses']);
    return;
  }

  this.isLoading = true;

  // Load course details
  this.courseService.getCourse(courseId).subscribe({
    next: (course) => {
      this.course = course;
      this.loadCourseLessons(courseId);
      this.loadRelatedCourses(course);
      this.calculateInstructorStats(course.instructorId);
      window.scrollTo({ top: 0, behavior: 'smooth' }); 
    },
    error: (error) => {
      console.error('Error loading course:', error);
      this.isLoading = false;
      // Show user-friendly error message
      alert('Course not found. It may have been removed or there might be a connection issue.');
      this.router.navigate(['/courses']);
    }
  });
}

  loadCourseLessons(courseId: number) {
    this.courseService.getCourseLessons(courseId).subscribe({
      next: (lessons) => {
        this.lessons = lessons.sort((a, b) => a.order - b.order);
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading lessons:', error);
        this.lessons = [];
        this.isLoading = false;
      }
    });
  }

  loadRelatedCourses(currentCourse: Course) {
    this.courseService.getCoursesByCategory(currentCourse.category).subscribe({
      next: (courses) => {
        // Filter out the current course and limit to 3 related courses
        this.relatedCourses = courses
          .filter(course => course.id !== currentCourse.id)
          .slice(0, 3);
      },
      error: (error) => {
        console.error('Error loading related courses:', error);
        this.relatedCourses = [];
      }
    });
  }

  calculateInstructorStats(instructorId: number) {
    this.courseService.getCoursesByInstructor(instructorId).subscribe({
      next: (courses) => {
        this.instructorCoursesCount = courses.length;
        this.totalStudents = courses.reduce((total, course) => total + course.students, 0);
      },
      error: (error) => {
        console.error('Error loading instructor stats:', error);
        this.instructorCoursesCount = 0;
        this.totalStudents = 0;
      }
    });
  }

  enrollCourse() {
    const currentUser = this.userService.getCurrentUser();
    
    if (!currentUser) {
      // Redirect to login if user is not authenticated
      this.router.navigate(['/login'], { queryParams: { returnUrl: this.router.url } });
      return;
    }

    if (!this.course) return;

    this.courseService.enrollCourse(this.course.id, currentUser.id).subscribe({
      next: (enrollment) => {
        console.log('Successfully enrolled in course:', enrollment);
        // Redirect to learning page
        this.router.navigate(['/student']);
      },
      error: (error) => {
        console.error('Error enrolling in course:', error);
        alert('There was an error enrolling in the course. Please try again.');
      }
    });
  }

  getTotalCourseDuration(): number {
    return this.lessons.reduce((total, lesson) => total + lesson.duration, 0);
  }

  formatDuration(minutes: number): string {
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return hours > 0 ? `${hours}h ${mins}m` : `${mins}m`;
  }

  goToCourse(id: number) {
  this.router.navigate(['/course', id]);
}
}
