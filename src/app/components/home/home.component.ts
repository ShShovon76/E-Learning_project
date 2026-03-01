import { Component, OnInit } from '@angular/core';
import { CourseService } from 'src/app/services/courses.service';
import { Course } from 'src/models/course.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit{
   featuredCourses: Course[] = [];
  isLoading = true;

  constructor(private courseService: CourseService) {}

  ngOnInit() {
    this.loadFeaturedCourses();
  }

  loadFeaturedCourses() {
    this.courseService.getFeaturedCourses().subscribe({
      next: (courses) => {
        this.featuredCourses = courses;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading featured courses:', error);
        this.isLoading = false;
      }
    });
  }
}
