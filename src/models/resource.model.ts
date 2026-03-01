export interface Resource {
  id: number;
  title: string;
  type: 'pdf' | 'doc' | 'zip' | 'link';
  url: string;
}