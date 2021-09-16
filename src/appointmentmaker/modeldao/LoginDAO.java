package appointmentmaker.modeldao;

import appointmentmaker.model.Users;

/**
 * Gives restricted access to the User table in the Database
 */
public final class LoginDAO extends UsersDAO {
    /**
     * Overridden method getting one User from the Database
     * @param userName username to search
     * @return Users object
     */
    @Override
    public Users displayOneUser(String userName) {
        return super.displayOneUser(userName);
    }
}
