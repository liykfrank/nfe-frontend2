import { TestBed, inject } from '@angular/core/testing';

import { FilesService } from './files.service';
import { AttachedFile } from '../models/attached-file.model';
import { Acdm } from '../models/acdm.model';

describe('FilesService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FilesService]
    });
  });

  it('should be created', inject([FilesService], (service: FilesService) => {
    expect(service).toBeTruthy();
  }));

  it('getFiles', inject([FilesService], (service: FilesService) => {
    expect(service.getFiles()).toBeTruthy();
  }));

  it('setFiles', inject([FilesService], (service: FilesService) => {
    let elem;
    service.getFiles().subscribe(data => elem = data);
    const msg : AttachedFile = new AttachedFile();
    msg.id = 1;
    service.setFiles(msg);
    expect(elem.length).toBeGreaterThan(0);

    let acdm: Acdm = new Acdm();
    service.copyToAdcm(acdm);
    expect(msg.id == 1).toBe(true);
  }));

  it('getFilesToUpload', inject([FilesService], (service: FilesService) => {
    expect(service.getFilesToUpload()).toBeTruthy();
  }));

  it('setFileToUpload', inject([FilesService], (service: FilesService) => {
    let elem;
    service.getFilesToUpload().subscribe(data => elem = data);
    const msg : File = new File([], 'test');
    service.setFileToUpload(msg);
    expect(elem).toBeTruthy();
  }));

});
