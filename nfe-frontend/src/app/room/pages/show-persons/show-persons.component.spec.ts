import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowPersonsComponent } from './show-persons.component';
import { SharedModule } from '../../../shared/shared.module';

xdescribe('ShowPersonsComponent', () => {
  let component: ShowPersonsComponent;
  let fixture: ComponentFixture<ShowPersonsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[SharedModule],
      declarations: [ ShowPersonsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowPersonsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
