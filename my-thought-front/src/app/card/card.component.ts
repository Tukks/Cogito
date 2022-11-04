import {Component, HostListener, Input, OnInit} from '@angular/core';
import {CardsLink, CardsType} from "../../types/cards-link";
import {Dialog} from "@angular/cdk/dialog";
import {ModalEditorComponent} from "../editors/modal-editor/modal-editor.component";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss']
})
export class CardComponent implements OnInit {
  @HostListener('click', ['$event.target'])
  onClick() {
    this.dialog.open(
      ModalEditorComponent, {
        height: '80%',
        width: '80%',
        data: {
          card: this.card,
        },
      });

  }

  public cardsType: typeof CardsType = CardsType;

  @Input()
  public card: CardsLink = {} as CardsLink;

  constructor(public dialog: Dialog) {
  }

  ngOnInit(): void {
  }

}
