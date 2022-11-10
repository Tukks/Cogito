import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less']
})
export class LoginComponent implements OnInit {

  constructor(public httpClient: HttpClient) {
  }


  ngOnInit(): void {
  }

  login() {

    this.httpClient.get("/api/login").subscribe();
  }
}
