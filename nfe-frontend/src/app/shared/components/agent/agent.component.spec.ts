import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { SimpleChanges } from '@angular/core';
import { AgentService } from './services/agent.service';
import { AgentComponent } from './agent.component';

describe('AgentComponent', () => {
    let comp: AgentComponent;
    let fixture: ComponentFixture<AgentComponent>;

    beforeEach(() => {
        const simpleChangesStub = {
            agentCode: {}
        };
        const agentServiceStub = {
            validateAgent: () => ({
                subscribe: () => ({})
            })
        };
        TestBed.configureTestingModule({
            declarations: [ AgentComponent ],
            schemas: [ NO_ERRORS_SCHEMA ],
            providers: [
                { provide: AgentService, useValue: agentServiceStub }
            ]
        });
        fixture = TestBed.createComponent(AgentComponent);
        comp = fixture.componentInstance;
    });

    it('can load instance', () => {
        expect(comp).toBeTruthy();
    });

    it('showMoreDetails defaults to: true', () => {
        expect(comp.showMoreDetails).toEqual(true);
    });

    it('disabledMoreDetails defaults to: true', () => {
        expect(comp.disabledMoreDetails).toEqual(true);
    });

    it('display defaults to: false', () => {
        expect(comp.display).toEqual(false);
    });

});
