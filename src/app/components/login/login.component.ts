import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  isLoading = false;
  showPassword = false;
  returnUrl = '/';
  

  // Role options for login
  roles = [
    { value: 'student', label: 'Student', description: 'Access your courses and learning materials' },
    { value: 'instructor', label: 'Instructor', description: 'Manage your courses and students' },
    { value: 'admin', label: 'Administrator', description: 'System administration and management' }
  ];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.loginForm = this.createForm();
  }

  ngOnInit() {
    // Get return url from route parameters or default to home
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  createForm(): FormGroup {
    return this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      role: ['student', [Validators.required]], // Default to student
      rememberMe: [false]
    });
  }

  onSubmit() {
  if (this.loginForm.valid) {
    this.isLoading = true;
    const { email, password, role } = this.loginForm.value;

    this.userService.login(email, password, role).subscribe({
      next: (user) => {
        this.isLoading = false;
        console.log('Login successful:', user);

        if (user) {
          // ✅ Save the full user object to localStorage
          localStorage.setItem('currentUser', JSON.stringify(user));
        }

        // Redirect based on role
        this.redirectBasedOnRole(user.role);
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Login failed:', error);
        alert('Login failed. Please check your credentials and role selection.');
      }
    });
  } else {
    this.markFormGroupTouched();
  }
}

  private redirectBasedOnRole(role: string): void {
    switch (role) {
      case 'student':
        this.router.navigate(['/student']);
        break;
      case 'instructor':
        this.router.navigate(['/instructor']);
        break;
      case 'admin':
        this.router.navigate(['/admin']);
        break;
      default:
        this.router.navigateByUrl(this.returnUrl);
    }
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  getValidationClass(controlName: string): string {
    const control = this.loginForm.get(controlName);
    if (control?.untouched) {
      return '';
    }
    return control?.valid ? 'is-valid' : 'is-invalid';
  }

  markFormGroupTouched() {
    Object.keys(this.loginForm.controls).forEach(key => {
      const control = this.loginForm.get(key);
      control?.markAsTouched();
    });
  }

    getRoleLabel(roleValue: string): string {
    const role = this.roles.find(r => r.value === roleValue);
    return role ? role.label : roleValue;
  }

  getRoleDescription(roleValue: string): string {
    const role = this.roles.find(r => r.value === roleValue);
    return role ? role.description : '';
  }

  demoLogin(role: 'student' | 'instructor') {
  const demoAccounts = {
    student: {
      email: 'sh@gmail.com',
      password: '123456',
      role: 'student'
    },
    instructor: {
      email: 'shshovonnct@gmail.com',
      password: '123456',
      role: 'instructor'
    }
  };

  const { email, password } = demoAccounts[role];

  // Auto-fill the form fields
  this.loginForm.setValue({
    email,
    password,
    role,
    rememberMe: false
  });

  // Submit using normal login flow
  this.onSubmit();
}

  
}