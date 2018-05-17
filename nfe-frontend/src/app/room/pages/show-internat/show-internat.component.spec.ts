import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowInternatComponent } from './show-internat.component';
import { SharedModule } from '../../../shared/shared.module';

describe('ShowInternatComponent', () => {
  let component: ShowInternatComponent;
  let fixture: ComponentFixture<ShowInternatComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[SharedModule],
      declarations: [ ShowInternatComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowInternatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
