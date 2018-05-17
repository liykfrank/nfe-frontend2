export const environment = {
  ionic: false,
  basePath: 'http://localhost:4200/',
  production: false,
  mock: true,
  room: {
    api: {
      persons: 'testPersons.json'
    },
    basePath: 'assets/mocks/'
  },
  files: {
    api: {
      listFiles: 'listfiles.json',
      downloadFile: 'down/foto.png',
      downloadFiles: 'down/Downloads.zip',
      removeFile: 'remfile.json',
      removeFiles: 'remfiles.json',
      apiConfiguration: 'v1/configurations',
      apiUpload: 'v1/files'
    },
    basePath: 'assets/mocks/'
  },
  accounts: {
    api: {
      accounts: 'v1/accounts'
    },
    basePath: ''
  },
  monitorUrl: 'http://yade.nfedev.accelya.com'
};
