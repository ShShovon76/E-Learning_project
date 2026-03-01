
export interface Enrollment {
  id?: number;
  studentId: number;
  courseId: number;
  enrolledDate: string;
  progress: number;
  completedLessons: number[];
  
}