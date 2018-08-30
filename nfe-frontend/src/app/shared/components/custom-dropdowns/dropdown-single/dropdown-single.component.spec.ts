import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ElementRef } from '@angular/core';
import { DropdownSingleComponent } from './dropdown-single.component';
import { FormGroup, FormControl } from '@angular/forms';

describe('DropdownSingleComponent', () => {
    let comp: DropdownSingleComponent;
    let fixture: ComponentFixture<DropdownSingleComponent>;

    const aux_country = [
        { 'isoCountryCode': 'AA', 'name': 'Country AA' },
        { 'isoCountryCode': 'BB', 'name': 'Country BB' },
        { 'isoCountryCode': 'CC', 'name': 'Country CC' }
    ];

    const simple_array = [
        'AA',
        'BB',
        'CC'
    ];

    beforeEach(() => {
        const elementRefStub = {};
        TestBed.configureTestingModule({
            declarations: [ DropdownSingleComponent ],
            schemas: [ NO_ERRORS_SCHEMA ],
            providers: [
                { provide: ElementRef, useValue: elementRefStub }
            ]
        });
        fixture = TestBed.createComponent(DropdownSingleComponent);
        comp = fixture.componentInstance;
    });

    it('COMPONENT can load instance', () => {
        expect(comp).toBeTruthy();
    });

    it('ATTRIBUTES [label] defaults to: Undefined', () => {
        expect(comp.label).toBeUndefined();
    });

    it('ATTRIBUTES [wrapText] defaults to: Undefined', () => {
        expect(comp.wrapText).toBeUndefined();
    });

    it('ATTRIBUTES [placeholderSelect] defaults to: Choose', () => {
        expect(comp.placeholderSelect).toEqual('Choose');
    });

    it('ATTRIBUTES [placeholderSearchBar] defaults to: Search', () => {
        expect(comp.placeholderSearchBar).toEqual('Search');
    });

    it('ATTRIBUTES options SET', () => {
        comp.options = null;
        expect(comp._options).toBeUndefined();

        comp.options = aux_country;
        expect(comp._options).toEqual(aux_country);
        expect(comp.options_filtered).toEqual(comp._options);

        comp.options = simple_array;
        expect(comp._options).toEqual(simple_array);
        expect(comp.options_filtered).toEqual(comp._options);
    });

    it('ATTRIBUTES _options defaults to: Undefined', () => {
        expect(comp._options).toBeUndefined();
    });

    it('ATTRIBUTES [optionLabel] defaults to: label', () => {
        expect(comp.optionLabel).toEqual('label');
    });

    it('ATTRIBUTES [optionValue] defaults to: value', () => {
        expect(comp.optionValue).toEqual('value');
    });

    it('ATTRIBUTES [searchBar] defaults to: false', () => {
        expect(comp.searchBar).toEqual(false);
    });

    it('ATTRIBUTES open defaults to: false', () => {
        expect(comp.open).toEqual(false);
    });

    it('ATTRIBUTES filter_value defaults to: Undefined', () => {
        expect(comp.filter_value).toBeUndefined();
    });

    it('ATTRIBUTES options_filtered defaults to: Undefined', () => {
        expect(comp.options_filtered).toBeUndefined();
    });

    it('ATTRIBUTES scroll_height defaults to: "0px"', () => {
        expect(comp.scroll_height).toEqual('0px');
    });

    it('ATTRIBUTES optionSelected SET', () => {
        comp._options = aux_country;
        comp.optionSelected = null;
        expect(comp._optionSelected).toBeNull();

        comp.optionSelected = 'invalid value';
        expect(comp._optionSelected).toBeNull();

        comp.optionSelected = aux_country[0];
        expect(comp._optionSelected).toEqual(aux_country[0]);

        comp.optionValue = 'isoCountryCode';

        comp.optionSelected = 'invalid value';
        expect(comp._optionSelected).toBeNull();

        comp.optionSelected = aux_country[0].isoCountryCode;
        expect(comp._optionSelected).toEqual(aux_country[0]);
    });

    it('ATTRIBUTES _optionSelected defaults to: Undefined', () => {
        expect(comp._optionSelected).toBeUndefined();
    });

    it('FUNCTION selectOption() ', () => {
        let val;
        comp.selectedItem.subscribe(data => val = data);

        comp.selectOption(aux_country[0]);

        expect(comp._optionSelected).toEqual(aux_country[0]);
        expect(comp.open).toEqual(false);
        expect(val).toEqual(aux_country[0]);

        comp.optionValue = 'isoCountryCode';
        comp.selectOption(aux_country[1]);

        expect(comp._optionSelected).toEqual(aux_country[1]);
        expect(comp.open).toEqual(false);
        expect(val).toEqual(aux_country[1].isoCountryCode);

        comp.groupForm = new FormGroup({
            example: new FormControl('')
        });
        comp.controlName = 'example';
        comp.selectOption(aux_country[2]);

        expect(comp.groupForm.get(comp.controlName).value).toEqual(aux_country[2].isoCountryCode);
    });

    it('FUNCTION checkFormGroup() ', () => {
        expect(comp.checkFormGroup()).toBeFalsy();
        comp.groupForm = new FormGroup({});
        expect(comp.checkFormGroup()).toBeFalsy();
        comp.controlName = 'something';
        expect(comp.checkFormGroup()).toBeTruthy();
    });

    it('FUNCTION checkClassSelected() ', () => {
        comp.selectOption(aux_country[0]);
        expect(comp.checkClassSelected(aux_country[0])).toEqual('selected');
        expect(comp.checkClassSelected(aux_country[1])).toEqual('');
    });

    it('FUNCTION toggleDrop() ', () => {
        const aux = comp.open;
        comp.toggleDrop();
        expect(comp.open).toEqual(!aux);
    });

    it('FUNCTION checkOpen() ', () => {
        const aux = comp.open;
        expect(comp.checkOpen()).toEqual(aux);
    });

    it('FUNCTION checkSearchBar() ', () => {
        const aux = comp.searchBar;
        expect(comp.checkSearchBar()).toEqual(aux);
    });

    it('FUNCTION checkClassOpen() ', () => {
        expect(comp.checkClassOpen()).toEqual('');
        comp.toggleDrop();
        expect(comp.checkClassOpen()).toEqual('open');
    });

    it('FUNCTION simpleSearch() ', () => {
        comp._options = aux_country;

        comp.simpleSearch();
        expect(comp.options_filtered).toEqual(aux_country);

        comp.searchBar = true;
        comp.filter_value = '';
        comp.simpleSearch();
        expect(comp.options_filtered).toEqual(aux_country);

        comp.filter_value = 'AA';
        comp.simpleSearch();
        expect(comp.options_filtered).toEqual([]);

        comp.optionLabel = 'name';
        comp.simpleSearch();
        expect(comp.options_filtered).toEqual([aux_country[0]]);
    });

    it('FUNCTION checkHeightScroll() ', () => {
        comp.options_filtered = [];
        comp.checkHeightScroll();
        expect(comp.scroll_height).toEqual('0px');

        for (let i = 1; i < 10; i++) {
            comp.options_filtered.push(aux_country[0]);
            comp.checkHeightScroll();
            if (i < 5) {
                expect(comp.scroll_height).toEqual((i * 37) + 'px');
            } else {
                expect(comp.scroll_height).toEqual('185px');
            }
        }
    });

});
