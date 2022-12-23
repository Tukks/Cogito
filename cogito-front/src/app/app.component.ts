import { Component, HostListener, OnInit } from "@angular/core";
import { CogitoStoreService } from "./internal-service/store/cogito-store.service";
import { ThoughtsService } from "./http-service/thoughts.service";
import { Observable, retry, share, switchMap, timer } from "rxjs";
import { BreakpointObserver, Breakpoints, BreakpointState } from "@angular/cdk/layout";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"]
})
export class AppComponent implements OnInit {
  isExtraSmall: Observable<BreakpointState> = this.breakpointObserver.observe(
    Breakpoints.HandsetPortrait
  );
  showMenu = true;
  filtersMenu: any[] = [];
  // When user close tab
  isVisible: boolean = false;
  filterToAdd: any = '';

  removeMode: boolean = false;
  constructor(private cogitoStoreService: CogitoStoreService,
              private thoughtService: ThoughtsService,
              private breakpointObserver: BreakpointObserver) {
  }


  @HostListener('document:visibilitychange', ['$event'])
  visibilityChange() {
    if (document.visibilityState === 'hidden') {
      localStorage.setItem("filtersList", JSON.stringify(this.filtersMenu));
    }
  }
  ngOnInit(): void {
    this.isExtraSmall.subscribe(value => this.showMenu = !value.matches);
    // TODO websocket
    timer(0, 10000).pipe(
      switchMap(() => this.thoughtService.getAllthougts()),
      retry(),
      share()
    ).subscribe();
    document.body.classList.add(localStorage.getItem("theme")!);
    const filtersList = localStorage.getItem("filtersList");
    if (filtersList) {
      this.filtersMenu = JSON.parse(filtersList);
    }
  }

  changeTheme(): void {
    document.body.classList.toggle("dark-mode");
    if (document.body.classList.contains("dark-mode")) {
      localStorage.setItem("theme", "dark-mode");
    } else {
      localStorage.setItem("theme", "");
    }
  }

  setSearch(val: string) {
    console.log(this.removeMode)
    if (!this.removeMode) {
      this.cogitoStoreService.setFilter(val);
    } else {
      this.filtersMenu = this.filtersMenu.filter(filter => filter.value !== val);
    }
  }

  handleAdd() {
    if (this.filterToAdd) {
      this.filtersMenu.push({ value: this.filterToAdd });
    }
    this.isVisible = false;

  }

  showModal() {
    this.isVisible = true;
  }

  handleCancel() {
    this.isVisible = false;
  }

  activateRemoveMode() {
    this.removeMode = !this.removeMode;
  }
}
