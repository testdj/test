import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormControl } from '@angular/forms';
import { first } from 'rxjs/operators';
import { AuthenticationService } from '../_services/authentication/authentication.service';

@Component({
  templateUrl: './login.component.html',
  selector: 'app-login',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loading = false;
  returnUrl: string;
  submitted = false;
  error = '';

	loginForm = new FormGroup({
    password: new FormControl(''),
    	username: new FormControl(''),
  	});

  constructor(private authenticationService : AuthenticationService,private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    this.authenticationService.logout();

      // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  onSubmit(){
  	this.submitted = true;

      // stop here if form is invalid
      if (this.loginForm.invalid) {
          return;
      }

      this.loading = true;
      this.authenticationService.login(this.loginForm.value["username"],this.loginForm.value["password"])
          .pipe(first())
          .subscribe(
              data => {
                  this.router.navigate([this.returnUrl]);
              },
              err => {
                console.log(err);
                this.loading = false;
                  this.error = err.error;
              });
  }

}
