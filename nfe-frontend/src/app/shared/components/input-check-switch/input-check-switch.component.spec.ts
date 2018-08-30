import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { InputCheckSwitchComponent } from './input-check-switch.component';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from 'primeng/primeng';
import { l10nConfig } from '../../base/conf/l10n.config';
import { TranslationModule } from 'angular-l10n';
import { HttpClientModule } from '@angular/common/http';

describe('InputCheckSwitchComponent', () => {
    let comp: InputCheckSwitchComponent;
    let fixture: ComponentFixture<InputCheckSwitchComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                ReactiveFormsModule,
                SharedModule,
                TranslationModule.forRoot(l10nConfig),
                HttpClientModule
            ],
            declarations: [ InputCheckSwitchComponent ],
            schemas: [ NO_ERRORS_SCHEMA ]
        });
        fixture = TestBed.createComponent(InputCheckSwitchComponent);
        comp = fixture.componentInstance;
    }));

    it('create compontent', () => {
        expect(comp).toBeTruthy();
    });

    it('isCheckBox defaults to: false', () => {
        expect(comp.isCheckBox).toEqual(false);
    });

    it('getLabelCheck() return string void', () => {
        expect(comp.getLabelCheck()).toBe('');
    });

    it('getLabelCheck() return string', () => {
        comp.labelCheck = 'testing';
        expect(comp.getLabelCheck()).toBe('testing');
    });
});
