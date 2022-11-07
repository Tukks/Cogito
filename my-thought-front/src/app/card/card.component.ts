import {Component, HostListener, Input, OnInit} from '@angular/core';
import {CardsLink, CardsType} from "../../types/cards-link";
import {Dialog} from "@angular/cdk/dialog";
import {ModalEditorComponent} from "../editors/modal-editor/modal-editor.component";
import {BreakpointObserver, Breakpoints, BreakpointState} from '@angular/cdk/layout';
import {Observable} from "rxjs";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss']
})
export class CardComponent implements OnInit {
  isExtraSmall: Observable<BreakpointState> = this.breakpointObserver.observe(
    Breakpoints.XSmall
  );

  @HostListener('click', ['$event.target'])
  onClick() {
    this.isExtraSmall.subscribe(value => {
      if (value.matches) {
        this.dialog.open(
          ModalEditorComponent, {
            height: '100vh',
            width: '100vw',
            data: {
              card: this.card,
            },
          });
      } else {
        this.dialog.open(
          ModalEditorComponent, {
            height: '80%',
            width: '80%',
            data: {
              card: this.card,
            },
          });
      }
    });


  }

  public cardsType: typeof CardsType = CardsType;

  @Input()
  public card: CardsLink = {} as CardsLink;

  constructor(public dialog: Dialog, public breakpointObserver: BreakpointObserver) {
  }

  ngOnInit(): void {
  }

}
