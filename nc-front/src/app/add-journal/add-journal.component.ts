import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { BpmnService } from '../_services/bpmn/bpmn.service';
import { ScienceJournalService } from '../_services/science-journal/science-journal.service';


@Component({
  templateUrl: './add-journal.component.html',
  selector: 'app-add-journal',
  styleUrls: ['./add-journal.component.scss']
})
export class AddJournalComponent implements OnInit {

  constructor(private scienceJournalService : ScienceJournalService, private bpmnService : BpmnService, private router : Router) { }

  private formFieldsDto = null;
  private multiSelectValues;
  private formFields = [];
  private enumValues = [];
  private processInstanceID = "";

  ngOnInit() {
    	this.scienceJournalService.getNewJournalForm().subscribe(
    		res => {
          this.formFields = res.formFields;
               this.formFieldsDto = res;
               this.processInstanceID = res.processInstanceId;
               console.log(res);
               this.formFields.forEach( (field) => {
                 if( field.type.name=='enum'){
                   this.enumValues = Object.keys(field.type.values);
                 }
               })
            },
        err => {
          alert("An error has occured!");
              console.log(err);
            });
  }

  onSubmit(value, form){
    let o = new Array();
    for (var property in value) {
      if(property=="nacin_placanja" || property=="naucne_oblasti"){
        value[property]=new Array();
        value[property].push("1","2");
        console.log(value[property]);
      }
      o.push({fieldId : property, fieldValue : value[property]});
    }

    let x = this.bpmnService.postProtectedFormData(this.formFieldsDto.taskId,o);
    console.log(o);

    x.subscribe(
      res => {
        this.router.navigate(['/journal/activate', this.processInstanceID]);
      },
      err => {
              console.log(err);
              this.formFieldsDto.taskId=err["taskID"];
              let map=new Map(Object.entries(err));
              map.delete("taskID");
              for(let key of Array.from( map.keys()) ) {
                 if(key=="unique"){
                  alert("Username or email are not unique!");
                  let fieldEmail=this.formFields.find(field => field.id=="email");
                  let fieldUsername=this.formFields.find(field => field.id=="username");
                  fieldEmail.err=true;
                  fieldUsername.err=true;
                  break;
                }
               let field=this.formFields.find(field => field.id==key);  
               field.errMsg=map[key];
               field.err=true;
        }
      }
    );
  }

}
