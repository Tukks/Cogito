import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { LoginService } from "../../http-service/login.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.less']
})
export class RegisterComponent implements OnInit {

  register = new FormGroup({
    email: new FormControl('', [Validators.email]),
    password: new FormControl('', [Validators.min(12)]),
  });

  constructor(private loginService: LoginService, private router: Router) {
  }

  ngOnInit(): void {
  }

  save() {
    const user = this.register.getRawValue();
    this.loginService.register(user).subscribe(() => {
      this.router.navigateByUrl("/login");
    });

  }

  backToLogin() {
    this.router.navigateByUrl("/login");
  }

}
