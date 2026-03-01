import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseService } from 'src/app/services/courses.service';
import { Course } from 'src/models/course.model';

@Component({
  selector: 'app-edit-course',
  templateUrl: './edit-course.component.html',
  styleUrls: ['./edit-course.component.scss']
})
export class EditCourseComponent implements OnInit {
    course: Course | null = null;
  isLoading = true;
  isUpdating = false;

  // Form model
  courseData = {
    title: '',
    description: '',
    category: '',
    level: 'beginner',
    price: 0,
    duration: 0,
    thumbnail: '',
    isFeatured: false,
    requirements: [''],
    learningObjectives: ['']
  };

  categories = ['Web Development', 'Programming', 'Data Science', 'Design', 'Business', 'Marketing'];
  levels = ['beginner', 'intermediate', 'advanced'];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private courseService: CourseService
  ) {}

  ngOnInit() {
    const courseId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadCourse(courseId);
  }

  loadCourse(courseId: number) {
    this.courseService.getCourse(courseId).subscribe({
      next: (course) => {
        this.course = course;
        // Initialize form with course data
        this.courseData = {
          title: course.title,
          description: course.description,
          category: course.category,
          level: course.level,
          price: course.price,
          duration: course.duration,
          thumbnail: course.thumbnail,
          isFeatured: course.isFeatured || false,
          requirements: course.requirements || [''],
          learningObjectives: course.learningObjectives || ['']
        };
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading course:', error);
        this.isLoading = false;
        this.router.navigate(['/instructor']);
      }
    });
  }

  updateCourse() {
    if (!this.course) return;

    this.isUpdating = true;

    // Filter out empty requirements and objectives
    const filteredData = {
      ...this.courseData,
      requirements: this.courseData.requirements.filter(req => req.trim() !== ''),
      learningObjectives: this.courseData.learningObjectives.filter(obj => obj.trim() !== '')
    };

    this.courseService.updateCourse(this.course.id, filteredData).subscribe({
      next: (updatedCourse) => {
        this.course = updatedCourse;
        this.isUpdating = false;
        alert('Course updated successfully!');
        this.router.navigate(['/instructor']);
      },
      error: (error) => {
        console.error('Error updating course:', error);
        this.isUpdating = false;
        alert('Error updating course. Please try again.');
      }
    });
  }

  addRequirement() {
    this.courseData.requirements.push('');
  }

  removeRequirement(index: number) {
    this.courseData.requirements.splice(index, 1);
  }

  addLearningObjective() {
    this.courseData.learningObjectives.push('');
  }

  removeLearningObjective(index: number) {
    this.courseData.learningObjectives.splice(index, 1);
  }

  cancelEdit() {
    this.router.navigate(['/instructor']);
  }
}
