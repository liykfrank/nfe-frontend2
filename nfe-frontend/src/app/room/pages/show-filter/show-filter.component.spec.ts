import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowFilterComponent } from './show-filter.component';
import { SharedModule } from '../../../shared/shared.module';

describe('ShowFilterComponent', () => {
  let component: ShowFilterComponent;
  let fixture: ComponentFixture<ShowFilterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[SharedModule],
      declarations: [ ShowFilterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
