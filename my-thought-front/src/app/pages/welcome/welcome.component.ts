import {Component, OnDestroy, OnInit} from '@angular/core';
import {StoreService} from "src/app/services/store.service";
import { getLinkPreview, getPreviewFromContent } from "link-preview-js";

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit, OnDestroy {
  public state: any;
  public numbers: any[] = [];

  constructor(public storeService: StoreService) { }

  ngOnInit() {
    getLinkPreview("https://www.youtube.com/watch?v=PN9RDonUjvQ", {
      imagesPropertyType: "og", // fetches only open-graph images
      headers: {
        "user-agent": "googlebot", // fetches with googlebot crawler user agent
        "Accept-Language": "fr-CA", // fetches site for French language
        // ...other optional HTTP request headers
      },
      timeout: 1000
    }).then((data) =>
      console.debug(data)
    );
    this.numbers = Array(5).fill(4);
    this.state = this.storeService.getState();
  }

  ngOnDestroy(): void {
    this.storeService.save(this.state);
  }



}
