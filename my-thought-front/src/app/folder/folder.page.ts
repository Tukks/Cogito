import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ThoughtsService} from "../service/thoughts.service";
import {CardsLink, CardsType} from "../types/cards-link";

@Component({
  selector: 'app-folder',
  templateUrl: './folder.page.html',
  styleUrls: ['./folder.page.scss'],
})
export class FolderPage implements OnInit {
  public folder: string;
  public cardsLink: CardsLink[] = [];
  public cardsType: typeof CardsType = CardsType;

  constructor(private activatedRoute: ActivatedRoute, private thoughtsService: ThoughtsService) {
  }

  ngOnInit() {
    this.thoughtsService.getAllthougts().subscribe(val => {
      for (let i = 0; i < 100; i++) {
        this.cardsLink = this.cardsLink.concat(val);
      }
    });
    // this.thoughtsService.saveLink("https://twitter.com/Foone/status/1587144406096052226").subscribe(val => this.cardsLink = val);


    this.folder = this.activatedRoute.snapshot.paramMap.get('id');
  }

  search($event) {
    console.log($event.detail.value);

  }
}
