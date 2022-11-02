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

  public mapCardsLink: any;
  constructor(private activatedRoute: ActivatedRoute, private thoughtsService: ThoughtsService) {
  }

  ngOnInit() {
    this.thoughtsService.getAllthougts().subscribe(val => {
      this.cardsLink = val;
      console.log(this.cardsLink)
      // pour tester un grand nombre de carte avec le virtual Scroll
      // for (let i = 0; i < 100; i++) {
      //   this.cardsLink = this.cardsLink.concat(val);
      // }
      // https://github.com/angular/components/issues/10114#issuecomment-704009955

      let i: number, j: number, temparray: any[][] = [], chunk = 3;
      for (i = 0, j = this.cardsLink.length; i < j; i += chunk) {
        temparray.push(this.cardsLink.slice(i, i + chunk));
      }
      this.mapCardsLink = temparray;
      console.log(this.mapCardsLink);

    });
    // this.thoughtsService.saveLink("https://twitter.com/Foone/status/1587144406096052226").subscribe(val => this.cardsLink = val);


    this.folder = this.activatedRoute.snapshot.paramMap.get('id');
  }

  search($event) {
    console.log($event.detail.value);

  }
}
