
const fs = require("fs");
// Protractor configuration file, see link for more information
// https://github.com/angular/protractor/blob/master/lib/config.ts

const { SpecReporter } = require('jasmine-spec-reporter');

console.log('path '+process.cwd());
mkdir(process.cwd()+'/e2e/resources/downloads');
exports.config = {
  allScriptsTimeout: 25000,
  specs: [
    './e2e/**/*.e2e-spec.ts'
  ],
  useAllAngular2AppRoots: true,
  capabilities: {
    'browserName': 'chrome',
    'chromeOptions': {
      args: ['--headless', '--disable-gpu', '--test-type=browser --headless'],
      // Set download path and avoid prompting for download even though
      // this is already the default on Chrome but for completeness
      prefs: {
      'download': {
      'prompt_for_download': false,
      'directory_upgrade': true,
      'default_directory': process.cwd()+'/e2e/resources/downloads'
      }
    }
  }
  },
  directConnect: true,
  baseUrl: 'http://localhost:4200/',
  framework: 'jasmine',
  jasmineNodeOpts: {
    showColors: true,
    defaultTimeoutInterval: 30000,
    print: function() {}
  },
  onComplete() {
    var globals = require('protractor');
    var browser = globals.browser;
    browser.ignoreSynchronization = true;
    browser.close().then(()=>console.log('Browser closed'));
    console.log('Complete');
  },
  onPrepare() {
    require('ts-node').register({
      project: 'e2e/tsconfig.e2e.json'
    });
    jasmine.getEnv().addReporter(new SpecReporter({ spec: { displayStacktrace: true } }));
  }
};
function mkdir(dir) {
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir);
  }
}
