import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Course } from 'src/models/course.model';
import { Observable, map, switchMap, forkJoin, catchError, of, tap } from 'rxjs';
import { Enrollment } from 'src/models/enrollment.model';
import { User } from 'src/models/user.model';
import { Lesson } from 'src/models/lesson.model';

@Injectable({
  providedIn: 'root'
})
export class CourseService {
 private apiUrl = 'http://localhost:3000';
  private coursesCache = new Map<number, Course>(); // Cache for individual courses

  constructor(private http: HttpClient) {}

  // Helper method to convert ID to number
  private ensureNumberId(id: any): number {
    if (typeof id === 'string') {
      return parseInt(id, 10);
    }
    return id;
  }

  // Get all courses with instructor data
  getCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.apiUrl}/courses`).pipe(
      switchMap(courses => {
        const coursesWithInstructors = courses.map(course => {
          const instructorId = this.ensureNumberId(course.instructorId);
          
          return this.getUser(instructorId).pipe(
            map(instructor => ({
              ...course,
              instructor: instructor,
              lessons: course.lessons || []
            })),
            catchError(error => {
              console.error(`Error loading instructor for course ${course.id}:`, error);
              return of({
                ...course,
                instructor: this.getFallbackInstructor(),
                lessons: course.lessons || []
              });
            })
          );
        });
        return forkJoin(coursesWithInstructors);
      })
    );
  }

  // Get single course by ID with full details - ENHANCED with fallback
  getCourse(id: number): Observable<Course> {
    // Check cache first
    const cachedCourse = this.coursesCache.get(id);
    if (cachedCourse) {
      console.log('Returning course from cache:', id);
      return of(cachedCourse);
    }

    return this.http.get<Course>(`${this.apiUrl}/courses/${id}`).pipe(
      switchMap(course => {
        const instructorId = this.ensureNumberId(course.instructorId);
        
        return this.getUser(instructorId).pipe(
          map(instructor => ({
            ...course,
            instructor: instructor,
            lessons: course.lessons || []
          })),
          catchError(error => {
            console.error(`Error loading instructor for course ${id}:`, error);
            return of({
              ...course,
              instructor: this.getFallbackInstructor(),
              lessons: course.lessons || []
            });
          })
        );
      }),
      tap(course => {
        // Cache the course for future requests
        this.coursesCache.set(id, course);
        console.log('Course cached:', id);
      }),
      catchError(error => {
        console.error(`Error loading course ${id} from API:`, error);
        
        // Fallback: Try to find course in local storage
        const fallbackCourse = this.findCourseInLocalStorage(id);
        if (fallbackCourse) {
          console.log('Using fallback course from local storage:', id);
          return of(fallbackCourse);
        }
        
        // If no fallback, re-throw the error
        throw error;
      })
    );
  }

  // Get featured courses
  getFeaturedCourses(): Observable<Course[]> {
    return this.getCourses().pipe(
      map(courses => courses.filter(course => course.isFeatured))
    );
  }

  // Get course lessons
  getCourseLessons(courseId: number): Observable<Lesson[]> {
    return this.http.get<Lesson[]>(`${this.apiUrl}/lessons?courseId=${courseId}`);
  }

  // Get user by ID
  getUser(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/users/${id}`);
  }

  // Enrollment methods
 enrollCourse(courseId: number | string, studentId: number | string): Observable<Enrollment> {
  const enrollment: Enrollment = {
    studentId: Number(studentId),
    courseId: Number(courseId),
    enrolledDate: new Date().toISOString().split('T')[0],
    progress: 0,
    completedLessons: []
  };

  return this.http.post<Enrollment>(`${this.apiUrl}/enrollments`, enrollment);
}

  getStudentEnrollments(studentId: number): Observable<Enrollment[]> {
  return this.http.get<Enrollment[]>(`${this.apiUrl}/enrollments`).pipe(
    map(enrollments =>
      enrollments.filter(enrollment =>
        Number(enrollment.studentId) === studentId
      )
    )
  );
}

  // Search courses
  searchCourses(query: string): Observable<Course[]> {
    return this.getCourses().pipe(
      map(courses => courses.filter(course =>
        course.title.toLowerCase().includes(query.toLowerCase()) ||
        course.description.toLowerCase().includes(query.toLowerCase()) ||
        course.category.toLowerCase().includes(query.toLowerCase())
      ))
    );
  }

  // Get courses by category
  getCoursesByCategory(category: string): Observable<Course[]> {
    return this.getCourses().pipe(
      map(courses => courses.filter(course => course.category === category))
    );
  }

  // Get courses by instructor
  getCoursesByInstructor(instructorId: number): Observable<Course[]> {
    const numericInstructorId = this.ensureNumberId(instructorId);
    
    return this.http.get<Course[]>(`${this.apiUrl}/courses?instructorId=${numericInstructorId}`).pipe(
      switchMap(courses => {
        if (courses.length === 0) {
          return of([]);
        }

        const coursesWithInstructors = courses.map(course => {
          const courseInstructorId = this.ensureNumberId(course.instructorId);
          
          return this.getUser(courseInstructorId).pipe(
            map(instructor => ({
              ...course,
              instructor: instructor,
              lessons: course.lessons || []
            })),
            catchError(error => {
              console.error(`Error loading instructor for course ${course.id}:`, error);
              return of({
                ...course,
                instructor: this.getFallbackInstructor(),
                lessons: course.lessons || []
              });
            })
          );
        });
        
        return forkJoin(coursesWithInstructors);
      }),
      tap(courses => {
        // Cache each course individually
        courses.forEach(course => {
          this.coursesCache.set(course.id, course);
        });
      }),
      catchError(error => {
        console.error('Error loading instructor courses:', error);
        return of([]);
      })
    );
  }

  // Create course - ENHANCED with proper caching and persistence
  createCourse(courseData: any): Observable<Course> {
    const instructorId = this.ensureNumberId(courseData.instructorId);
    const newCourse = {
      ...courseData,
      id: this.generateId(),
      instructorId: instructorId,
      students: courseData.students || 0,
      rating: courseData.rating || 0,
      createdDate: courseData.createdDate || new Date().toISOString().split('T')[0],
      lessons: courseData.lessons || [],
      requirements: courseData.requirements || [],
      learningObjectives: courseData.learningObjectives || []
    };
    
    console.log('Creating course:', newCourse);
    
    return this.http.post<Course>(`${this.apiUrl}/courses`, newCourse).pipe(
      switchMap(createdCourse => {
        console.log('Course created successfully, loading instructor data...');
        
        // Load instructor data for the created course
        return this.getUser(instructorId).pipe(
          map(instructor => ({
            ...createdCourse,
            instructor: instructor,
            lessons: createdCourse.lessons || []
          })),
          catchError(error => {
            console.error('Error loading instructor for created course:', error);
            return of({
              ...createdCourse,
              instructor: this.getFallbackInstructor(),
              lessons: createdCourse.lessons || []
            });
          })
        );
      }),
      tap(fullCourse => {
        // Cache the complete course
        this.coursesCache.set(fullCourse.id, fullCourse);
        console.log('Course cached after creation:', fullCourse.id);
        
        // Also save to local storage as backup
        this.saveCourseToLocalStorage(fullCourse);
      })
    );
  }

  // PRIVATE METHODS FOR PERSISTENCE

  /**
   * Find course in local storage fallback
   */
  private findCourseInLocalStorage(courseId: number): Course | null {
    try {
      // Try to find course in any instructor's local storage
      for (let i = 0; i < localStorage.length; i++) {
        const key = localStorage.key(i);
        if (key && key.startsWith('instructor_') && key.endsWith('_courses')) {
          const coursesJson = localStorage.getItem(key);
          if (coursesJson) {
            const courses: Course[] = JSON.parse(coursesJson);
            const foundCourse = courses.find(c => c.id === courseId);
            if (foundCourse) {
              console.log('Found course in local storage:', courseId);
              return foundCourse;
            }
          }
        }
      }
    } catch (error) {
      console.error('Error searching for course in local storage:', error);
    }
    return null;
  }

  /**
   * Save course to local storage for persistence
   */
  private saveCourseToLocalStorage(course: Course): void {
    try {
      const key = `instructor_${course.instructorId}_courses`;
      const existingCoursesJson = localStorage.getItem(key);
      let existingCourses: Course[] = [];
      
      if (existingCoursesJson) {
        existingCourses = JSON.parse(existingCoursesJson);
      }
      
      // Remove if exists and add updated version
      existingCourses = existingCourses.filter(c => c.id !== course.id);
      existingCourses.push(course);
      
      localStorage.setItem(key, JSON.stringify(existingCourses));
      console.log('Course saved to local storage for persistence:', course.title);
    } catch (error) {
      console.error('Error saving course to local storage:', error);
    }
  }

  private generateId(): number {
    return Math.floor(Math.random() * 10000);
  }

  private getFallbackInstructor(): User {
    return {
      id: 0,
      name: 'Unknown Instructor',
      email: 'unknown@example.com',
      role: 'instructor',
      avatar: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face',
      joinDate: new Date().toISOString().split('T')[0],
      bio: 'Instructor information not available'
    };
  }

  // Update course
  updateCourse(courseId: number, courseData: any): Observable<Course> {
    return this.http.patch<Course>(`${this.apiUrl}/courses/${courseId}`, courseData).pipe(
      tap(updatedCourse => {
        // Update cache
        this.coursesCache.set(courseId, updatedCourse);
        // Update local storage
        this.saveCourseToLocalStorage(updatedCourse);
      })
    );
  }

  deleteCourse(courseId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/courses/${courseId}`).pipe(
      tap(() => {
        // Remove from cache
        this.coursesCache.delete(courseId);
        console.log('Course removed from cache:', courseId);
      })
    );
  }
}
