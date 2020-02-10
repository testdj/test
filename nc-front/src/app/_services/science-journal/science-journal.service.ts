import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

const BASE_BPMN_CONTROLLER_URL = "http://localhost:8080/restapi/bpmn"
const BASE_JOURNAL_CONTROLLER_URL = "http://localhost:8080/restapi/journal"

@Injectable({
  providedIn: 'root'
})
export class ScienceJournalService {

  constructor(private http: HttpClient) { }
  
  getMineJournals(): Observable<any>{
    return this.http.get(`${BASE_JOURNAL_CONTROLLER_URL}/mine`);
  }
  getNewJournalForm(): Observable<any>{
    return this.http.get(`${BASE_JOURNAL_CONTROLLER_URL}`);
  }
}
