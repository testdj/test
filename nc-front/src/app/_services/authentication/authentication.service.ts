import { Injectable } from '@angular/core';
import { User } from '../../_model/user';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { BehaviorSubject, Observable } from 'rxjs';



@Injectable({ providedIn: 'root' })
export class AuthenticationService {

    private LOGIN_URL = "http://localhost:8080/restapi/auth/login"

    public currentUser: Observable<User>;
    private currentUserSubject: BehaviorSubject<User>;

    constructor(private http: HttpClient) {
        this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
        this.currentUser = this.currentUserSubject.asObservable();
    }

    public get currentUserValue(): User {
        return this.currentUserSubject.value;
    }

    logout() {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);
    }
    login(username: string, password: string) {
        return this.http.post<any>(this.LOGIN_URL, { username, password })
            .pipe(map(user => {
                // login successful if there's a jwt token in the response
                if (user && user.token) {
                    // store user details and jwt token in local storage to keep user logged in between page refreshes
                    console.log('USER ROLE: '+user.role);
                    localStorage.setItem('currentUser', JSON.stringify(user));
                    this.currentUserSubject.next(user);
                }

                return user;
            }));
    }

}