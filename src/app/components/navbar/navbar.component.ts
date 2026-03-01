import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/models/user.model';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit, OnDestroy {
     currentUser: User | null = null;
  searchQuery: string = '';
  private userSubscription: Subscription;

  constructor(
    private userService: UserService,
    private router: Router
  ) {
    // Subscribe to user changes
    this.userSubscription = this.userService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
  }

  ngOnInit() {
    // Get current user from service
    this.currentUser = this.userService.getCurrentUser();
  }

  ngOnDestroy() {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }

  onSearch() {
    if (this.searchQuery.trim()) {
      // Navigate to courses page with search query
      this.router.navigate(['/courses'], { 
        queryParams: { search: this.searchQuery.trim() } 
      });
      this.searchQuery = ''; // Clear search after navigating
    }
  }

  logout() {
    this.userService.logout();
    this.currentUser = null;
    this.router.navigate(['/login']);
  }

  // Helper method to get user initials for avatar fallback
  getUserInitials(): string {
    if (!this.currentUser?.name) return '';
    return this.currentUser.name
      .split(' ')
      .map(part => part[0])
      .join('')
      .toUpperCase()
      .slice(0, 2);
  }

  // Helper method to safely check user role for template
  isStudent(): boolean {
    return this.currentUser?.role === 'student';
  }

  isInstructor(): boolean {
    return this.currentUser?.role === 'instructor';
  }

  isAdmin(): boolean {
    return this.currentUser?.role === 'admin';
  }
}
