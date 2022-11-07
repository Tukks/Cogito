import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ThoughtsService} from "../../service/thoughts.service";

@Component({
  selector: 'app-handle-share',
  templateUrl: './handle-share.component.html',
  styleUrls: ['./handle-share.component.less']
})
export class HandleShareComponent implements OnInit {

  constructor(private route: ActivatedRoute, private thoughtsService: ThoughtsService) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe((param: any) => {
      this.thoughtsService.save(param.text).subscribe();
    });
  }

}
