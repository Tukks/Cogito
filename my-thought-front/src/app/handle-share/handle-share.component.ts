import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ThoughtsService} from "../../service/thoughts.service";

@Component({
  selector: 'app-handle-share',
  templateUrl: './handle-share.component.html',
  styleUrls: ['./handle-share.component.less']
})
export class HandleShareComponent implements OnInit {
  timeoutId: string | number | NodeJS.Timeout | undefined;
  tags: string[] = [];
  showTagsAdd: boolean = false;

  constructor(private route: ActivatedRoute, private thoughtsService: ThoughtsService) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe((param: any) => {
      if (param.tex && param.text != '') {
        this.thoughtsService.save(param.text).subscribe();
      }
      this.timeoutId = setTimeout(() => window.open('', '_self')!.close(), 1000);
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
