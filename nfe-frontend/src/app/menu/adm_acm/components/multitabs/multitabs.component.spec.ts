import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MultitabsComponent } from './multitabs.component';

xdescribe('MultitabsComponent', () => {
  let component: MultitabsComponent;
  let fixture: ComponentFixture<MultitabsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MultitabsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultitabsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
