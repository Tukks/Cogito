import { ChangeDetectionStrategy, Component, HostListener, Input, OnDestroy, OnInit } from "@angular/core";
import { CardsType, CardType, LinkCard, NoteCard, TweetCard } from "../../types/cards-link";
import { Dialog } from "@angular/cdk/dialog";
import { BreakpointObserver, Breakpoints, BreakpointState } from "@angular/cdk/layout";
import { Observable, Subscription } from "rxjs";
import { NzContextMenuService, NzDropdownMenuComponent } from "ng-zorro-antd/dropdown";
import { ThoughtsService } from "../../http-service/thoughts.service";
import { Clipboard } from "@angular/cdk/clipboard";
import { NzMessageService } from "ng-zorro-antd/message";
import { ModalEntryComponent } from "../modal-entry/modal-entry.component";
import { Router } from "@angular/router";

@Component({
  selector: "app-card",
  templateUrl: "./card.component.html",
  styleUrls: ["./card.component.less"],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CardComponent implements OnInit, OnDestroy {
  public cardsType: typeof CardsType = CardsType;

  @Input()
  public card: CardType = {} as CardType;

  public isTodo: boolean = false;
  isExtraSmall: Observable<BreakpointState> = this.breakpointObserver.observe(
    Breakpoints.XSmall
  );
  $isExtraSmall: Subscription | undefined;
  contextMenu($event: MouseEvent, menu: NzDropdownMenuComponent): void {
    this.nzContextMenuService.create($event, menu);
  }

  @HostListener('click', ['$event'])
  onClick($event: any) {
    this.$isExtraSmall = this.isExtraSmall.subscribe((value) => {
      if (value.matches) {
        this.dialog.open(ModalEntryComponent, {
          height: '100vh',
          width: '100vw',
          data: {
            card: this.card,
          },
        });
      } else {
        this.dialog.open(ModalEntryComponent, {
          height: '95%',
          width: '95%',
          data: {
            card: this.card,
          },
        });
      }
    });
  }

  constructor(
    public dialog: Dialog,
    public breakpointObserver: BreakpointObserver,
    private nzContextMenuService: NzContextMenuService,
    private thoughtService: ThoughtsService,
    private clipboard: Clipboard,
    private nzMessageService: NzMessageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isTodo = !!this.card.tags.find(value => value.tag.toLowerCase() === 'todo');
  }

  deleteCard() {
    this.thoughtService.delete(this.card.id).subscribe();
  }

  copyContent() {
    switch (this.card.thingType) {
      case CardsType.LINK:
        this.clipboard.copy((this.card as LinkCard).url);
        break;
      case CardsType.MARKDOWN:
        this.clipboard.copy((this.card as NoteCard).markdown);
        break;
      case CardsType.TWEET:
        this.clipboard.copy((this.card as TweetCard).url);
        break;
    }
    this.nzMessageService.success('Content copied !');
  }

  openLink($event: Event) {
    $event.stopPropagation();
    $event.preventDefault();
    window.open((this.card as LinkCard).url, '_blank');
  }

  closeMenu() {
    this.nzContextMenuService.close();
  }

  openNewTab() {
    const url = this.router.serializeUrl(
      this.router.createUrlTree(['/' + this.card.id])
    );
    window.open(url, '_blank');
  }

  markAsDone($event: MouseEvent) {
    $event.preventDefault();
    $event.stopPropagation();
    this.thoughtService
      .editThing(this.card.id, {
        ...this.card,
        tags: this.card.tags.filter(value => value.tag.toLowerCase() != 'todo'),
      })
      .subscribe();
  }

  ngOnDestroy(): void {
    if (this.$isExtraSmall) {
      this.$isExtraSmall.unsubscribe();
    }
  }
}
