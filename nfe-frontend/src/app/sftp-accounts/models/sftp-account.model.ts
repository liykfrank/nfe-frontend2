export interface SftpAccountIf {
    login: string;
    status: string;
    publicKey: string;
}

export class SftpAccount implements SftpAccountIf {
  public mode = 'RO';

    constructor ( public login: string,
      public status: string,
      public publicKey: string) {
      }
}
