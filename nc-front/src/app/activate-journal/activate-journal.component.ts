import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { Router, ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { BpmnService } from '../_services/bpmn/bpmn.service';
import { ScienceJournalService } from '../_services/science-journal/science-journal.service';


@Component({
  templateUrl: './activate-journal.component.html',
  selector: 'app-activate-journal',
  styleUrls: ['./activate-journal.component.scss']
})
export class ActivateJournalComponent implements OnInit {

  constructor(private scienceJournalService : ScienceJournalService, private bpmnService : BpmnService, private router : Router, private route : ActivatedRoute) { }

  private enumValues = [];
  private formFieldsDto = null;
  private processInstanceID = "";
  private multiselectList={};
  private formFields = [];
  private selectedItems={};
  private dropdownSettings={};
  private propertyType={};

  ngOnInit() {

  		this.dropdownSettings = {
	      singleSelection: false,
	      unSelectAllText: 'UnSelect All',
	      idField: 'item_id',
	      selectAllText: 'Select All',
	      textField: 'item_text',
	      allowSearchFilter: true,
	      itemsShowLimit: 5
	    };

  		this.processInstanceID=this.route.snapshot.paramMap.get('processInstanceID');

    	this.bpmnService.getActiveTaskForm(this.processInstanceID).subscribe(
    		res => {
          this.formFields = res.formFields;
               this.formFieldsDto = res;
               console.log(res);
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
                this.router.navigate(['stagod']);
              });
  }

  
  onSubmit(value, form){
    console.log(this.propertyType);
    let o = new Array();
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
    console.log(o);
    let x = this.bpmnService.postProtectedFormData(this.formFieldsDto.taskId,o);
    
    x.subscribe(
      res => {
        this.ngOnInit();
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
    
    onItemSelect(item: any) {
    }
    onSelectAll(items: any) {
    }
  }
  