import { ParamsEnum } from './../models/params.enum';
import { IListFiles } from "./../models/contract/list-files";
import { FilesModule } from "./../files.module";
import { TestBed, inject, async } from "@angular/core/testing";

import { ListFilesService } from "./list-files.service";
import { ListFilesResource } from "./resources/listfiles.resopurce";
import { HttpClient, HttpClientModule } from "@angular/common/http";
import { CoreModule } from "../../../core/core.module";
import { TranslationModule, LocalizationModule } from "angular-l10n";
import { l10nConfig } from "../../../shared/base/conf/l10n.config";
import { FileNw } from "../models/file";
import { Pagination } from "../models/pagination";
import { UtilsService } from "../../../shared/services/utils.service";
import { ListFilesFilter } from "../models/list-files-filter";
import { DownFileResource } from "./resources/downfile.resource";
import { saveAs } from "file-saver/FileSaver";
import { DownFilesResource } from "./resources/downfiles.resource";
import { RemoveFilesResource } from "./resources/removefiles.resource";
import { RemoveFileResource } from "./resources/removefile.resource";
import { SortType } from '../models/sort-type.enum';

xdescribe("ListFilesService", () => {
  var timeOut;
  beforeEach(() => {
    // timeOut = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    // jasmine.DEFAULT_TIMEOUT_INTERVAL = 50000;

    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [
        ListFilesService,
        ListFilesResource,
        DownFileResource,
        DownFilesResource,
        RemoveFilesResource,
        RemoveFileResource,
        UtilsService
      ]
    });
  });

  it(
    "should be created",
    inject(
      [ListFilesService, ListFilesResource],
      (service: ListFilesService) => {
        expect(service).toBeTruthy();
      }
    )
  );

  it(
    "download file response",
    async(
      inject([ListFilesService], (service: ListFilesService) => {
        let file1 = new FileNw();
        file1.id = 11;

        service.downloadFile(file1).subscribe(dat => {
          expect(dat).toBeDefined();
          expect(dat.size).toBeGreaterThan(0);
          console.log("size blob = " + dat.size);
          //save or open
          //saveAs(dat, 'blobsav');
          //var url= window.URL.createObjectURL(dat);
          //window.open(url);
        });
      })
    )
  );

  it(
    "download files params",
    async(
      inject([ListFilesService], (service: ListFilesService) => {
        let file1 = new FileNw();
        let file2 = new FileNw();
        let file3 = new FileNw();

        file1.id = 11;
        file2.id = 22;
        file3.id = 33;

        let files: FileNw[] = [];
        files.push(file1);
        files.push(file2);
        files.push(file3);

        let params= service.convertFilesToParams(files);

        expect(params.has(ParamsEnum.ID)).toBeTruthy();
        expect(params.getAll(ParamsEnum.ID).length).toEqual(3);

        service.downloadFiles(files).subscribe(dat => {
          expect(dat).toBeDefined();
          expect(dat.size).toBeGreaterThan(0);
          console.log("size blob = " + dat.size);
        });
      })
    )
  );

  it(
    "should be error ***",
    async(
      inject([ListFilesService], service => {
        service.listFiles(null).subscribe(data => {
          expect(data.content.length).toBe(5);
        });
      })
    )
  );

  it(
    "get list files with pagination",
    async(
      inject([ListFilesService], (service: ListFilesService) => {
        const filter = new ListFilesFilter();
        service.listFilesData(filter).subscribe(data => {
          expect(data.listData.length).toBe(5);
          expect(data.pagination.numberOfElements).toBeDefined();
        });
      })
    )
  );

  it(
    "filters setting",
    async(
      inject([ListFilesService], (service: ListFilesService) => {
        const filter = new ListFilesFilter();
        filter.id = 88;
        filter.sort = [{name:'sortfield', type:SortType.ASC}];
        filter.minUploadDate = new Date();

        const params = service.convertFilterToParams(filter);
        expect(params.get(ParamsEnum.ID)).toBeTruthy();
        expect(params.get(ParamsEnum.SORT)).toBeTruthy();
        expect(params.get(ParamsEnum.MIN_UPLOAD_DATE)).toBeTruthy();
        expect(params.get(ParamsEnum.PAGE)).toBeNull();

        console.log("minUploadDate= " + params.get(ParamsEnum.MIN_UPLOAD_DATE));
        service.listFilesData(filter).subscribe(data => {
          expect(data.pagination.numberOfElements).toBeDefined();
        });
      })
    )
  );

  it(
    "copy object File",
    async(
      inject([ListFilesService], (service: ListFilesService) => {
        service.listFiles(null).subscribe(dat => {
          console.log('RARARRAR');
          console.log(dat);
          expect(dat.content.length).toBe(5);
          let file = new FileNw(dat.content[0]);
          expect(file.id).toBeDefined();
          let pagination = new Pagination(dat);
          expect(pagination.totalElements).toBeDefined();
        });
      })
    )
  );

  it(
    "remove file",
    async(
      inject([ListFilesService], (service: ListFilesService) => {
        let file1 = new FileNw();
        file1.id = 11;

        service.removeFile(file1).subscribe(dat => {
          expect(dat).toBeDefined();
        });
      })
    )
  );

  it(
    "remove files",
    async(
      inject([ListFilesService], (service: ListFilesService) => {
        let file1 = new FileNw();
        file1.id = 11;

        service.downloadFile(file1).subscribe(dat => {
          expect(dat).toBeDefined();
          console.log("size blob = " + dat.size);
        });
      })
    )
  );


  afterEach(function() {
    // jasmine.DEFAULT_TIMEOUT_INTERVAL = timeOut;
  });
});
