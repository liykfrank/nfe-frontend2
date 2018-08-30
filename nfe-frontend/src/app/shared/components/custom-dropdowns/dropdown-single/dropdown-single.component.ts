import { Component, Input, ElementRef, EventEmitter, Output } from '@angular/core';
import { DropdownAbstractComponent } from '../dropdown-abstract/dropdown-abstract.component';
import { FormGroup } from '@angular/forms';

@Component({
    selector: 'bspl-dropdown-single',
    templateUrl: './dropdown-single.component.html',
    styleUrls: ['./dropdown-single.component.scss']
})
export class DropdownSingleComponent extends DropdownAbstractComponent {

    _optionSelected: any;
    @Input('optionSelected')
    set optionSelected(value: any) {
        if (value) {
            this._optionSelected = null;
            this._options.map(elem => {
                const aux = elem[this.optionValue] || elem;
                if (aux === value) {
                    this._optionSelected = elem;
                }
            });
        } else {
            this._optionSelected = null;
        }
    }
    @Input('groupForm') groupForm: FormGroup;
    @Input('controlName') controlName: string;

    @Output() selectedItem: EventEmitter<any> = new EventEmitter();

    constructor(
        eRef: ElementRef
    ) {
        super(eRef);
    }

    selectOption(option) {
        this._optionSelected = option;
        const value = option[this.optionValue] || option;
        if (this.checkFormGroup()) {
            this.groupForm.get(this.controlName).setValue(value);
        } else {
            this.selectedItem.emit(value);
        }
        this.open = false;
    }

    checkFormGroup() {
        return this.groupForm != undefined && this.controlName != undefined;
    }

    checkClassSelected(opt) {
        return this._optionSelected == opt ? 'selected' : '';
    }
}
