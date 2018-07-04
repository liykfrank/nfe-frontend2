
export const environment = {
  environment: 'dev',
  basePath: 'http://localhost:8080',
  ionic: false,
  production: false,
  mock: false,
  files: {
    api: {
      listFiles: '/v1/files',
      downloadFile: '/v1/files',
      downloadFiles: '/v1/files/zip',
      removeFile: '/v1/files',
      removeFiles: '/v1/files',
      apiConfiguration: '/v1/configurations',
      apiUpload: '/v1/files'
    },
    basePath: '/files'
  },
  sftAccount: {
    api: {
      create: '?u=register&p=register&path=/WebInterface/jQuery/messageForm.html&registration_username={1}&registration_email={2}',
      modify: '?command=request_reset&reset_username_email={1}&currentURL=http://ftp.nfedev.accelya.com/WebInterface/jQuery/reset.html'
    },
    basePath: 'http://ftp.nfedev.accelya.com/'
  },
  adm_acm: {
    api: {
      acdm: '/v1/acdms', // post: save | /{id} | /{id}/files
      configuration: '/v1/configurations',
      company: '/v1/airlines', // /{isoCountryCode}/{airlineCode}
      agent: '/v1/agents', // {code}
      country: '/v1/countries', // creo v1/configurations/isocs ¿?¿?
      toca: '/v1/tctps', // {isoc} | {isoc}/{code}: remove
      currency: '/v1/general-info/currencies', // {isoc}
      period: '/v1/general-info/periods', // {isoc}
      reasons: '/v1/reasons'
    },
    basePath: ''
  },
  monitorUrl: 'http://yade.nfedev.accelya.com'
};
