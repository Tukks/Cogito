import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less']
})
export class LoginComponent implements OnInit {

  constructor(public httpService: HttpClient) {
  }

  ngOnInit(): void {
  }

  login() {
//       this.httpService.post(`https://accounts.google.com/o/oauth2/v2/auth?
// scope=openid&access_type=offline&include_granted_scopes=true&
// response_type=code&
// state=state_parameter_passthrough_value&
// redirect_uri=http%3A%2F%2Flocalhost%3A4200&
// client_id=972567513023-dtp0mkqehkvra4unmb1bu4h6ab8ee07i.apps.googleusercontent.com`, {}).subscribe()
    window.location.href =
      `https://accounts.google.com/o/oauth2/v2/auth?
scope=openid&access_type=online&include_granted_scopes=true&
response_type=token&
state=state_parameter_passthrough_value&
redirect_uri=http%3A%2F%2Flocalhost%3A4200&
client_id=972567513023-dtp0mkqehkvra4unmb1bu4h6ab8ee07i.apps.googleusercontent.com`;

  }
}
