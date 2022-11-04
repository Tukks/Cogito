import {Component, Inject, OnInit} from '@angular/core';
import {CardsLink, CardsType} from "../../../types/cards-link";
import {DIALOG_DATA, DialogRef} from "@angular/cdk/dialog";
import {ThoughtsService} from "../../../service/thoughts.service";

@Component({
  selector: 'app-modal-editor',
  templateUrl: './modal-editor.component.html',
  styleUrls: ['./modal-editor.component.scss']
})
export class ModalEditorComponent implements OnInit {
  public cardsType: typeof CardsType = CardsType;

  constructor(@Inject(DIALOG_DATA) public data: { card: CardsLink },
              public dialogRef: DialogRef<string>, public thoughtService: ThoughtsService) {
  }

  ngOnInit(): void {
    console.log(this.data.card);
  }

  delete(id: number): void {
    this.thoughtService.delete(id).subscribe();
    this.dialogRef.close();
  }
}
