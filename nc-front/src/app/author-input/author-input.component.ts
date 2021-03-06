import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../_services/authentication/authentication.service';
import { ScienceJournalService } from '../_services/science-journal/science-journal.service';
import { FileUploader } from 'ng2-file-upload';
import { BpmnService } from '../_services/bpmn/bpmn.service';
import { NotifierService } from 'angular-notifier';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';

@Component({
  templateUrl: '../_common/generic-form.html',
  selector: 'app-author-input',
  styleUrls: ['./author-input.component.scss']
})
export class AuthorInputComponent implements OnInit {

  constructor(private authService : AuthenticationService, private notifierService : NotifierService, private modalService : NgbModal, private scienceJournalService : ScienceJournalService, private bpmnService : BpmnService, private router : Router, private route : ActivatedRoute) { }

  private formFields = [];
  private formFieldsDto = null;
  private uploadingField;
  private processInstanceID = "";
  private child=new Object();
  private enumValues = [];
  private dropdownSettings={};
  private multiselectList={};
  private uploader: FileUploader;
  private selectedItems={};
  private propertyType={};

  ngOnInit() {

    this.uploader= new FileUploader({ 
      url: "http://localhost:8080/restapi/paper", 
      removeAfterUpload: false, 
      authToken: "Bearer " + this.authService.currentUserValue.token});

    this.dropdownSettings = {
      singleSelection: false,
      selectAllText: 'Select All',
      idField: 'item_id',
      unSelectAllText: 'UnSelect All',
      textField: 'item_text',
      allowSearchFilter: true,
      itemsShowLimit: 5
    };

    this.processInstanceID=this.route.snapshot.paramMap.get('processInstanceID');

    this.bpmnService.getActiveTaskForm(this.processInstanceID).subscribe(
      res => {
        console.log(res);
        this.formFields = res.formFields;
        this.processInstanceID = res.processInstanceId;
        this.formFieldsDto = res;
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

  

  onSubmit(value, form){

    let o = new Array();

    this.uploader.onSuccessItem = (item:any, response:any, status:any, headers:any) => {
      value[this.uploadingField]=response;
      console.log('Field: ',value[this.uploadingField]);
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
          if(fieldReset.properties['type']=='json'){
            value[property] = JSON.stringify(value[property]);
            console.log(value[property]);
          }
          o.push({fieldId : property, fieldValue : value[property]});
        }
      }
      console.log(o);
      let x = this.bpmnService.postProtectedFormData(this.formFieldsDto.taskId,o);

      x.subscribe(
        res => {
          this.notifierService.notify("success","Uspesno poslat rad!");
          this.router.navigate(['pdf']);
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

    this.upload();
    
  }

  fileSelectionChanged(field){
    this.uploadingField=field.id;
  }

  upload(){
    
    this.uploader.uploadItem(this.uploader.queue[0]);
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

}
