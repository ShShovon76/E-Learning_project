import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CourseService } from 'src/app/services/courses.service';
import { UserService } from 'src/app/services/user.service';
import { Course } from 'src/models/course.model';
import { User } from 'src/models/user.model';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.scss']
})
export class CoursesComponent implements OnInit {
   currentUser: User | null = null;
   allCourses: Course[] = [];
  filteredCourses: Course[] = [];
  isLoading = true;

  // Filter properties
  searchTerm = '';
  selectedCategories: string[] = [];
  selectedLevels: string[] = [];
  selectedPriceRange = 'all';
  minRating = 0;
  sortBy = 'popular';

  // Filter options
  categories: string[] = ['Web Development', 'Programming', 'Data Science', 'Design', 'Business', 'Marketing'];
  levels: string[] = ['beginner', 'intermediate', 'advanced'];

  // Stats
  totalCourses = 0;
  totalInstructors = 0;

  constructor(
    private courseService: CourseService,
    private userService: UserService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.loadCourses();
     this.currentUser = this.userService.getCurrentUser();
    // Check for search query from navbar
    this.route.queryParams.subscribe(params => {
      if (params['search']) {
        this.searchTerm = params['search'];
        this.filterCourses();
      }
    });
  }

  loadCourses() {
    this.isLoading = true;
    this.courseService.getCourses().subscribe({
      next: (courses) => {
        this.allCourses = courses;
        this.filteredCourses = [...courses];
        this.calculateStats();
        this.filterCourses();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading courses:', error);
        this.isLoading = false;
      }
    });
  }

  calculateStats() {
  this.totalCourses = this.allCourses.length;
  
  // Count unique instructors from the actual instructor objects
  const instructorIds = new Set(this.allCourses.map(course => course.instructor.id));
  this.totalInstructors = instructorIds.size;
}

  filterCourses() {
    let filtered = [...this.allCourses];

    // Search filter
    if (this.searchTerm) {
      const searchLower = this.searchTerm.toLowerCase();
      filtered = filtered.filter(course =>
        course.title.toLowerCase().includes(searchLower) ||
        course.description.toLowerCase().includes(searchLower) ||
        course.instructor.name.toLowerCase().includes(searchLower) ||
        course.category.toLowerCase().includes(searchLower)
      );
    }

    // Category filter
    if (this.selectedCategories.length > 0) {
      filtered = filtered.filter(course =>
        this.selectedCategories.includes(course.category)
      );
    }

    // Level filter
    if (this.selectedLevels.length > 0) {
      filtered = filtered.filter(course =>
        this.selectedLevels.includes(course.level)
      );
    }

    // Price filter
    if (this.selectedPriceRange === 'free') {
      filtered = filtered.filter(course => course.price === 0);
    } else if (this.selectedPriceRange === 'paid') {
      filtered = filtered.filter(course => course.price > 0);
    }

    // Rating filter
    if (this.minRating > 0) {
      filtered = filtered.filter(course => course.rating >= this.minRating);
    }

    this.filteredCourses = filtered;
    this.sortCourses();
  }

  sortCourses() {
    switch (this.sortBy) {
      case 'rating':
        this.filteredCourses.sort((a, b) => b.rating - a.rating);
        break;
      case 'newest':
        this.filteredCourses.sort((a, b) => 
          new Date(b.createdDate).getTime() - new Date(a.createdDate).getTime()
        );
        break;
      case 'price-low':
        this.filteredCourses.sort((a, b) => a.price - b.price);
        break;
      case 'price-high':
        this.filteredCourses.sort((a, b) => b.price - a.price);
        break;
      case 'popular':
      default:
        this.filteredCourses.sort((a, b) => b.students - a.students);
        break;
    }
  }

  // Filter toggle methods
  toggleCategory(category: string) {
    const index = this.selectedCategories.indexOf(category);
    if (index > -1) {
      this.selectedCategories.splice(index, 1);
    } else {
      this.selectedCategories.push(category);
    }
    this.filterCourses();
  }

  toggleLevel(level: string) {
    const index = this.selectedLevels.indexOf(level);
    if (index > -1) {
      this.selectedLevels.splice(index, 1);
    } else {
      this.selectedLevels.push(level);
    }
    this.filterCourses();
  }

  // Helper methods for counts
  getCategoryCount(category: string): number {
    return this.allCourses.filter(course => course.category === category).length;
  }

  getLevelCount(level: string): number {
    return this.allCourses.filter(course => course.level === level).length;
  }

  clearFilters() {
    this.searchTerm = '';
    this.selectedCategories = [];
    this.selectedLevels = [];
    this.selectedPriceRange = 'all';
    this.minRating = 0;
    this.sortBy = 'popular';
    this.filterCourses();
  }
   isStudent(): boolean {
    return this.currentUser?.role === 'student';
  }

  isInstructor(): boolean {
    return this.currentUser?.role === 'instructor';
  }

}
