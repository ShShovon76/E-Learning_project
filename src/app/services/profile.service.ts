import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
 private apiUrl = 'http://localhost:3000';

  constructor(private http: HttpClient) {}

  updateProfile(userId: number, userData: any): Observable<User> {
    return this.http.patch<User>(`${this.apiUrl}/users/${userId}`, userData);
  }

  changePassword(userId: number, passwordData: { currentPassword: string; newPassword: string }): Observable<any> {
    // In a real app, you'd have a proper password change endpoint
    return this.http.patch(`${this.apiUrl}/users/${userId}`, {
      password: passwordData.newPassword
    });
  }

  uploadAvatar(userId: number, avatarUrl: string): Observable<User> {
    return this.http.patch<User>(`${this.apiUrl}/users/${userId}`, {
      avatar: avatarUrl
    });
  }
  
    getUserById(userId: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/users/${userId}`);
  }
}
