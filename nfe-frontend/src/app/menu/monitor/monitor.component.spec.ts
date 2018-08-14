import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { MonitorComponent } from './monitor.component';
import { SafeUrlPipe } from '../../shared/pipes/safe-url.pipe';


describe('MonitorComponent', () => {
  let component: MonitorComponent;
  let fixture: ComponentFixture<MonitorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MonitorComponent, SafeUrlPipe]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MonitorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
