import { NewUser } from './new-user.po';

describe('new User', () => {
  const page = new NewUser();

  describe('new Agent', () => {
    beforeEach(() => {
      page.navigateTo();
      page.selectAgentType();
    });

    it('set BASIC info on webPage to create New Agent', () => {
      const userCode = page.getInputCode();
      const name = page.getInputName();
      const lastname = page.getInputLastname();
      const username = page.getUsername();
      const email = page.getEmail();
      const organization = page.getOrganization();
      const telephone = page.getTelephone();

      const buttonCreate = page.getCreateButton();

      userCode.sendKeys('7820002');
      name.click();

      expect(userCode.isEnabled()).toBe(true);
      expect(name.isEnabled()).toBe(true);
      expect(lastname.isEnabled()).toBe(true);
      expect(username.isEnabled()).toBe(true);
      expect(email.isEnabled()).toBe(true);
      expect(organization.isEnabled()).toBe(true);
      expect(telephone.isEnabled()).toBe(true);
      expect(buttonCreate.isEnabled()).toBe(false);

      name.sendKeys('TEST1');
      lastname.sendKeys('TEST1');
      username.sendKeys('TEST1@TEST.ES');
      email.sendKeys('TEST1@TEST.ES');
      organization.sendKeys('TEST1');
      telephone.sendKeys('TEST1');
      expect(buttonCreate.isEnabled()).toBe(true);

      buttonCreate.click();

      expect(page.getOK().isPresent()).toBe(true);
    });

    it('set ALL info on webPage to create New Agent', () => {
      const userCode = page.getInputCode();
      const name = page.getInputName();
      const lastname = page.getInputLastname();
      const username = page.getUsername();
      const email = page.getEmail();
      const organization = page.getOrganization();
      const telephone = page.getTelephone();

      const locality = page.getLocality();
      const city = page.getCity();
      const zip = page.getZip();
      const country = page.getCountry();

      const buttonCreate = page.getCreateButton();

      userCode.sendKeys('7820002');
      name.click();

      expect(userCode.isEnabled()).toBe(true);
      expect(name.isEnabled()).toBe(true);
      expect(lastname.isEnabled()).toBe(true);
      expect(username.isEnabled()).toBe(true);
      expect(email.isEnabled()).toBe(true);
      expect(organization.isEnabled()).toBe(true);
      expect(telephone.isEnabled()).toBe(true);
      expect(buttonCreate.isEnabled()).toBe(false);

      expect(locality.isEnabled()).toBe(true);
      expect(city.isEnabled()).toBe(true);
      expect(zip.isEnabled()).toBe(true);
      expect(country.isEnabled()).toBe(true);

      name.sendKeys('TEST2');
      lastname.sendKeys('TEST2');
      username.sendKeys('TEST2@TEST.ES');
      email.sendKeys('TEST2@TEST.ES');
      organization.sendKeys('TEST2');
      telephone.sendKeys('TEST2');
      locality.sendKeys('TEST2');
      city.sendKeys('TEST2');
      zip.sendKeys('TEST2');
      country.sendKeys('TEST2');
      expect(buttonCreate.isEnabled()).toBe(true);

      buttonCreate.click();
      expect(page.getOK().isPresent()).toBe(true);
    });
  });
});
