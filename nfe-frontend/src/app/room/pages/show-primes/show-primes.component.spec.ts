import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowPrimesComponent } from './show-primes.component';

xdescribe('ShowPrimesComponent', () => {
  let component: ShowPrimesComponent;
  let fixture: ComponentFixture<ShowPrimesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowPrimesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowPrimesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
