export const environment = {
  ionic: false,
  basePath: 'http://localhost:4200/',
  production: false,
  mock: true,
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
      accounts: ''
    },
    basePath: ''
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
      acdm: '',
      configuration: 'configuration.json',
      company: 'airline.json',
      agent: 'agent.json',
      country: 'countries.json',
      toca: 'toca.json',
      currency: 'currency.json',
      period: 'billingPeriod.json',
      reasons: 'reasons.json'
    },
    basePath: 'assets/mocks/adcm/'
  },
  monitorUrl: 'http://yade.nfedev.accelya.com'
};
