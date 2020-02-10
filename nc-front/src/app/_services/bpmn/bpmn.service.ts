import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

const BASE_CONTROLLER_URL = "http://localhost:8080/restapi/bpmn"

@Injectable({
  providedIn: 'root'
})
export class BpmnService {

  constructor(private http: HttpClient) { }


  postProtectedFormData(taskID,formData): Observable<any>{
    return this.http.post(`${BASE_CONTROLLER_URL}/protected/form/`+taskID,formData);
  }
  
  postFormData(taskID,formData): Observable<any>{
    return this.http.post(`${BASE_CONTROLLER_URL}/form/`+taskID,formData);
  }

  getTask(id): Observable<any>{
    return this.http.get(`${BASE_CONTROLLER_URL}/tasks/`+id);
  }
  
  getActiveTaskForm(processInstanceId): Observable<any>{
    return this.http.get(`${BASE_CONTROLLER_URL}/task/active/`+processInstanceId);
  }
 
  getMyTasks(): Observable<any>{
    return this.http.get(`${BASE_CONTROLLER_URL}/tasks`);
  }
  
  getNonActivatedJournals(): Observable<any>{
    return this.http.get(`${BASE_CONTROLLER_URL}/admin/journal`);
  }

}
