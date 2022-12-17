import { ComponentFixture, TestBed } from "@angular/core/testing";

import { ModalEntryComponent } from "./modal-entry.component";

describe('ModalEntryComponent', () => {
  let component: ModalEntryComponent;
  let fixture: ComponentFixture<ModalEntryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalEntryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
