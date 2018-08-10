import { HttpClientModule } from '@angular/common/http';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslationModule } from 'angular-l10n';
import { FileUploadModule, ScrollPanelModule } from 'primeng/primeng';

import { l10nConfig } from '../../../../base/conf/l10n.config';
import { FilesComponent } from './files.component';

describe('FilesComponent', () => {
  let component: FilesComponent;
  let fixture: ComponentFixture<FilesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        ScrollPanelModule,
        FileUploadModule,
        TranslationModule.forRoot(l10nConfig),
        HttpClientModule
      ],
      declarations: [FilesComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('onUploadHandler', () => {
    const file1: any = { name: 'abc1.xyz', size: 1025 };
    const file2: any = { name: 'abc2.xyz', size: 1025 };
    const file3: any = { name: 'abc3.xyz', size: 1025 };


    let list = [];
    component.pushFiles.subscribe((data) => list = data);

    const aux = {files: []};
    aux.files.push([]);
    aux.files[0].push(file1);
    aux.files[0].push(file2);
    aux.files[0].push(file3);

    component.onUploadHandler(aux);
    expect(list.length).toBe(3);
  });

});
