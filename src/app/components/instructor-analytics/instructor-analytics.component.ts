import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Chart, registerables  } from 'chart.js';
import { CourseService } from 'src/app/services/courses.service';
import { Course } from 'src/models/course.model';

Chart.register(...registerables);
@Component({
  selector: 'app-instructor-analytics',
  templateUrl: './instructor-analytics.component.html',
  styleUrls: ['./instructor-analytics.component.scss']
})
export class InstructorAnalyticsComponent implements OnInit {
   @ViewChild('performanceChart', { static: false }) chartRef!: ElementRef<HTMLCanvasElement>;

  isLoading = true;
  course: any;
  chart!: Chart;
  analyticsData = {
    totalEnrollments: 0,
    completionRate: 0,
    averageRating: 0,
    revenue: 0
  };
  apiUrl = 'http://localhost:3000/courses';

  constructor(private http: HttpClient,
     private route: ActivatedRoute,
    private courseService: CourseService

  ) {}

  ngOnInit() {
    const storedUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (!storedUser?.id) return;

    this.http.get<any[]>(`${this.apiUrl}?instructorId=${storedUser.id}`).subscribe({
      next: (courses) => {
        this.course = courses[0]; // example: first course
        this.initChart();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading analytics:', err);
        this.isLoading = false;
      }
    });
       const courseId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadCourseAnalytics(courseId);
  }

  initChart() {
    if (!this.chartRef) return;

    const ctx = this.chartRef.nativeElement.getContext('2d');
    if (!ctx) return;

    this.chart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
        datasets: [
          {
            label: 'Enrollments',
            data: [50, 70, 65, 90, 80, 120],
            borderColor: '#4e73df',
            backgroundColor: 'rgba(78, 115, 223, 0.1)',
            fill: true,
            tension: 0.4
          },
          {
            label: 'Revenue ($)',
            data: [1500, 2200, 1800, 2700, 2400, 3000],
            borderColor: '#1cc88a',
            backgroundColor: 'rgba(28, 200, 138, 0.1)',
            fill: true,
            tension: 0.4
          }
        ]
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'bottom'
          }
        }
      }
    });
  }
   loadCourseAnalytics(courseId: number) {
    this.courseService.getCourse(courseId).subscribe({
      next: (course) => {
        this.course = course;
        this.calculateAnalytics(course);
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading course analytics:', error);
        this.isLoading = false;
      }
    });
  }

  calculateAnalytics(course: Course) {
    this.analyticsData = {
      totalEnrollments: course.students,
      completionRate: Math.min(100, Math.floor((course.students * 0.7) / course.students * 100)), // Mock data
      averageRating: course.rating,
      revenue: course.price * course.students
    };
  }

  
 
}
