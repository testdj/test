import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { BpmnService } from '../_services/bpmn/bpmn.service';

@Component({
  templateUrl: './user-tasks.component.html',
  selector: 'app-user-tasks',
  styleUrls: ['./user-tasks.component.scss']
})
export class UserTasksComponent implements OnInit {

  private tasks;

  constructor(
    private bpmnService : BpmnService, 
    private router : Router) { }

  ngOnInit() {
    this.bpmnService.getMyTasks().subscribe(
      res => {
        this.tasks=res;
        console.log(res);
      },
      err => {
        alert("An error has occured!");
        console.log(err);
      });
  }

  onClick(taskId){
    this.router.navigate(['task',taskId]);
  }

}
