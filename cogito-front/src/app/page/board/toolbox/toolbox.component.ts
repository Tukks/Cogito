import { Component, HostListener, OnInit } from "@angular/core";
import { WebSocketService } from "../../../http-service/web-socket.service";
import { CogitoStoreService } from "../../../internal-service/store/cogito-store.service";

@Component({
  selector: "app-toolbox",
  templateUrl: "./toolbox.component.html",
  styleUrls: ["./toolbox.component.less"]
})
export class ToolboxComponent implements OnInit {
  removeMode: boolean = false;

  filtersMenu: any[] = [];

  isVisible: boolean = false;
  filterToAdd: any = "";
  public websocketConnect: boolean = true;

  @HostListener("document:visibilitychange", ["$event"])
  visibilityChange() {
    if (document.visibilityState === "hidden") {
      localStorage.setItem("filtersList", JSON.stringify(this.filtersMenu));
    }
  }

  constructor(private webSocketService: WebSocketService,
              private cogitoStoreService: CogitoStoreService) {
  }

  ngOnInit(): void {
    this.cogitoStoreService.websocketStatus$.subscribe(value => this.websocketConnect = value);

    const filtersList = localStorage.getItem("filtersList");
    if (filtersList) {
      this.filtersMenu = JSON.parse(filtersList);
    }
  }

  activateRemoveMode() {
    this.removeMode = !this.removeMode;
  }

  changeTheme(): void {
    document.body.classList.toggle("dark-mode");
    if (document.body.classList.contains("dark-mode")) {
      localStorage.setItem("theme", "dark-mode");
    } else {
      localStorage.setItem("theme", "");
    }
  }

  showModal() {
    this.isVisible = true;
  }

  handleCancel() {
    this.isVisible = false;
  }

  handleAdd() {
    if (this.filterToAdd) {
      this.filtersMenu.push({ value: this.filterToAdd });
    }
    this.isVisible = false;

  }

  setSearch(val: string) {
    if (!this.removeMode) {
      this.cogitoStoreService.setFilter(val);
    } else {
      this.filtersMenu = this.filtersMenu.filter(filter => filter.value !== val);
    }
  }


}
