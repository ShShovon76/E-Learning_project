
export interface Lesson {
  id?: number;
  courseId: number;
  title: string;
  description: string;

  type: 'article' | 'video';   // ← REQUIRED
  content?: string;            // ← REQUIRED for article lessons
  videoUrl?: string;           // ← REQUIRED for video lessons

  duration: number;
  order: number;

  isFree: boolean;             // ← Exists in your backend
  isCompleted?: boolean; 
}