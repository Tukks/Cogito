import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {CardsLink, CardsType} from "../../../types/cards-link";
import {DIALOG_DATA, DialogRef} from "@angular/cdk/dialog";
import {ThoughtsService} from "../../../service/thoughts.service";
import {MarkdownCardComponent} from "../markdown-card/markdown-card.component";

@Component({
  selector: 'app-modal-editor',
  templateUrl: './modal-editor.component.html',
  styleUrls: ['./modal-editor.component.scss']
})
export class ModalEditorComponent implements OnInit {
  @ViewChild(MarkdownCardComponent) markdownCardComponent!: MarkdownCardComponent;

  public cardsType: typeof CardsType = CardsType;

  constructor(@Inject(DIALOG_DATA) public data: { card: CardsLink },
              public dialogRef: DialogRef<string>, public thoughtService: ThoughtsService) {
  }

  ngOnInit(): void {
  }

  update(): void {
    if (this.data.card.thingType === this.cardsType.MARKDOWN) {
      this.thoughtService.editMarkdown(this.data.card.id, this.markdownCardComponent.stackEditor?.content!).subscribe();
      this.dialogRef.close();
    }
  }

  delete(id: number): void {
    this.thoughtService.delete(id).subscribe();
    this.dialogRef.close();
  }
}
