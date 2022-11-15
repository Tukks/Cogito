import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../http-service/auth-service.service";
import {Router} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginService} from "../../http-service/login.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less']
})
export class LoginComponent implements OnInit {
  register = new FormGroup({
    email: new FormControl('', [Validators.email]),
    password: new FormControl('', [Validators.min(12)]),
  });

  constructor(private loginService: LoginService,
              private authService: AuthService, private router: Router) {
  }


  ngOnInit(): void {
  }

  save() {
    this.loginService.login(this.register.getRawValue()).subscribe(() => {
      this.router.navigateByUrl("/board");
    });
  }

  goToNewAccount() {
    this.router.navigateByUrl("/register");
  }
}
