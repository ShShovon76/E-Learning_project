import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CourseService } from 'src/app/services/courses.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-course-creation',
  templateUrl: './course-creation.component.html',
  styleUrls: ['./course-creation.component.scss']
})
export class CourseCreationComponent implements OnInit{
   courseForm: FormGroup;
  isLoading = false;
  currentStep = 1;
  totalSteps = 3;
  currentUser: any;
  previewMode = false;

  // Form options
  categories = [
    'Web Development',
    'Data Science',
    'Programming',
    'Design',
    'Business',
    'Marketing',
    'Mobile Development',
    'DevOps'
  ];

  levels = [
    { value: 'beginner', label: 'Beginner', description: 'No prior knowledge required' },
    { value: 'intermediate', label: 'Intermediate', description: 'Some experience needed' },
    { value: 'advanced', label: 'Advanced', description: 'For experienced professionals' }
  ];

  lessonTypes = [
    { value: 'video', label: 'Video Lesson', icon: 'bi-play-circle' },
    { value: 'article', label: 'Article', icon: 'bi-file-text' },
    { value: 'quiz', label: 'Quiz', icon: 'bi-question-circle' },
    { value: 'assignment', label: 'Assignment', icon: 'bi-pencil' }
  ];

  constructor(
    private fb: FormBuilder,
    private courseService: CourseService,
    private userService: UserService,
    private router: Router
  ) {
    this.courseForm = this.createForm();
  }

  ngOnInit() {
    this.currentUser = this.userService.getCurrentUser();
  }

  createForm(): FormGroup {
    return this.fb.group({
      // Basic Information
      title: ['', [Validators.required, Validators.minLength(5)]],
      description: ['', [Validators.required, Validators.minLength(50)]],
      category: ['', Validators.required],
      level: ['beginner', Validators.required],
      price: [0, [Validators.required, Validators.min(0)]],
      duration: [0, [Validators.required, Validators.min(1)]],
      thumbnail: ['', Validators.required],
      
      // Course Content
      lessons: this.fb.array([]),
      
      // Settings
      isFeatured: [false],
      requirements: this.fb.array([this.fb.control('')]),
      learningObjectives: this.fb.array([this.fb.control('')])
    });
  }

  // ADD THIS METHOD TO FIX THE ERROR
  isStepValid(step: number): boolean {
  switch (step) {
    case 1:
      const fields = ['title', 'description', 'category', 'level', 'price', 'duration', 'thumbnail'];
      return fields.every(fieldName => {
        const control = this.courseForm.get(fieldName);
        return control ? control.valid : false;
      });
    
    case 2:
      if (this.lessons.length === 0) {
        return false;
      }
      
      return this.lessons.controls.every(lesson => {
        const lessonFields = ['title', 'description', 'duration'];
        return lessonFields.every(fieldName => {
          const control = lesson.get(fieldName);
          return control ? control.valid : false;
        });
      });
    
    case 3:
      return true;
    
    default:
      return false;
  }
}

  get lessons(): FormArray {
    return this.courseForm.get('lessons') as FormArray;
  }

  get requirements(): FormArray {
    return this.courseForm.get('requirements') as FormArray;
  }

  get learningObjectives(): FormArray {
    return this.courseForm.get('learningObjectives') as FormArray;
  }

  addLesson(): void {
    this.lessons.push(this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      type: ['video', Validators.required],
      duration: [0, [Validators.required, Validators.min(1)]],
      videoUrl: [''],
      content: [''],
      order: [this.lessons.length + 1],
      isFree: [false]
    }));
  }

  removeLesson(index: number): void {
    this.lessons.removeAt(index);
    this.updateLessonOrder();
  }

  updateLessonOrder(): void {
    this.lessons.controls.forEach((lesson, index) => {
      lesson.patchValue({ order: index + 1 });
    });
  }

  moveLessonUp(index: number): void {
    if (index > 0) {
      const lesson = this.lessons.at(index);
      this.lessons.removeAt(index);
      this.lessons.insert(index - 1, lesson);
      this.updateLessonOrder();
    }
  }

  moveLessonDown(index: number): void {
    if (index < this.lessons.length - 1) {
      const lesson = this.lessons.at(index);
      this.lessons.removeAt(index);
      this.lessons.insert(index + 1, lesson);
      this.updateLessonOrder();
    }
  }

  addRequirement(): void {
    this.requirements.push(this.fb.control(''));
  }

  removeRequirement(index: number): void {
    this.requirements.removeAt(index);
  }

  addLearningObjective(): void {
    this.learningObjectives.push(this.fb.control(''));
  }

  removeLearningObjective(index: number): void {
    this.learningObjectives.removeAt(index);
  }

  nextStep(): void {
    if (this.currentStep < this.totalSteps && this.isStepValid(this.currentStep)) {
      this.currentStep++;
    }
  }

  prevStep(): void {
    if (this.currentStep > 1) {
      this.currentStep--;
    }
  }

  goToStep(step: number): void {
    if (step >= 1 && step <= this.totalSteps) {
      this.currentStep = step;
    }
  }

  togglePreview(): void {
    this.previewMode = !this.previewMode;
  }

  onSubmit(): void {
  if (this.courseForm.valid && this.currentUser) {
    this.isLoading = true;
    
    // Ensure instructorId is a number
    const instructorId = typeof this.currentUser.id === 'string' 
      ? parseInt(this.currentUser.id, 10) 
      : this.currentUser.id;

    const courseData = {
      ...this.courseForm.value,
      instructorId: instructorId, // This will now be a number
      students: 0,
      rating: 0,
      createdDate: new Date().toISOString().split('T')[0],
      requirements: this.requirements.value.filter((req: string) => req.trim() !== ''),
      learningObjectives: this.learningObjectives.value.filter((obj: string) => obj.trim() !== '')
    };

    this.courseService.createCourse(courseData).subscribe({
      next: (course) => {
        this.isLoading = false;
        alert('Course created successfully!');
        this.router.navigate(['/instructor']);
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Error creating course:', error);
        alert('Error creating course. Please try again.');
      }
    });
  } else {
    this.markFormGroupTouched();
  }
}

  markFormGroupTouched(): void {
    Object.keys(this.courseForm.controls).forEach(key => {
      const control = this.courseForm.get(key);
      control?.markAsTouched();
    });
  }

  getStepProgress(): number {
    return (this.currentStep / this.totalSteps) * 100;
  }

  // Thumbnail placeholder based on category
  getThumbnailPlaceholder(): string {
    const category = this.courseForm.get('category')?.value;
    const placeholders: { [key: string]: string } = {
      'Web Development': 'https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=400&h=250&fit=crop',
      'Data Science': 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=400&h=250&fit=crop',
      'Programming': 'https://images.unsplash.com/photo-1627398242454-45a1465c2479?w=400&h=250&fit=crop',
      'Design': 'https://images.unsplash.com/photo-1561070791-2526d30994b5?w=400&h=250&fit=crop',
      'Business': 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=400&h=250&fit=crop'
    };
    return placeholders[category] || 'https://images.unsplash.com/photo-1496171367470-9ed9a91ea931?w=400&h=250&fit=crop';
  }
}
