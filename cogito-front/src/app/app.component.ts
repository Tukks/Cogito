import {Component} from '@angular/core';
import {NzConfigService} from "ng-zorro-antd/core/config";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private nzConfigService: NzConfigService) {
  }

  onChangeConfig() {
    this.nzConfigService.set('theme', {bodyBackground: '#1890ff'})
  }
}
