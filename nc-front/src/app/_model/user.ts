export interface User {
    expiresIn?:number;
    username: string;
    role?:string;
    token?: string;
}