export interface User {
    id: number;
    username: string;
    email: string;
}

export interface Comment {
    id: number;
    content: string;
    createdAt: string;  
    username: string;   
    userId: number;
    parentId: number | null;  
    replies: Comment[]; 
}