import {Component, Input, OnInit} from '@angular/core';
import {CardsLink, CardsType} from "../../types/cards-link";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss']
})
export class CardComponent implements OnInit {
  public cardsType: typeof CardsType = CardsType;

  @Input()
  public card: CardsLink = {} as CardsLink;

  constructor() {
  }

  ngOnInit(): void {
  }

}
