import {Component, OnInit} from '@angular/core';
import {ThoughtsService} from "../../service/thoughts.service";
import {CardsLink, CardsType} from "../../types/cards-link";

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit {
  public cardsLink: CardsLink[] = [];
  public cardsType: typeof CardsType = CardsType;

  public mapCardsLink: any;

  constructor(private thoughtsService: ThoughtsService) {
  }

  ngOnInit(): void {
    this.thoughtsService.getAllthougts().subscribe();
    this.thoughtsService.subject.subscribe(val => {
      this.cardsLink = val;
      // console.log(this.cardsLink)
      // pour tester un grand nombre de carte avec le virtual Scroll
      // for (let i = 0; i < 100; i++) {
      //   this.cardsLink = this.cardsLink.concat(val);
      // }
      // https://github.com/angular/components/issues/10114#issuecomment-704009955

      let i: number, j: number, temparray: any[][] = [], chunk = 5;
      for (i = 0, j = this.cardsLink.length; i < j; i += chunk) {
        temparray.push(this.cardsLink.slice(i, i + chunk));
      }
      this.mapCardsLink = temparray;
      console.log(this.mapCardsLink);

    })
  }

}
