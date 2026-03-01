import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Lesson } from 'src/models/lesson.model';

@Injectable({
  providedIn: 'root'
})
export class LessonService {
 private apiUrl = 'http://localhost:3000';

  constructor(private http: HttpClient) {}

  getLesson(lessonId: number): Observable<Lesson> {
    return this.http.get<Lesson>(`${this.apiUrl}/lessons/${lessonId}`);
  }

  updateLessonProgress(lessonId: number, isCompleted: boolean): Observable<Lesson> {
    return this.http.patch<Lesson>(`${this.apiUrl}/lessons/${lessonId}`, {
      isCompleted: isCompleted
    });
  }

  getNextLesson(currentLessonOrder: number, courseId: number): Observable<Lesson | null> {
    return this.http.get<Lesson[]>(`${this.apiUrl}/lessons?courseId=${courseId}&order=${currentLessonOrder + 1}`).pipe(
      map(lessons => lessons.length > 0 ? lessons[0] : null)
    );
  }
}
