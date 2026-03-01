import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { User } from 'src/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:3000';
  private currentUser = new BehaviorSubject<User | null>(null);

  // Public observable for components to subscribe to
  public currentUser$ = this.currentUser.asObservable();

  constructor(private http: HttpClient) {
    // Load user from localStorage on service initialization
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
      this.currentUser.next(JSON.parse(savedUser));
    }
  }

  login(email: string, password: string, role: string): Observable<User> {
    return this.http.get<User[]>(`${this.apiUrl}/users?email=${email}`).pipe(
      map(users => {
        if (users.length === 0) {
          throw new Error('User not found');
        }

        const user = users[0];

        // Check if the user's role matches the selected role
        if (user.role !== role) {
          throw new Error(`Invalid role selection. Please login as ${user.role}`);
        }

        // In a real app, you should verify password here
        // For now, we'll assume any password works for demo
        this.setCurrentUser(user);
        return user;
      })
    );
  }

  register(userData: any): Observable<User> {
    const newUser: User = {
      id: this.generateId(),
      name: userData.name,
      email: userData.email,
      password: userData.password,
      role: userData.role,
      avatar: userData.avatar,
      joinDate: new Date().toISOString().split('T')[0],
      bio: userData.bio || '',
      phone: userData.phone || '',
      location: userData.location || '',
      website: userData.website || '',
      twitter: userData.twitter || '',
      linkedin: userData.linkedin || '',
      github: userData.github || '',
      expertise: userData.expertise || '',
      experience: userData.experience || '',
      hourlyRate: userData.hourlyRate || 0,
      isVerified: false
    };

    return this.http.post<User>(`${this.apiUrl}/users`, newUser);
  }

  setCurrentUser(user: User): void {
    this.currentUser.next(user);
    localStorage.setItem('currentUser', JSON.stringify(user));
  }

  getCurrentUser(): User | null {
    return this.currentUser.value;
  }

  logout(): void {
    this.currentUser.next(null);
    localStorage.removeItem('currentUser');
  }

  isLoggedIn(): boolean {
    return this.currentUser.value !== null;
  }

  private generateId(): number {
    return Math.floor(Math.random() * 1000);
  }

  getUserById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/users/${id}`);
  }

  updateUser(id: number, data: any): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/users/${id}`, data);
  }

  getEnrolledCoursesByStudentId(studentId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/enrollments?_expand=course&studentId=${studentId}`);
  }
}
