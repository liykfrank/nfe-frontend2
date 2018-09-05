import { UploadFiles } from './upload-files.po';

describe('Upload file', () => {
  const page = new UploadFiles();

  const UPLOAD_PATH = process.cwd() + '/e2e/resources/upload/';

  const FILENAME1 = 'A0aaAAA0_20180521.txt';
  const FILENAME2 = 'B0aaAAA0_20180521.txt';

  beforeEach(() => {
    page.navigateTo();
  });

  it('TEST1: upload 2 files', () => {
    const input = page.getInputToSelectElement();
    const cancel = page.getCancel();
    const update = page.getUpdate();

    expect(cancel.isEnabled()).toBe(false);
    expect(update.isEnabled()).toBe(false);

    input.sendKeys(UPLOAD_PATH + FILENAME1);
    input.sendKeys(UPLOAD_PATH + FILENAME2);

    expect(page.getDivsFiles().count()).toBeGreaterThan(0);
    expect(cancel.isEnabled()).toBe(true);
    expect(update.isEnabled()).toBe(true);

    update.click();
    expect(page.getDivOKs().count()).toBeGreaterThan(0);
  });
});
