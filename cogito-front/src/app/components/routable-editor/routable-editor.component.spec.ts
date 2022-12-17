import { ComponentFixture, TestBed } from "@angular/core/testing";

import { RoutableEditorComponent } from "./routable-editor.component";

describe('RoutableEditorComponent', () => {
  let component: RoutableEditorComponent;
  let fixture: ComponentFixture<RoutableEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RoutableEditorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoutableEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
