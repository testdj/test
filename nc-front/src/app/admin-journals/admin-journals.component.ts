import { BpmnService } from '../_services/bpmn/bpmn.service';
import { Component, OnInit } from '@angular/core';
import { NotifierService } from "angular-notifier";
import { ScienceJournalService } from '../_services/science-journal/science-journal.service';



@Component({
  templateUrl: './admin-journals.component.html',
  selector: 'app-admin-journals',
  styleUrls: ['./admin-journals.component.scss']
})
export class AdminJournalsComponent implements OnInit {

	private casopisi;
  constructor(private scienceJournalService : ScienceJournalService, private bpmnService : BpmnService, private notifierService: NotifierService) { }

  ngOnInit() {
  	this.bpmnService.getNonActivatedJournals().subscribe(
		res => {
               this.casopisi=res;
            },
        err => {
              console.log(err);
              alert("An error has occured!");
            });
  }

  onClick(taskID,flag){
    let dtos=new Array();
    dtos.push({fieldId : "potvrda_admina", fieldValue : flag});
    this.bpmnService.postProtectedFormData(taskID,dtos).subscribe(
        res => {
          this.notifierService.notify("success","Accepted successfully");
          this.ngOnInit();
        },
        err => {
          alert("Error Occured!");
          console.log(err);
        }
      );
  }

}
