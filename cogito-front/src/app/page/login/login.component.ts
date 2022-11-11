import {Component, ElementRef, NgZone, OnInit, ViewChild} from '@angular/core';
import {AuthService} from "../../service/auth-service.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less']
})
export class LoginComponent implements OnInit {

  constructor(private authService: AuthService, private router: Router, private zone: NgZone) {
  }

  @ViewChild('buttonDiv') buttonDiv!: ElementRef;


  ngAfterViewInit(): void {
    if (!this.authService.exist()) {
      (window as any).google.accounts.id.initialize({
        client_id: "972567513023-dtp0mkqehkvra4unmb1bu4h6ab8ee07i.apps.googleusercontent.com",
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
      this.router.navigate(["/board"]).then();
    });

  }
}
