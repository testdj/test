import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AppRoutingModule } from '../app-routing.module';
import { BpmnService } from '../_services/bpmn/bpmn.service';
import { RegistrationService } from '../_services/registration/registration.service';
import { NotifierService } from "angular-notifier";
import { ScienceJournalService } from '../_services/science-journal/science-journal.service';



@Component({
  templateUrl: './registration.component.html',
  selector: 'app-registration',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {


   constructor(
     private registrationService : RegistrationService, 
     private bpmnService : BpmnService, 
     private router : Router, 
     private route : ActivatedRoute, 
     private notifierService : NotifierService) { }

   private formFields = [];
  private formFieldsDto = null;
  private propertyType={};
  private processInstanceID = "";
  private selectedItems={};
  private enumValues = [];
  private dropdownSettings={};
  private multiselectList={};

  ngOnInit() {

      this.dropdownSettings = {
        singleSelection: false,
        unSelectAllText: 'UnSelect All',
        itemsShowLimit: 5,
        idField: 'item_id',
        allowSearchFilter: true,
        selectAllText: 'Select All',
        textField: 'item_text'
      };


      this.registrationService.getRegistrationForm().subscribe(
        res => {
          this.formFields = res.formFields;
          console.log(res);
               this.formFieldsDto = res;
               this.processInstanceID = res.processInstanceId;
               this.formFields.forEach( (field) => {
                 this.propertyType[field.id]=field.type.name;
                 if( field.type.name=='enum'){
                   this.enumValues = Object.keys(field.type.values);
                 }
                 if(field.type.name.includes('multi-select')){
                   this.multiselectList[field.id]=[];
                   this.selectedItems[field.id]=[];
                   let map=new Map(Object.entries(field.type.values));
                   //let values = Object.values(field.type.values);
                   for(let key of Array.from(map.keys())){
                     var item={item_id:"",item_text:""};
                     item.item_text=map.get(key);
                     item.item_id=key;
                     console.log(item);
                     this.multiselectList[field.id].push(item);
                    let selVals=field.value.value;
                    console.log("SelVals:",selVals);
                    if(selVals!=null){
                      if(selVals.includes(key)){
                        this.selectedItems[field.id].push(item);
                      }
                    }
                   }
                 }
               })
            },
        err => {
                console.log(err);
                
              });
  }

  onSelectAll(items: any) {
    //console.log(items);
  }
  onItemSelect(item: any) {
    //console.log(item);
  }

  onSubmit(value, form){

    let o = new Array();
    console.log(this.propertyType);
    for (var property in value) {
      let fieldReset=this.formFields.find(field => field.id==property);
      fieldReset.errMsg=null;
      fieldReset.err=false;
      if(this.propertyType[property].includes('multi-select')){
        let arr = [];
        for(let itm of value[property]){
          arr.push(itm.item_id);
        }
        o.push({fieldId: property, fieldValue: arr})
      }else{
        o.push({fieldId : property, fieldValue : value[property]});
      }
    }

    let x = this.bpmnService.postFormData(this.formFieldsDto.taskId,o);
    console.log(o);

    x.subscribe(
      res => {
        this.router.navigate(['/login']);
        this.notifierService.notify("info","Please confrim your registration via email.");
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
