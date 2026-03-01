import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CourseService } from 'src/app/services/courses.service';
import { EnrollmentService } from 'src/app/services/enrollment.service';
import { LessonService } from 'src/app/services/lesson.service';
import { Course } from 'src/models/course.model';
import { Enrollment } from 'src/models/enrollment.model';
import { Lesson } from 'src/models/lesson.model';

@Component({
  selector: 'app-learning',
  templateUrl: './learning.component.html',
  styleUrls: ['./learning.component.scss']
})
export class LearningComponent implements OnInit {
  
  course!: Course;
  lessons: Lesson[] = [];
  currentLesson!: Lesson | null;
  enrollment!: Enrollment;

  isLoading = true;
  progress = 0;

  constructor(
    private route: ActivatedRoute,
    private courseService: CourseService,
    private lessonService: LessonService,
    private enrollmentService: EnrollmentService
  ) {}

  ngOnInit(): void {
    const courseId = Number(this.route.snapshot.paramMap.get('id'));
    const user = JSON.parse(localStorage.getItem('currentUser')!);

    this.loadCourse(courseId, user.id);
  }

  // -----------------------------------------------------
  // Load course, lessons, and enrollment
  // -----------------------------------------------------
  loadCourse(courseId: number, studentId: number) {
    this.courseService.getCourse(courseId).subscribe({
      next: course => {
        this.course = course;
        this.lessons = course.lessons ?? [];

        this.loadEnrollment(courseId, studentId);
      },
      error: err => console.error("Failed to load course", err)
    });
  }

  loadEnrollment(courseId: number, studentId: number) {
    this.enrollmentService.getStudentEnrollments(studentId).subscribe({
      next: enrollments => {
        this.enrollment = enrollments.find(e => Number(e.courseId) === courseId)!;
        this.progress = this.enrollment.progress;

        // Load first lesson if none completed
        const completed = this.enrollment.completedLessons || [];

        if (completed.length > 0) {
          this.currentLesson =
            this.lessons.find(l => l.order === completed.length) || this.lessons[0];
        } else {
          this.currentLesson = this.lessons[0];
        }

        this.isLoading = false;
      }
    });
  }

  // -----------------------------------------------------
  // Mark lesson completed
  // -----------------------------------------------------
  completeLesson() {
    if (!this.currentLesson) return;

    const lessonOrder = this.currentLesson.order;

    // Prevent duplicates
    if (!this.enrollment.completedLessons.includes(lessonOrder)) {
      this.enrollment.completedLessons.push(lessonOrder);
    }

    // Calculate progress
    this.progress = Math.floor(
      (this.enrollment.completedLessons.length / this.lessons.length) * 100
    );

    // Update enrollment
    this.enrollmentService.updateEnrollment(this.enrollment.id!, {
      completedLessons: this.enrollment.completedLessons,
      progress: this.progress
    }).subscribe();

    // Move to next lesson
    this.goToNextLesson();
  }

  // -----------------------------------------------------
  // Navigate to next lesson
  // -----------------------------------------------------
  goToNextLesson() {
    const nextOrder = (this.currentLesson?.order || 1) + 1;

    const nextLesson = this.lessons.find(l => l.order === nextOrder);

    if (nextLesson) {
      this.currentLesson = nextLesson;
    } else {
      this.currentLesson = null; // completed course
    }
  }

  // -----------------------------------------------------
  // Select lesson from sidebar
  // -----------------------------------------------------
  openLesson(lesson: Lesson) {
    this.currentLesson = lesson;
  }
}
