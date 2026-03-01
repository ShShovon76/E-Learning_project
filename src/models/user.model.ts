export interface User {
  id: number;
  name: string;
  email: string;
  password?: string;
  role: 'student' | 'instructor' | 'admin';
  avatar: string;
  joinDate: string;
  bio?: string;
  
  // New optional properties for profile
  phone?: string;
  location?: string;
  website?: string;
  twitter?: string;
  linkedin?: string;
  github?: string;
  
  // Instructor-specific properties
  expertise?: string;
  experience?: string;
  hourlyRate?: number;
  
  // System properties
  lastLogin?: string;
  isVerified?: boolean;
}