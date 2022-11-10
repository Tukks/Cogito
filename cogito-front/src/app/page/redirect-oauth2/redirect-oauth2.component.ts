import {Component, OnInit} from '@angular/core';
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-redirect-oauth2',
  templateUrl: './redirect-oauth2.component.html',
  styleUrls: ['./redirect-oauth2.component.less']
})
export class RedirectOAuth2Component implements OnInit {

  constructor() {
  }

  ngOnInit(): void {
    // redirect on the spring server for auth
    window.location.href = `${environment.server}/oauth2/authorization/google`
  }

}
