
import { browser, by, element, ElementFinder, promise } from 'protractor';

export class UtilsElement {

  /**
   * Find element on web Finding By
   * @param id Elem id
   */
  findById(id: string): ElementFinder {
    console.log('Finding element by id: ' + id);
    return element(by.id(id));
  }

  /**
   * Find element on web Finding By
   * @param name Elem name
   */
  findByName(name: string): ElementFinder {
    console.log('Finding element by name: ' + name);
    return element(by.name(name));
  }

  /**
   * Find element on web Finding By
   * @param css Elem CSS
   */
  findByCSS(css: string): ElementFinder {
    console.log('Finding element by css: ' + css);
    return element(by.css(css));
  }

  /**
   * Check if element exist on web finding by
   * @param id Elem id
   */
  isPresentFindById(id: string) {
    return this.isPresentFindByElem(this.findById(id));
  }

  /**
   * Check if element exist on web finding by
   * @param name Elem name
   */
  isPresentFindByName(name: string) {
    return this.isPresentFindByElem(this.findByName(name));
  }

  /**
   * Check if element exist on web finding by
   * @param css Elem css
   */
  isPresentFindByCSS(css: string) {
    return this.isPresentFindByElem(this.findByCSS(css));
  }

  /**
   * Check if element exist on web finding by
   * @param elem Elem
   */
  isPresentFindByElem(elem: ElementFinder) {
    console.log('Checking presence of element');
    return expect(elem.isPresent()).toBe(true);
  }

  /**
   * Check if element doesn't exist on web finding by
   * @param id Elem
   */
  isNotPresentFindById(id: string) {
    return this.isNotPresentFindByElem(this.findById(id));
  }

  /**
   * Check if element doesn't exist on web finding by
   * @param id Elem
   */
  isNotPresentFindByName(name: string) {
    return this.isNotPresentFindByElem(this.findByName(name));
  }

  /**
   * Check if element doesn't exist on web finding by
   * @param css Elem
   */
  isNotPresentFindByCSS(css: string) {
    return this.isNotPresentFindByElem(this.findByCSS(css));
  }

  /**
   * Check if element doesn't exist on web finding by
   * @param elem Elem
   */
  isNotPresentFindByElem(elem: ElementFinder) {
    console.log('Checking NO presence of element');
    return expect(elem.isPresent()).toBe(false);
  }

  /**
   * Check if element doesn't exist on web finding by
   * @param id Elem
   */
  beTruthyFindById(id: string) {
    return this.isNotPresentFindByElem(this.findById(id));
  }

  /**
   * Check if element doesn't exist on web finding by
   * @param id Elem
   */
  beTruthyFindByName(name: string) {
    return this.isNotPresentFindByElem(this.findByName(name));
  }

  /**
   * Check if element doesn't exist on web finding by
   * @param css Elem
   */
  beTruthyFindByCSS(css: string) {
    return this.beTruthyFindByElem(this.findByCSS(css));
  }

  /**
   * Check if element doesn't exist on web finding by
   * @param elem Elem
   */
  beTruthyFindByElem(elem: ElementFinder) {
    console.log('Checking presence of element');
    return expect(elem).toBeTruthy();
  }

}
