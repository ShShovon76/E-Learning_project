import { animate, state, style, transition, trigger } from '@angular/animations';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { forkJoin, map } from 'rxjs';
import { CourseService } from 'src/app/services/courses.service';
import { EnrollmentService } from 'src/app/services/enrollment.service';
import { UserService } from 'src/app/services/user.service';
import { Course } from 'src/models/course.model';
import { Enrollment } from 'src/models/enrollment.model';
import { User } from 'src/models/user.model';


@Component({
  selector: 'app-student-profile',
  templateUrl: './student-profile.component.html',
  styleUrls: ['./student-profile.component.scss'],
   animations: [
    trigger('fadeIn', [
      state('void', style({ opacity: 0, transform: 'translateY(-10px)' })),
      transition(':enter', [
        animate('400ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ]),
      transition(':leave', [
        animate('300ms ease-in', style({ opacity: 0, transform: 'translateY(-10px)' }))
      ])
    ])
  ]
})
export class StudentProfileComponent implements OnInit {
 currentUser: User | null = null;
  enrolledCourses: (Enrollment & { course: Course })[] = [];
  achievements: any[] = [];

  // ---------- UI STATE ----------
  editProfileForm!: FormGroup;
  passwordForm!: FormGroup;

  isEditing = false;
  isLoading = false;
  activeTab: string = 'profile';
  showSuccessToast = false;

  private apiUrl = 'http://localhost:3000/users';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private userService: UserService,
    private enrollmentService: EnrollmentService,
    private courseService: CourseService,
  ) {}

  ngOnInit(): void {
    this.currentUser = this.userService.getCurrentUser();
    this.initForms();

    // Load user info for form patching
    if (this.currentUser) {
      this.patchFormValues();
    }
  }

  // ==========================================================
  // ✅ FORM INITIALIZATION
  // ==========================================================
  initForms() {
    this.editProfileForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      bio: [''],
      phone: [''],
      location: [''],
      website: [''],
      twitter: [''],
      linkedin: [''],
    });

    this.passwordForm = this.fb.group(
      {
        currentPassword: ['', Validators.required],
        newPassword: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', Validators.required],
      },
      { validators: this.passwordMatchValidator }
    );
  }

  // ==========================================================
  // ✅ PATCH USER DATA INTO FORM
  // ==========================================================
  patchFormValues() {
    if (!this.currentUser) return;

    this.editProfileForm.patchValue({
      name: this.currentUser.name,
      email: this.currentUser.email,
      bio: this.currentUser.bio || '',
      phone: this.currentUser.phone || '',
      location: this.currentUser.location || '',
      website: this.currentUser.website || '',
      twitter: this.currentUser.twitter || '',
      linkedin: this.currentUser.linkedin || '',
    });
  }

  // ==========================================================
  // PASSWORD VALIDATION
  // ==========================================================
  passwordMatchValidator(group: FormGroup) {
    return group.get('newPassword')?.value === group.get('confirmPassword')?.value  
      ? null  
      : { passwordMismatch: true };
  }

  onPasswordSubmit() {
    if (this.passwordForm.invalid) return;
    // your future password update logic
  }

  // ==========================================================
  // EDIT MODE HANDLING
  // ==========================================================
  toggleEditMode() {
    this.isEditing = !this.isEditing;
  }

  cancelEdit() {
    this.isEditing = false;
    this.patchFormValues();
  }

  // ==========================================================
  // UPDATE PROFILE
  // ==========================================================
  onEditSubmit() {
    if (!this.currentUser || this.editProfileForm.invalid) return;

    this.isLoading = true;
    const updatedData = { ...this.currentUser, ...this.editProfileForm.value };

    this.http.patch(`${this.apiUrl}/${this.currentUser.id}`, updatedData).subscribe({
      next: res => {
        this.currentUser = res as User;
        localStorage.setItem('currentUser', JSON.stringify(this.currentUser));
        this.isEditing = false;
        this.isLoading = false;

        this.showSuccessToastMessage('Profile updated successfully!');
      },
      error: err => {
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  // ==========================================================
  // AVATAR UPLOAD
  // ==========================================================
  updateAvatar(event: any) {
    const file = event.target.files[0];
    if (!file || !this.currentUser) return;

    const reader = new FileReader();
    reader.onload = () => {
      const newAvatar = reader.result as string;

      this.http.patch(`${this.apiUrl}/${this.currentUser!.id}`, { avatar: newAvatar })
        .subscribe({
          next: res => {
            this.currentUser!.avatar = (res as any).avatar;
            localStorage.setItem('currentUser', JSON.stringify(this.currentUser));
            this.showSuccessToastMessage('Avatar updated!');
          },
          error: err => console.error(err)
        });
    };

    reader.readAsDataURL(file);
  }

  // ==========================================================
  // TAB SWITCH HANDLING
  // ==========================================================
  changeTab(tab: string) {
    this.activeTab = tab;

    if (tab === 'courses') this.loadEnrolledCourses();
    if (tab === 'achievements') this.loadAchievements();
  }

  // ==========================================================
  // LOAD ENROLLED COURSES (Dashboard Style)
  // ==========================================================
  loadEnrolledCourses() {
    if (!this.currentUser) return;

    this.enrollmentService.getStudentEnrollments(this.currentUser.id)
      .subscribe({
        next: enrollments => {
          const requests = enrollments.map(enrollment =>
            this.courseService.getCourse(Number(enrollment.courseId)).pipe(
              map(course => ({ ...enrollment, course }))
            )
          );

          forkJoin(requests).subscribe({
            next: results => {
              this.enrolledCourses = [...results];
            },
            error: err => console.error('Failed to load courses', err)
          });
        }
      });
  }

  // ==========================================================
  // LOAD ACHIEVEMENTS
  // ==========================================================
  loadAchievements() {
    if (!this.currentUser) return;

    this.http.get<any[]>(`http://localhost:3000/achievements?userId=${this.currentUser.id}`)
      .subscribe({
        next: data => this.achievements = data,
        error: err => console.error('Failed to load achievements', err)
      });
  }

  // ==========================================================
  // CALCULATIONS
  // ==========================================================
  getCompletedCourses() {
    return this.enrolledCourses.filter(c => c.progress === 100).length;
  }

  getTotalLearningTime() {
    return this.enrolledCourses.reduce((sum, ec) => sum + ec.course.duration, 0);
  }

  // ==========================================================
  // UI TOAST
  // ==========================================================
  showSuccessToastMessage(message: string) {
    this.showSuccessToast = true;
    setTimeout(() => (this.showSuccessToast = false), 2500);
  }

  continueLearning(courseId: number) {
  this.router.navigate(['/learning', courseId]);
}

}
