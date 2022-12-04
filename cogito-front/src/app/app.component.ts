import { Component, OnInit } from '@angular/core';
import { CogitoStoreService } from './internal-service/store/cogito-store.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  darkModeMenu: string = 'Dark theme';

  constructor(private cogitoStoreService: CogitoStoreService) {}

  ngOnInit(): void {
    document.body.classList.add(localStorage.getItem('theme')!);
  }

  changeTheme(): void {
    document.body.classList.toggle('dark-mode');
    if (document.body.classList.contains('dark-mode')) {
      localStorage.setItem('theme', 'dark-mode');
      this.darkModeMenu = 'White Theme';
    } else {
      localStorage.setItem('theme', '');
      this.darkModeMenu = 'Dark theme';
    }
  }

  setSearch(val: string) {
    this.cogitoStoreService.setFilter(val);
  }
}
