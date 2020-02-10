import { Component, OnInit } from '@angular/core';
import { NotifierService } from "angular-notifier";



@Component({
  selector: 'app-urednik-register',
  templateUrl: './urednik-register.component.html',
  styleUrls: ['./urednik-register.component.scss']
})
export class UrednikRegisterComponent implements OnInit {

    constructor(private notifierService: NotifierService) { }
  
    ngOnInit() {
    }
  
  }