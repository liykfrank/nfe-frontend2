import { AbsolutePipe } from './absolute.pipe';

import { TestBed } from '@angular/core/testing';
import { BrowserModule } from '@angular/platform-browser';



describe('AbsolutePipe', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [BrowserModule] });
  });

  it('should create an instance', () => {
    const directive = new AbsolutePipe();
    expect(directive).toBeTruthy();
  });

  it('transform', () => {
    const pipe = new AbsolutePipe();
    const number = 2;
    expect(pipe.transform(number)).toBe(Math.abs(number));
  });
});
