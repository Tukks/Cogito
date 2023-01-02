import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from "@angular/core";
import { ThoughtsService } from "../http-service/thoughts.service";

@Component({
  selector: 'app-tags-input',
  templateUrl: './custom-tags-input.html',
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

  constructor(
    public thoughtService: ThoughtsService
  ) {}
  onInput(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    if (value !== "" && value) {
      this.thoughtService.getTagsStartWith(value).subscribe(val => {
        this.options = val ? val : [];
      });
    }
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
