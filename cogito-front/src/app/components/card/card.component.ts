import {Component, HostListener, Input, OnInit} from '@angular/core';
import {CardsType, CardType} from "../../types/cards-link";
import {Dialog} from "@angular/cdk/dialog";
import {ModalEditorComponent} from "../modal-editor/modal-editor.component";
import {BreakpointObserver, Breakpoints, BreakpointState} from '@angular/cdk/layout';
import {Observable} from "rxjs";
import {NzContextMenuService, NzDropdownMenuComponent} from "ng-zorro-antd/dropdown";
import {ThoughtsService} from "../../service/thoughts.service";
import {Clipboard} from '@angular/cdk/clipboard';
import {NzMessageService} from "ng-zorro-antd/message";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss']
})
export class CardComponent implements OnInit {

  public cardsType: typeof CardsType = CardsType;

  @Input()
  public card: CardType = {} as CardType;

  isExtraSmall: Observable<BreakpointState> = this.breakpointObserver.observe(
    Breakpoints.XSmall
  );

  contextMenu($event: MouseEvent, menu: NzDropdownMenuComponent): void {
    this.nzContextMenuService.create($event, menu);
  }

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


  constructor(public dialog: Dialog,
              public breakpointObserver: BreakpointObserver,
              private nzContextMenuService: NzContextMenuService,
              private thoughtService: ThoughtsService,
              private clipboard: Clipboard,
              private nzMessageService: NzMessageService) {
  }

  ngOnInit(): void {
  }

  deleteCard() {
    this.thoughtService.delete(this.card.id).subscribe();
  }

  copyContent() {
    switch (this.card.thingType) {
      case CardsType.LINK:
        this.clipboard.copy(this.card.url);
        break;
      case CardsType.MARKDOWN:
        this.clipboard.copy(this.card.markdown);
        break;
      case CardsType.TWEET:
        this.clipboard.copy(this.card.url);
        break;
    }
    this.nzMessageService.success("Content copied !");
  }

  openLink($event: Event) {
    $event.stopPropagation();
    $event.preventDefault();
    window.open(this.card.url, "_blank");
  }

  closeMenu() {
    this.nzContextMenuService.close();
  }
}
