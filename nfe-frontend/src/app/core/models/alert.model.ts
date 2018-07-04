import { AlertType } from './alert-type.enum';
import { TranslationService } from 'angular-l10n';

export class AlertModel {
    title: string;
    message: string;
    alert_type: AlertType;

    constructor(title: string, message: string, alert_type?: AlertType) {
      this.title = title;
      this.message = message;
      this.alert_type = alert_type || AlertType.INFO;
    }

}
