import { SafeUrlPipe } from "./safe-url.pipe";
import { inject, TestBed } from "@angular/core/testing";
import { BrowserModule, DomSanitizer } from "@angular/platform-browser";

describe("SafeUrlPipe", () => {
  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [BrowserModule] });
  });

  it(
    "create an instance",
    inject([DomSanitizer], (domSanitizer: DomSanitizer) => {
      const pipe = new SafeUrlPipe(domSanitizer);
      expect(pipe).toBeTruthy();
    })
  );
});
