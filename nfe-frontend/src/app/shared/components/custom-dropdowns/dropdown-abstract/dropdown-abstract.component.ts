import { Input, HostListener, ElementRef } from '@angular/core';

export class DropdownAbstractComponent {

    _options: any[];
    filter_value: string;
    options_filtered: any[];

    open = false;
    scroll_height = '0px';

    @Input('label') label: string;
    @Input('wrapText') wrapText: boolean;
    @Input('placeholderSelect') placeholderSelect = 'Choose';
    @Input('placeholderSearchBar') placeholderSearchBar = 'Search';
    @Input('options')
    set options(value: any[]) {
        if (value) {
            this._options = value;
            this.options_filtered = this._options;
            this.checkHeightScroll();
        }
    }
    @Input('optionLabel') optionLabel = 'label';
    @Input('optionValue') optionValue = 'value';
    @Input('searchBar') searchBar = false;

    @HostListener('document:click', ['$event'])
    clickout(event) {
        if (!this.eRef.nativeElement.contains(event.target)) {
            this.open = false;
        }
    }

    constructor(
        private eRef: ElementRef
    ) { }

    toggleDrop() {
        this.open = !this.open;
    }

    checkOpen() {
        return this.open;
    }

    checkSearchBar() {
        return this.searchBar;
    }

    checkClassOpen() {
        return this.open ? 'open' : '';
    }

    simpleSearch() {
        if (this.searchBar && this.filter_value != '') {
            this.options_filtered = this._options.filter(elem => {
                const aux = elem[this.optionLabel] || elem;
                return String(aux).toUpperCase()
                    .includes(this.filter_value.toUpperCase());
            });
        } else {
            this.options_filtered = this._options;
        }

        this.checkHeightScroll();
    }

    checkHeightScroll() {
        if (this.options_filtered.length <= 5) {
            this.scroll_height = this.options_filtered.length * 37 + 'px';
        } else {
            this.scroll_height = '185px';
        }
    }
}
