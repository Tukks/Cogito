import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ThoughtsService} from "../../http-service/thoughts.service";
import {NzMessageService} from "ng-zorro-antd/message";

@Component({
  selector: 'app-handle-share',
  templateUrl: './handle-share.component.html',
  styleUrls: ['./handle-share.component.less']
})
export class HandleShareComponent implements OnInit {
  timeoutId: string | number | NodeJS.Timeout | undefined;
  tags: string[] = [];
  showTagsAdd: boolean = false;

  constructor(private route: ActivatedRoute, private thoughtsService: ThoughtsService, private message: NzMessageService) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe((param: any) => {
      console.log(param);
      // On Android text is equals to the url
      if (param.text && param.text != '') {
        this.thoughtsService.save(param.text).subscribe(val => {
          this.message.success("Saved successfully" + param.text)
          this.timeoutId = setTimeout(() => window.open('', '_self')!.close(), 2500);
        });
      } else if (param.url && param.url != '') {
        this.thoughtsService.save(param.url).subscribe(val => {
          this.message.success("Saved successfully" + param.url)
          this.timeoutId = setTimeout(() => window.open('', '_self')!.close(), 2500);
        });
      } else {
        this.thoughtsService.save(param.title).subscribe(val => {
          this.message.success("Saved successfully" + param.title)
          this.timeoutId = setTimeout(() => window.open('', '_self')!.close(), 2500);
        });
      }
    });

  }

  removeTimeout() {
    this.showTagsAdd = true;
    clearTimeout(this.timeoutId);
  }

  update(): void {
    // TODO add tag
  }
}
