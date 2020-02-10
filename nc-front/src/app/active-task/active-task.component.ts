import { Component, OnInit } from '@angular/core';
import { PaperServiceService } from '../_services/paper/paper-service.service';
import { ScienceJournalService } from '../_services/science-journal/science-journal.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BpmnService } from '../_services/bpmn/bpmn.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  templateUrl: '../_common/generic-form.html',
  selector: 'app-active-task',
  styleUrls: ['./active-task.component.scss']
})
export class ActiveTaskComponent implements OnInit {

  constructor(private paperService : PaperServiceService, private modalService : NgbModal, private scienceJournalService : ScienceJournalService, private bpmnService : BpmnService, private router : Router, private route : ActivatedRoute) { }

  private formFields = [];
  private formFieldsDto = null;
  private multiselectList={};
  private processInstanceID = "";
  private enumValues = [];
  private propertyType={};
  private selectedItems={};
  private taskId;
  private dropdownSettings={};
  private src;

  ngOnInit() {

    this.dropdownSettings = {
      unSelectAllText: 'UnSelect All',
      idField: 'item_id',
      textField: 'item_text',
      singleSelection: false,
      selectAllText: 'Select All',
      allowSearchFilter: true,
      itemsShowLimit: 5
    };

    this.taskId=this.route.snapshot.paramMap.get('taskID');

    this.bpmnService.getTask(this.taskId).subscribe(
      res => {      
                this.formFields = res.formFields;
                this.formFieldsDto = res;
                this.processInstanceID = res.processInstanceId;
                console.log(res);
                this.formFields.forEach( (field) => {
                  this.propertyType[field.id]=field.type.name;
                  if(field.properties['type']=='file' && this.isReadonly(field.validationConstraints)){
                    this.paperService.getCasopisPdf(field.value.value).subscribe(
                      res => {
                        this.src=res;
                      },
                      err => {

                      }
                    )
                  }

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
                this.router.navigate(['stagod']);
              });
  }

  openPdf(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title',windowClass : "myCustomModalClass"}).result.then((result) => {

    }, (reason) => {

    });
  }
  
  onSelectAll(items: any) {
  }
  onItemSelect(item: any) {
  }

  onSubmit(value, form){
    console.log(this.propertyType);
    let o = new Array();
    for (var property in value) {
      let fieldReset=this.formFields.find(field => field.id==property);
      fieldReset.errMsg=null;
      fieldReset.err=false;
      if(!this.isReadonly(fieldReset.validationConstraints)){
        if(this.propertyType[property].includes('multi-select')){
          let arr = [];
          for(let itm of value[property]){
            arr.push(itm.item_id);
          }
          o.push({fieldId: property, fieldValue: arr})
        }else{
          if(this.propertyType[property].includes('add-children')){
            value[property] = JSON.stringify(value[property]);
          }
          o.push({fieldId : property, fieldValue : value[property]});
        }
      }
    }

    console.log(o);
    let x = this.bpmnService.postProtectedFormData(this.formFieldsDto.taskId,o);

    x.subscribe(
      res => {
        this.router.navigate(['tasks']);
      },
      err => {
        console.log(err);
        let map=new Map(Object.entries(err.error));
        this.formFieldsDto.taskId=err.error["taskID"];
        map.delete("taskID");
        for(let key of Array.from( map.keys()) ) {
          let field=this.formFields.find(field => field.id==key);  
          field.errMsg=map.get(key);
          field.err=true;
        }
      }
      );

  }


isReadonly(constraints){
  if(constraints.length!=0){
    if(constraints[0].name=='readonly')
      return true;
    else
      return false;
  }else{
    return false;
  }
}

openChild(content,fieldID) {
  this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
    console.log(result);
    let field = this.formFields.find(field => field.id==fieldID);
    field.value.value=[];
    field.value.value.push(result); 
  }, (reason) => {
    console.log(reason);
  });
}

}
