import { DecimalsFormatterPipe } from './decimals-formatter.pipe';

describe('DecimalsFormatterPipe', () => {
  it('create an instance', () => {
    const pipe = new DecimalsFormatterPipe();
    expect(pipe).toBeTruthy();
  });

  it('transform', () => {
    const pipe = new DecimalsFormatterPipe();
    expect(pipe.transform(1000.1, 2)).toBe('1,000.10');
  });

  it('parse', () => {
    const pipe = new DecimalsFormatterPipe();
    expect(pipe.parse('1,000.10', 2)).toBe('1000.10');
  });

});
