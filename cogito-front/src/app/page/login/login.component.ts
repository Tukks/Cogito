import {Component, ElementRef, NgZone, OnInit, ViewChild} from '@angular/core';
import {AuthService} from "../../http-service/auth-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less']
})
export class LoginComponent implements OnInit {

  constructor(private authService: AuthService, private router: Router, private zone: NgZone, private activatedRoute: ActivatedRoute) {
  }

  @ViewChild('buttonDiv') buttonDiv!: ElementRef;


  ngAfterViewInit(): void {
    if (!this.authService.exist()) {
      (window as any).google.accounts.id.initialize({
        client_id: environment.clientId,
        auto_select: true,
        callback: this.handleCredentialResponse.bind(this)
      });
      (window as any).google.accounts.id.renderButton(
        this.buttonDiv.nativeElement,
        {theme: "outline", size: "large"}  // customization attributes
      );
      (window as any).google.accounts.id.prompt();
    }
  }

  ngOnInit(): void {
    if (this.authService.exist()) {
      this.router.navigate(['/board']);
    }
  }

  handleCredentialResponse(response: { credential: string; }) {
    this.zone.run(() => {
      this.authService.saveJwt(response.credential);
      const queryParamRedirect = this.activatedRoute.snapshot.queryParams['redirectTo'];
      this.router.navigateByUrl(queryParamRedirect ?
        queryParamRedirect : "/board");

    });

  }
}
