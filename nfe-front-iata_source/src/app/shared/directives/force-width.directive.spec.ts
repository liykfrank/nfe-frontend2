import { ForceWidthDirective } from './force-width.directive';
import { ElementRef, Renderer2, Component, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

@Component({
  template: `<input type="number" fbInputlistener value="12345">`
})
class TestInputComponent {
}

describe('ForceWidthDirective', () => {
  let component: TestInputComponent;
  let fixture: ComponentFixture<TestInputComponent>;
  let inputEl: DebugElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TestInputComponent, ForceWidthDirective]
    });

    fixture = TestBed.createComponent(TestInputComponent);
    component = fixture.componentInstance;
    inputEl = fixture.debugElement.query(By.css('input'));
 });

  it('should create an instance', () => {
    /*
    const directive = new ForceWidthDirective(null, component);
    expect(directive).toBeTruthy();
    */
  });
});
