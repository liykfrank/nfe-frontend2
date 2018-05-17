import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowNewGridComponent } from './show-new-grid.component';
import { SharedModule } from '../../../shared/shared.module';

describe('ShowNewGridComponent', () => {
  let component: ShowNewGridComponent;
  let fixture: ComponentFixture<ShowNewGridComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[SharedModule],
      declarations: [ ShowNewGridComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowNewGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
