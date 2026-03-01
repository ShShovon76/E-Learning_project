
import { Lesson } from "./lesson.model";
import { User } from "./user.model";

export interface Course {
  id: number;
  title: string;
  description: string;
  instructorId: number;
  instructor: User;  // Required since service always enriches it
  price: number;
  rating: number;
  students: number;
  thumbnail: string;
  category: string;
  level: 'beginner' | 'intermediate' | 'advanced';
  duration: number;
  lessons: Lesson[];
  createdDate: string;
  isFeatured: boolean;
  requirements?: string[];
  learningObjectives?: string[];
}


