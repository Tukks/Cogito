import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from "@angular/core";

@Component({
  selector: 'app-tags-input',
  template: `
    <nz-tag class="label"
            *ngFor="let tag of tags; let i = index"
            [nzMode]="'closeable'"
            (nzOnClose)="handleClose(tag)"
    >
      {{ sliceTagName(tag) }}
    </nz-tag>
    <nz-tag *ngIf="!inputVisible" nzNoAnimation (click)="showInput()" class="label">
      <span nz-icon nzType="plus"></span>
      New Tag
    </nz-tag>
    <input
      #inputElement
      nz-input
      nzSize="small"
      *ngIf="inputVisible"
      type="text"
      [(ngModel)]="inputValue"
      class="tags-input"
      (input)="onInput($event)"
      (blur)="handleInputConfirm()"
      (keydown.enter)="handleInputConfirm()"
      [nzAutocomplete]="auto"
    />
    <nz-autocomplete [nzDataSource]="options" nzBackfill #auto></nz-autocomplete>
  `,
  styleUrls: ['./custom-tags-input.less']
})
export class CustomTagsInput {
  @Input()
  public tags: string[] = [];

  @Output()
  public tagsChange = new EventEmitter<string[]>();

  options: string[] = [];
  inputVisible = false;
  inputValue = '';
  @ViewChild('inputElement', {static: false}) inputElement?: ElementRef;

  onInput(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.options = value ? [value, value + value, value + value + value] : [];
  }
  handleClose(removedTag: {}): void {
    this.tags = this.tags.filter(tag => tag !== removedTag);
    this.tagsChange.emit(this.tags);
  }

  sliceTagName(tag: string): string {
    const isLongTag = tag.length > 20;
    return isLongTag ? `${tag.slice(0, 20)}...` : tag;
  }

  showInput(): void {
    this.inputVisible = true;
    setTimeout(() => {
      this.inputElement?.nativeElement.focus();
    }, 10);
  }

  handleInputConfirm(): void {
    if (this.inputValue && this.tags.indexOf(this.inputValue) === -1) {
      this.tags = [...this.tags, this.inputValue];
      this.tagsChange.emit(this.tags);
    }
    this.inputValue = '';
    this.inputVisible = false;
  }
}
