import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { environment } from '../../../../environments/environment';
import { HttpServiceAbstract } from '../../../shared/base/http-service-abstract';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class FilesService extends HttpServiceAbstract<any, Object> {
    constructor(private http: HttpClient) {
        super(http, environment.basePath + environment.api.files.apiUpload);
    }

    uploadFiles(files: File[]): Observable<any> {
        const formData: FormData = new FormData();

        for (const file of files) {
            formData.append('file', file, file.name);
        }

        return this.postSingle<any[]>(formData);
    }
}
