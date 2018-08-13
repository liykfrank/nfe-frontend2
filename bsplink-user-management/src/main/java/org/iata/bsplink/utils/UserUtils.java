package org.iata.bsplink.utils;

import lombok.extern.apachecommons.CommonsLog;
import org.iata.bsplink.model.entity.Address;
import org.iata.bsplink.model.entity.User;

@CommonsLog
public class UserUtils {

    private UserUtils() {}

    /**
     * Maps old user to new user.
     * 
     * @param oldUser Old user to map
     * @param newUser New user to map
     * @return User
     */
    public static User mapUserToUpdate(User oldUser, User newUser) {

        oldUser.setExpiryDate(newUser.getExpiryDate());
        oldUser.setUserType(newUser.getUserType());
        oldUser.setUserCode(newUser.getUserCode());
        oldUser.setName(newUser.getName());
        oldUser.setTelephone(newUser.getTelephone());
        oldUser.setOrganization(newUser.getOrganization());

        Address address = newUser.getAddress();
        address.setId(oldUser.getAddress() != null ? oldUser.getAddress().getId() : null);
        oldUser.setAddress(address);

        log.debug("built user to update: " + oldUser);

        return oldUser;
    }

}
