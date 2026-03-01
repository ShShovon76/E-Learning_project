import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup;
  isLoading = false;
  showPassword = false;

  // Role options for registration
  roles = [
    { value: 'student', label: 'Student', description: 'Join as a learner and access courses' },
    { value: 'instructor', label: 'Instructor', description: 'Create and manage your own courses' }
    // Note: Admin registration is typically not available publicly
  ];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ) {
    this.registerForm = this.createForm();
  }

  createForm(): FormGroup {
    return this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
      role: ['', [Validators.required]], // Default to student
      terms: [false, [Validators.requiredTrue]]
    }, {
      validators: this.passwordMatchValidator
    });
  }

  passwordMatchValidator(control: AbstractControl) {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      confirmPassword.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    }
    return null;
  }

  onSubmit() {
  if (this.registerForm.valid) {
    this.isLoading = true;
    const { firstName, lastName, email, password, role } = this.registerForm.value;

    const userData = {
      name: `${firstName} ${lastName}`,
      email: email,
      password: password,
      role: role,
      avatar: this.generateRandomAvatar(),
      bio: '', // Initialize empty bio
      phone: '', // Initialize empty phone
      location: '', // Initialize empty location
      website: '', // Initialize empty website
      twitter: '', // Initialize empty twitter
      linkedin: '', // Initialize empty linkedin
      expertise: '', // Initialize empty expertise
      experience: '', // Initialize empty experience
      hourlyRate: 0 // Initialize hourly rate
    };

    this.userService.register(userData).subscribe({
      next: (user) => {
        this.isLoading = false;
        console.log('Registration successful:', user);
        
        // Auto-login after registration
        this.userService.setCurrentUser(user);
        alert('Account created successfully!');
        
        // Redirect based on role
        this.redirectBasedOnRole(user.role);
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Registration failed:', error);
        alert('Registration failed. Please try again.');
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
      default:
        this.router.navigate(['/']);
    }
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  getValidationClass(controlName: string): string {
    const control = this.registerForm.get(controlName);
    if (control?.untouched) {
      return '';
    }
    return control?.valid ? 'is-valid' : 'is-invalid';
  }

  markFormGroupTouched() {
    Object.keys(this.registerForm.controls).forEach(key => {
      const control = this.registerForm.get(key);
      control?.markAsTouched();
    });
  }

  generateRandomAvatar(): string {
    const avatars = [
      'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&h=150&fit=crop&crop=face',
      'https://images.unsplash.com/photo-1527980965255-d3b416303d12?w=150&h=150&fit=crop&crop=face',
      'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&h=150&fit=crop&crop=face',
      'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&h=150&fit=crop&crop=face'
    ];
    return avatars[Math.floor(Math.random() * avatars.length)];
  }

  // Helper method to get role label
  getRoleLabel(roleValue: string): string {
  const role = this.roles.find(r => r.value === roleValue);
  return role ? role.label : roleValue;
}

getRoleDescription(roleValue: string): string {
  const role = this.roles.find(r => r.value === roleValue);
  return role ? role.description : '';
}
}