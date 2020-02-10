import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

const BASE_BPMN_CONTROLLER_URL = "http://localhost:8080/restapi/bpmn"
const BASE_REG_CONTROLLER_URL = "http://localhost:8080/restapi/registration"


@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(private http: HttpClient) { }

  getRegistrationForm(): Observable<any>{
    return this.http.get(`${BASE_REG_CONTROLLER_URL}`);
  }

  /*getUrednikRegistrationForm(): Observable<any>{
    return this.http.get(`${BASE_BPMN_CONTROLLER_URL}/urednik`);
  }*/

  getNonActivatedUsers(): Observable<any>{
    return this.http.get(`${BASE_BPMN_CONTROLLER_URL}/admin/registration`);
  }
  checkValidity(processInstanceID): Observable<any>{
    return this.http.get(`${BASE_REG_CONTROLLER_URL}/valid/`+processInstanceID);
  }


  
}
