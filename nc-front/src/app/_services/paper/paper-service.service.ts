import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient,  HttpHeaders } from '@angular/common/http';

const BASE_CONTROLLER_URL = "http://localhost:8080/restapi/paper"

@Injectable({
  providedIn: 'root'
})
export class PaperServiceService {

  private headers = new HttpHeaders();

  constructor(private http: HttpClient) { }

  getCasopisPdf(filename): Observable<any>{
    this.headers = this.headers.set('Content-Type', 'application/pdf');
    return this.http.get(`${BASE_CONTROLLER_URL}/files/`+filename,{headers: this.headers,responseType: 'blob'});
  }
  getCasopisiAndStartProcess(): Observable<any>{
    return this.http.get(`${BASE_CONTROLLER_URL}`);
  }

  
}
