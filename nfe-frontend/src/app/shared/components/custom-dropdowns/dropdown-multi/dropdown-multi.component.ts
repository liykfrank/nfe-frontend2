import { Component, Input, ElementRef, Output, EventEmitter } from '@angular/core';
import { DropdownAbstractComponent } from '../dropdown-abstract/dropdown-abstract.component';

@Component({
    selector: 'bspl-dropdown-multi',
    templateUrl: './dropdown-multi.component.html',
    styleUrls: ['./dropdown-multi.component.scss']
})
export class DropdownMultiComponent extends DropdownAbstractComponent {

    _optionSelected: any[] = [];
    @Input('selectAllBar') selectAllBar = false;
    @Input('buttonsBar') buttonsBar = false;
    @Input('optionSelected')
    set optionSelected(value: any[]) {
        if (value) {
            this._optionSelected = value;
        } else {
            this._optionSelected = [];
        }
    }

    @Output() selectedItem: EventEmitter<any[]> = new EventEmitter();
    @Output() cancel:       EventEmitter<null> = new EventEmitter();

    constructor(
        eRef: ElementRef
    ) {
        super(eRef);
    }

    selectOption(option) {
        const index = this._optionSelected.indexOf(option);
        if (index == -1) {
            this._optionSelected.push(option);
        } else {
            this._optionSelected.splice(index, 1);
        }

        this.buildResponse();
    }

    buildResponse(fromApply?: boolean) {
        const aux = [];
        this._optionSelected.map(elem => {
            const value_to_push = elem[this.optionValue] || elem;
            aux.push(value_to_push);
        });

        if (!this.buttonsBar || fromApply) {
            this.selectedItem.emit(aux);
        }
    }

    selectAll() {
        if (!this.checkAllSelected()) {
            this._optionSelected = this._options.slice();
        } else {
            this._optionSelected = [];
        }

        this.buildResponse();
    }

    checkAllSelected() {
        if (this._optionSelected && this._options) {
            return this._optionSelected.length == this._options.length;
        }
        return false;
    }

    checkClassAllSelected() {
        return this.checkAllSelected() ? 'selected' : '';
    }

    checkSelectAll() {
        return this.selectAllBar;
    }

    checkButtonsBar() {
        return this.buttonsBar;
    }

    checkClassSelected(opt) {
        return this._optionSelected.indexOf(opt) != -1 ? 'selected' : '';
    }

    clickApply() {
        this.open = false;
        this.buildResponse(true);
    }

    clickCancel() {
        this.open = false;
        this.cancel.emit();
    }
}
