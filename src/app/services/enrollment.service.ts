import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable, switchMap } from 'rxjs';
import { Enrollment } from 'src/models/enrollment.model';


@Injectable({
  providedIn: 'root'
})
export class EnrollmentService {
   private apiUrl = 'http://localhost:3000';

  constructor(private http: HttpClient) {}

  // Check if user is enrolled in a course
    isEnrolled(studentId: string | number, courseId: string | number): Observable<boolean> {
    return this.http.get<Enrollment[]>(
      `${this.apiUrl}/enrollments?studentId=${studentId}&courseId=${courseId}`
    ).pipe(
      map(enrollments => enrollments.length > 0)
    );
  }

  // Update lesson progress - FIXED VERSION
 markLessonComplete(enrollmentId: string, lessonId: number, totalLessons: number): Observable<Enrollment> {
    return this.http.get<Enrollment>(`${this.apiUrl}/enrollments/${enrollmentId}`).pipe(
      switchMap(enrollment => {
        if (enrollment.completedLessons.includes(lessonId)) {
          return new Observable<Enrollment>(observer => observer.next(enrollment));
        }

        const updatedCompletedLessons = [...enrollment.completedLessons, lessonId];
        const updatedEnrollment = {
          ...enrollment,
          completedLessons: updatedCompletedLessons,
          progress: this.calculateProgress(updatedCompletedLessons, totalLessons)
        };

        return this.http.put<Enrollment>(`${this.apiUrl}/enrollments/${enrollmentId}`, updatedEnrollment);
      })
    );
  }

  // Get enrollment by student and course
  getEnrollment(studentId: string | number, courseId: string | number): Observable<Enrollment | null> {
    return this.http.get<Enrollment[]>(
      `${this.apiUrl}/enrollments?studentId=${studentId}&courseId=${courseId}`
    ).pipe(
      map(enrollments => enrollments.length > 0 ? enrollments[0] : null)
    );
  }

  // Get all enrollments for a student
  getStudentEnrollments(studentId: number) {
  return this.http.get<any[]>(`${this.apiUrl}/enrollments`).pipe(
    map((enrollments) =>
      enrollments.filter((e) => Number(e.studentId) === Number(studentId))
    )
  );
}

  // Update enrollment progress
  updateEnrollmentProgress(enrollmentId: string, completedLessons: number[], totalLessons: number): Observable<Enrollment> {
    const updatedEnrollment = {
      progress: this.calculateProgress(completedLessons, totalLessons),
      completedLessons: completedLessons
    };
    
    return this.http.patch<Enrollment>(`${this.apiUrl}/enrollments/${enrollmentId}`, updatedEnrollment);
  }

  private calculateProgress(completedLessons: number[], totalLessons: number): number {
    if (totalLessons === 0) return 0;
    return Math.round((completedLessons.length / totalLessons) * 100);
  }

  updateEnrollment(id: string |number, changes: Partial<Enrollment>) {
  return this.http.patch<Enrollment>(`http://localhost:3000/enrollments/${id}`, changes);
}

}
