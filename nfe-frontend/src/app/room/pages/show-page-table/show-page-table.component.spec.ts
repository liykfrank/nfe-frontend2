import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowPageTableComponent } from './show-page-table.component';

xdescribe('ShowPageTableComponent', () => {
  let component: ShowPageTableComponent;
  let fixture: ComponentFixture<ShowPageTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowPageTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowPageTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
