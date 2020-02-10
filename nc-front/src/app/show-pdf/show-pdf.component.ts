import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PaperServiceService } from '../_services/paper/paper-service.service';

@Component({
  templateUrl: './show-pdf.component.html',
  selector: 'app-show-pdf',
  styleUrls: ['./show-pdf.component.scss']
})
export class ShowPdfComponent implements OnInit {

  private src;

  constructor(private paperService : PaperServiceService, private modalService : NgbModal) { }

  ngOnInit() {
      this.paperService.getCasopisPdf("proba.pdf").subscribe(
          res => {
            this.src=res;
            console.log(res);
          },
          err => {

          }
        )
  }

  open(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title',windowClass : "myCustomModalClass"}).result.then((result) => {
      console.log(result);
    }, (reason) => {
      console.log(reason);
    });
  }

}
