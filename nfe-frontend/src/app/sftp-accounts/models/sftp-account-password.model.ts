export interface SftpAccountPasswordIf {
    oldPassword: string;
    password: string;
    confirmPassword: string;
}

export class SftpAccountPassword implements SftpAccountPasswordIf {

    constructor ( public oldPassword: string,
      public password: string,
      public confirmPassword: string) {
      }
}
