import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ThoughtsService } from '../../http-service/thoughts.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { Tag } from '../../types/cards-link';

@Component({
  selector: 'app-handle-share',
  templateUrl: './handle-share.component.html',
  styleUrls: ['./handle-share.component.less'],
})
export class HandleShareComponent implements OnInit {
  timeoutId: string | number | NodeJS.Timeout | undefined;
  tags: string[] = [];
  showTagsAdd: boolean = false;

  currentNote: string = '';

  constructor(
    private route: ActivatedRoute,
    private thoughtsService: ThoughtsService,
    private message: NzMessageService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((param: any) => {
      // On Android text is equals to the url
      if (param.text && param.text != '') {
        this.timeoutId = setTimeout(() => {
          this.currentNote = param.text;
          this.thoughtsService.save({ note: param.text }).subscribe((val) => {
            this.message.success('Saved successfully');
          });
          window.open('', '_self')!.close();
        }, 2000);
      } else if (param.url && param.url != '') {
        this.timeoutId = setTimeout(() => {
          this.currentNote = param.url;
          this.thoughtsService.save({ note: param.url }).subscribe((val) => {
            this.message.success('Saved successfully');
          });
          window.open('', '_self')!.close();
        }, 2500);
      } else {
        this.timeoutId = setTimeout(() => {
          this.currentNote = param.title;
          this.thoughtsService.save({ note: param.title }).subscribe((val) => {
            this.message.success('Saved successfully');
          });
          window.open('', '_self')!.close();
        }, 2500);
      }
    });
  }

  removeTimeout() {
    this.showTagsAdd = true;
    clearTimeout(this.timeoutId);
  }

  update(): void {
    let tagsToSend: Tag[] = [];
    if (this.tags.length !== 0) {
      tagsToSend = this.tags.map((value) => {
        return { tag: value, hidden: false };
      });
    }
    this.thoughtsService
      .save({ note: this.currentNote, tags: tagsToSend })
      .subscribe((val) => {
        this.message.success('Saved successfully');
      });
  }

  addQuickTag(tag: string) {
    this.tags.push(tag);
  }
}
