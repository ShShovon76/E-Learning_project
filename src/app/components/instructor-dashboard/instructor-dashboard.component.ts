import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { CourseService } from 'src/app/services/courses.service';
import { UserService } from 'src/app/services/user.service';
import { Course } from 'src/models/course.model';
import { User } from 'src/models/user.model';

@Component({
  selector: 'app-instructor-dashboard',
  templateUrl: './instructor-dashboard.component.html',
  styleUrls: ['./instructor-dashboard.component.scss']
})
export class InstructorDashboardComponent implements OnInit {
  currentUser: User | null = null;
  myCourses: Course[] = [];
  isLoading = true;
  stats = {
    totalCourses: 0,
    totalStudents: 0,
    totalRevenue: 0,
    averageRating: 0
  };

  recentEnrollments = [
    { studentName: 'John Doe', course: 'Angular Masterclass', date: '2024-01-15', amount: 49.99 },
    { studentName: 'Jane Smith', course: 'JavaScript Fundamentals', date: '2024-01-14', amount: 29.99 },
    { studentName: 'Mike Johnson', course: 'Angular Masterclass', date: '2024-01-13', amount: 49.99 }
  ];

  constructor(
    private userService: UserService,
    private courseService: CourseService,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit() {
    this.currentUser = this.userService.getCurrentUser();
    this.loadDashboardData();
    
    // ADD THIS: Reload data when navigating back to dashboard
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        if (event.url === '/instructor/dashboard') {
          this.loadDashboardData();
        }
      });
  }

  loadDashboardData() {
    if (!this.currentUser) {
      console.log('No current user found');
      this.isLoading = false;
      return;
    }

    console.log('Loading courses for instructor:', this.currentUser.id);

    this.courseService.getCoursesByInstructor(this.currentUser.id).subscribe({
      next: (courses) => {
        console.log('Courses from API:', courses);
        
        // ADD THIS: Handle case where API returns empty array but we have local data
        if (courses.length === 0) {
          // Try to get courses from local storage as fallback
          const localCourses = this.getCoursesFromLocalStorage();
          if (localCourses.length > 0) {
            console.log('Using courses from local storage fallback:', localCourses);
            this.myCourses = localCourses;
          } else {
            this.myCourses = [];
          }
        } else {
          this.myCourses = courses;
          // ALSO ADD THIS: Save to local storage for future fallback
          this.saveCoursesToLocalStorage(courses);
        }
        
        this.calculateStats();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading courses from API:', error);
        
        // ADD THIS: Fallback to local storage on error
        const localCourses = this.getCoursesFromLocalStorage();
        console.log('Using local storage fallback due to error:', localCourses);
        this.myCourses = localCourses;
        this.calculateStats();
        this.isLoading = false;
      }
    });
  }

  // ADD THESE TWO NEW METHODS for local storage fallback:

  /**
   * Get courses from local storage as fallback when API fails
   */
  private getCoursesFromLocalStorage(): Course[] {
    if (!this.currentUser) return [];
    
    const coursesJson = localStorage.getItem(`instructor_${this.currentUser.id}_courses`);
    if (coursesJson) {
      try {
        return JSON.parse(coursesJson);
      } catch (error) {
        console.error('Error parsing courses from local storage:', error);
        return [];
      }
    }
    return [];
  }

  /**
   * Save courses to local storage for fallback scenarios
   */
  private saveCoursesToLocalStorage(courses: Course[]): void {
    if (!this.currentUser) return;
    
    try {
      localStorage.setItem(`instructor_${this.currentUser.id}_courses`, JSON.stringify(courses));
      console.log('Courses saved to local storage for instructor:', this.currentUser.id);
    } catch (error) {
      console.error('Error saving courses to local storage:', error);
    }
  }

  calculateStats() {
    this.stats.totalCourses = this.myCourses.length;
    this.stats.totalStudents = this.myCourses.reduce((sum, course) => sum + course.students, 0);
    this.stats.totalRevenue = this.myCourses.reduce((sum, course) => sum + (course.price * course.students), 0);
    this.stats.averageRating = this.myCourses.length > 0 
      ? this.myCourses.reduce((sum, course) => sum + course.rating, 0) / this.myCourses.length
      : 0;
  }

  createNewCourse() {
    // Navigate to course creation page
    console.log('Create new course');
  }

  editCourse(courseId: number) {
  this.router.navigate(['/instructor/edit-course', courseId]);
}

previewCourse(courseId: number) {
  // Navigate to the public course view
  this.router.navigate(['/course', courseId]);
}

  viewAnalytics(courseId: number) {
    this.router.navigate(['/instructor/analytics', courseId]);
  }
  navigateToAnalytics() {
  if (this.myCourses.length > 0) {
    // Navigate to analytics of the first course, or you can create a general analytics page
    this.router.navigate(['/instructor/analytics', this.myCourses[0].id]);
  } else {
    alert('Please create a course first to view analytics.');
  }
}

deleteCourse(courseId: number) {
  if (!confirm('Are you sure you want to delete this course?')) {
    return;
  }

  this.http.delete(`http://localhost:3000/courses/${courseId}`).subscribe({
    next: () => {
      // Remove from local list
      this.myCourses = this.myCourses.filter(course => course.id !== courseId);
      alert('✅ Course deleted successfully!');
    },
    error: (err) => {
      console.error('Delete failed:', err);
      alert('❌ Failed to delete the course.');
    }
  });
}

}
