import { Router } from '@angular/router';
import { BpmnService } from '../_services/bpmn/bpmn.service';
import { ScienceJournalService } from '../_services/science-journal/science-journal.service';
import { Component, OnInit } from '@angular/core';

@Component({
  templateUrl: './urednik-journals.component.html',
  selector: 'app-urednik-journals',
  styleUrls: ['./urednik-journals.component.scss']
})
export class UrednikJournalsComponent implements OnInit {

  private casopisi;
  constructor(private scienceJournalService : ScienceJournalService, private bpmnService : BpmnService, private router : Router) { }

  ngOnInit() {
    this.scienceJournalService.getMineJournals().subscribe(
    res => {
               this.casopisi=res;
               console.log(res);
            },
        err => {
              alert("An error has occured!");
              console.log(err);
            });
  }

  onClick(processInstanceId){
    this.router.navigate(['journal/activate',processInstanceId]);
    console.log("processInstanceId :",processInstanceId);
  }

}
