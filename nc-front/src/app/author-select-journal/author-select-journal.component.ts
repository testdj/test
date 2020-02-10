import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PaperServiceService } from '../_services/paper/paper-service.service';
import { NotifierService } from 'angular-notifier';
import { BpmnService } from '../_services/bpmn/bpmn.service';

@Component({
  templateUrl: './author-select-journal.component.html',
  selector: 'app-author-select-journal',
  styleUrls: ['./author-select-journal.component.scss']
})
export class AuthorSelectJournalComponent implements OnInit {

  private propertyType={};
  private formFieldsDto = null;
  private selectedItems={};
  private dropdownSettings={};
  private formFields = [];
  private processInstanceID = "";
  private multiselectList={};
  private enumValues = [];

  constructor(private paperService : PaperServiceService, private bpmnService : BpmnService, private notifierService : NotifierService, private router : Router) {  }

  ngOnInit() {
    this.paperService.getCasopisiAndStartProcess().subscribe(
        res => {
          this.formFieldsDto = res;
               console.log(res);
               this.processInstanceID = res.processInstanceId;
               this.formFields = res.formFields;
               this.formFields.forEach( (field) => {
                 this.propertyType[field.id]=field.type.name;
                 if( field.type.name=='enum'){
                   this.enumValues = Object.keys(field.type.values);
                 }
               })
        },
        err =>{

        }
      )
  }

  onSelect(fieldId,key){
    let o = new Array();
    o.push({'fieldId' : fieldId, 'fieldValue' : key});
    console.log(o);
    let x = this.bpmnService.postProtectedFormData(this.formFieldsDto.taskId,o);

    x.subscribe(
      res => {
        this.router.navigate(['input/paper/'+this.processInstanceID])
        this.notifierService.notify("success","Uspesno ste odabrali casopis!");
      },
      err => {
              console.log(err);
              this.formFieldsDto.taskId=err.error["taskID"];
              let map=new Map(Object.entries(err.error));
              map.delete("taskID");
              for(let key of Array.from( map.keys()) ) {
               let field=this.formFields.find(field => field.id==key);  
               field.errMsg=map.get(key);
               field.err=true;
        }
      }
    );
  }

}
