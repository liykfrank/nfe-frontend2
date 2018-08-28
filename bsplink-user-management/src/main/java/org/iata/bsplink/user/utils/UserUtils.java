package org.iata.bsplink.user.utils;

import org.iata.bsplink.user.model.entity.Address;
import org.iata.bsplink.user.model.entity.User;

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

        oldUser.setEmail(newUser.getEmail());
        oldUser.setExpiryDate(newUser.getExpiryDate());
        oldUser.setUserType(newUser.getUserType());
        oldUser.setUserCode(newUser.getUserCode());
        oldUser.setName(newUser.getName());
        oldUser.setLastName(newUser.getLastName());
        oldUser.setTelephone(newUser.getTelephone());
        oldUser.setOrganization(newUser.getOrganization());
        Address address = newUser.getAddress();
        address.setId(oldUser.getAddress() != null ? oldUser.getAddress().getId() : null);
        oldUser.setAddress(address);

        return oldUser;
    }

}
