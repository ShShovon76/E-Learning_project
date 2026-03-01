import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CourseService } from 'src/app/services/courses.service';
import { ProfileService } from 'src/app/services/profile.service';
import { UserService } from 'src/app/services/user.service';
import { Course } from 'src/models/course.model';
import { User } from 'src/models/user.model';

@Component({
  selector: 'app-instructor-profile',
  templateUrl: './instructor-profile.component.html',
  styleUrls: ['./instructor-profile.component.scss']
})
export class InstructorProfileComponent implements OnInit {
 currentUser: any = null;
  editProfileForm!: FormGroup;
  passwordForm!: FormGroup;

  createdCourses: any[] = [];
  isEditing = false;
  isLoading = false;
  activeTab: string = 'profile';
  showSuccessToast = false;
  toastMessage = '';

  private usersApi = 'http://localhost:3000/users';
  private coursesApi = 'http://localhost:3000/courses';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private userService: UserService,
    private courseService: CourseService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initForms();
    this.loadCurrentUser();
  }

  initForms() {
    this.editProfileForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      bio: [''],
      phone: [''],
      location: [''],
      website: ['', Validators.pattern('https?://.+') ],
      twitter: [''],
      linkedin: ['', Validators.pattern('https?://.+')],
      github: [''],
      expertise: [''],
      experience: ['']
    });

    this.passwordForm = this.fb.group({
      currentPassword: ['', []],
      newPassword: ['', [Validators.minLength(6)]],
      confirmPassword: ['', []]
    }, { validators: this.passwordMatchValidator });
  }

  // Use index-access for error key to avoid the TS template issue you've seen
  passwordMatchValidator(group: FormGroup) {
    const newPass = group.get('newPassword')?.value;
    const confirmPass = group.get('confirmPassword')?.value;
    return newPass === confirmPass ? null : { passwordMismatch: true };
  }

  loadCurrentUser() {
    // Prefer UserService observable/value; fallback to localStorage
    const svcUser = this.userService.getCurrentUser();
    const storedUser = localStorage.getItem('currentUser');

    if (svcUser) {
      this.currentUser = svcUser;
      this.afterUserLoaded();
    } else if (storedUser) {
      try {
        this.currentUser = JSON.parse(storedUser);
        // Refresh from server (ensure up-to-date)
        this.http.get<any>(`${this.usersApi}/${this.currentUser.id}`).subscribe({
          next: (u) => {
            this.currentUser = u;
            this.userService.setCurrentUser(u); // sync service & localStorage
            this.afterUserLoaded();
          },
          error: (err) => {
            console.warn('Could not refresh user from API, using stored data', err);
            this.afterUserLoaded();
          }
        });
      } catch {
        console.warn('Invalid localStorage currentUser');
      }
    } else {
      console.warn('No logged-in user found');
    }
  }

  afterUserLoaded() {
    // patch forms
    this.patchFormValues();
    // load courses created by this instructor
    if (this.currentUser?.id) {
      this.courseService.getCoursesByInstructor(Number(this.currentUser.id)).subscribe({
        next: (courses) => {
          // ensure numeric ids if json has strings
          this.createdCourses = courses.map(c => ({ ...c, id: Number(c.id) }));
        },
        error: (err) => {
          console.error('Failed to load instructor courses', err);
        }
      });
    }
  }

  patchFormValues() {
    if (!this.currentUser) return;
    this.editProfileForm.patchValue({
      name: this.currentUser.name || '',
      email: this.currentUser.email || '',
      bio: this.currentUser.bio || '',
      phone: this.currentUser.phone || '',
      location: this.currentUser.location || '',
      website: this.currentUser.website || '',
      twitter: this.currentUser.twitter || '',
      linkedin: this.currentUser.linkedin || '',
      github: this.currentUser.github || '',
      expertise: this.currentUser.expertise || '',
      experience: this.currentUser.experience || ''
    });
  }

  toggleEditMode() {
    this.isEditing = !this.isEditing;
    if (this.isEditing) this.patchFormValues();
  }

  cancelEdit() {
    this.isEditing = false;
    this.patchFormValues();
  }

  onEditSubmit() {
    if (this.editProfileForm.invalid || !this.currentUser) return;

    this.isLoading = true;
    const updated = { ...this.currentUser, ...this.editProfileForm.value };

    // Use UserService.updateUser (your service uses PUT)
    this.userService.updateUser(this.currentUser.id, updated).subscribe({
      next: (res) => {
        // sync local store and service
        this.currentUser = res;
        this.userService.setCurrentUser(res);
        localStorage.setItem('currentUser', JSON.stringify(res));
        this.isEditing = false;
        this.isLoading = false;
        this.showToast('Profile updated successfully!');
      },
      error: (err) => {
        console.error('Profile update failed', err);
        this.isLoading = false;
        this.showToast('Failed to update profile');
      }
    });
  }

  // Avatar upload: read file and PATCH avatar field
  updateAvatar(event: any) {
    const file = event.target.files?.[0];
    if (!file || !this.currentUser) return;

    const reader = new FileReader();
    reader.onload = () => {
      const avatarBase64 = reader.result as string;

      // Use userService.updateUser (PUT) or http.patch - we'll patch avatar only
      this.http.patch<any>(`${this.usersApi}/${this.currentUser.id}`, { avatar: avatarBase64 }).subscribe({
        next: (res) => {
          this.currentUser.avatar = res.avatar;
          this.userService.setCurrentUser(this.currentUser);
          localStorage.setItem('currentUser', JSON.stringify(this.currentUser));
          this.showToast('Avatar updated!');
        },
        error: (err) => {
          console.error('Avatar upload failed', err);
          this.showToast('Failed to update avatar');
        }
      });
    };
    reader.readAsDataURL(file);
  }

  // Password change
  onPasswordSubmit() {
    if (!this.passwordForm.valid || !this.currentUser) return;

    const currentPasswordInput = this.passwordForm.get('currentPassword')?.value;
    const newPassword = this.passwordForm.get('newPassword')?.value;

    // If stored user has password field, check it; otherwise allow (demo)
    const storedPassword = this.currentUser.password || '';

    if (storedPassword && currentPasswordInput !== storedPassword) {
      this.showToast('Current password is incorrect');
      return;
    }

    const updated = { ...this.currentUser, password: newPassword };

    this.userService.updateUser(this.currentUser.id, updated).subscribe({
      next: (res) => {
        this.currentUser = res;
        this.userService.setCurrentUser(res);
        localStorage.setItem('currentUser', JSON.stringify(res));
        this.passwordForm.reset();
        this.showToast('Password updated successfully');
      },
      error: (err) => {
        console.error('Password update failed', err);
        this.showToast('Failed to update password');
      }
    });
  }

  // Delete course
  deleteCourse(courseId: number) {
    if (!confirm('Are you sure you want to delete this course? This action cannot be undone.')) return;

    this.http.delete(`${this.coursesApi}/${courseId}`).subscribe({
      next: () => {
        this.createdCourses = this.createdCourses.filter(c => Number(c.id) !== Number(courseId));
        this.showToast('Course deleted');
      },
      error: (err) => {
        console.error('Delete course failed', err);
        this.showToast('Failed to delete course');
      }
    });
  }

  // Navigate to analytics page (assumes route is /instructor/analytics/:id)
  viewAnalytics(courseId: number) {
    this.router.navigate(['/instructor/analytics', courseId]);
  }

  changeTab(tab: string) {
    this.activeTab = tab;
    this.isEditing = false;
  }

  showToast(message: string) {
    this.toastMessage = message;
    this.showSuccessToast = true;
    setTimeout(() => (this.showSuccessToast = false), 2500);
  }

  goToCourse(courseId: number): void {
  this.router.navigate(['/course', courseId]);
}

}
