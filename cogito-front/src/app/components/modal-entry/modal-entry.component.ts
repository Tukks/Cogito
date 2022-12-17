import { Component, HostListener, Inject, OnDestroy, OnInit } from "@angular/core";
import { DIALOG_DATA, DialogRef } from "@angular/cdk/dialog";
import { CardType } from "../../types/cards-link";

@Component({
  selector: 'app-modal-entry',
  templateUrl: './modal-entry.component.html',
  styleUrls: ['./modal-entry.component.less']
})
export class ModalEntryComponent implements OnInit, OnDestroy {
  @HostListener('window:popstate', ['$event'])
  dismissModal() {
    this.dialogRef.close();
  }
  constructor(@Inject(DIALOG_DATA) public data: { card: CardType },
              public dialogRef: DialogRef<string>) { }

  ngOnInit(): void {
    const modalState = {
      modal: true,
      desc: 'fake state for our modal',
    };
    history.pushState(modalState, '');

  }

  close(): void {
    this.dialogRef.close();
  }
  ngOnDestroy() {
    if (window.history.state.modal) {
      history.back();
    }
  }

}
