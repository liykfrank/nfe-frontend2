// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
  ionic: false,
  basePath: '',
  production: false,
  mock: false,
  room: {
    api: {
      persons: 'testPersons'
    },
     basePath: ''
  },
  files: {
    api: {
      listFiles: 'v1/files',
      downloadFile: 'v1/files',
      downloadFiles: 'v1/files/zip',
      removeFile: 'v1/files',
      removeFiles: 'v1/files',
      apiConfiguration: 'v1/configurations',
      apiUpload: 'v1/files'
    },
    basePath: 'http://www.nfedev.accelya.com:8080/'
  },
  accounts: {
    api: {
      accounts: 'v1/accounts'
    },
    basePath: 'http://www.nfedev.accelya.com:8081/'
  },
  monitorUrl: 'http://yade.nfedev.accelya.com'
};