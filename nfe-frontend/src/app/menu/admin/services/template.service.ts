import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpServiceAbstract } from '../../../shared/base/http-service-abstract';
import { TemplateModel } from '../components/user/models/template.model';
import { environment } from '../../../../environments/environment';

@Injectable()
export class TemplateService extends HttpServiceAbstract<TemplateModel[], any> {

    private templates: TemplateModel[] = [];
    templatesCloned: TemplateModel[] = [];

    constructor(private http: HttpClient) {
        super(http, environment.api.user.template);
        this.getAllTemplates();
    }

    getAllTemplates() {
        this.get().subscribe((response) => {
            this.templates = response;
            this.templatesCloned = this.templates.slice();
        });
    }

    removeTemplate(template) {
        const position = this.templatesCloned.indexOf(template);
        this.templatesCloned.splice(position, 1);
    }

    restoreTemplate(template) {
        const position = this.templates.indexOf(template);
        this.templatesCloned.splice(position, 0, template);
    }

}
