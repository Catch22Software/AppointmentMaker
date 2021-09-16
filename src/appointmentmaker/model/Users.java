package appointmentmaker.model;

import appointmentmaker.controller.UserViewController;
import appointmentmaker.modeldao.UsersDAO;
import javafx.beans.property.*;
import java.time.LocalDateTime;

/**
 * Class to hold User objects
 */
public class Users {

    private final IntegerProperty userId;
    private final StringProperty userName;
    private final StringProperty passHash;
    private String saltValue;
    private final ObjectProperty<LocalDateTime> lastUpdated;
    private final StringProperty lastUpdatedBy;

    /**
     * Create new User object
     */
    public Users(){
        userId = new SimpleIntegerProperty();
        userName = new SimpleStringProperty();
        passHash = new SimpleStringProperty();
        saltValue = "";
        lastUpdated = new SimpleObjectProperty<>();
        lastUpdatedBy = new SimpleStringProperty();
    }

    /**
     * Gets last updated LocalDateTime
     * @return LocalDateTime
     */
    public final LocalDateTime getLastUpdated(){return lastUpdated.get();}

    /**
     * Sets last updated LocalDateTime
     * @param localDateTime LocalDateTime to set
     */
    public final void setLastUpdated(LocalDateTime localDateTime){this.lastUpdated.set(localDateTime);}

    /**
     * Gets last updated as a property
     * @return LocalDateTime property
     */
    public ObjectProperty<LocalDateTime> lastUpdatedProperty(){return lastUpdated;}

    /**
     * Gets last updated by
     * @return last updated by
     */
    public final String getLastUpdatedBy(){return lastUpdatedBy.get();}

    /**
     * Sets last updated by
     * @param name last updated by to set
     */
    public final void setLastUpdatedBy(String name){this.lastUpdatedBy.set(name);}

    /**
     * Gets last updated by as a property
     * @return last updated by property
     */
    public StringProperty lastUpdatedByProperty(){return lastUpdatedBy;}

    /**
     * Gets user id
     * @return user id
     */
    public final int getUserId(){return userId.get();}

    /**
     * Sets user id
     * @param userId id to set
     */
    public final void setUserId(int userId){this.userId.set(userId);}

    /**
     * Returns user id as a property
     * @return user id property
     */
    public IntegerProperty userIdProperty(){return userId;}

    /**
     * Gets username
     * @return username
     */
    public final String getUserName() {
        return userName.get();
    }

    /**
     * Sets username
     * @param user username to set
     */
    public final void setUserName(String user){this.userName.set(user);}

    /**
     * Gets username as a property
     * @return username property
     */
    public StringProperty userNameProperty() {
        return userName;
    }

    /**
     * Gets password
     * @return password
     */
    public final String getPassHash() {
        return passHash.get();
    }

    /**
     * Sets password
     * @param pwdHash password to set
     */
    public final void setPassHash(String pwdHash){this.passHash.set(pwdHash);}

    /**
     * Gets password as a property
     * @return password property
     */
    public StringProperty passHashProperty() {
        return passHash;
    }

    /**
     * Gets salt value
     * @return salt value
     */
    public final String getSaltValue(){return saltValue;}

    /**
     * Sets salt value if caller is either UsersDAO or UserViewController
     * @param saltValue salt value to set
     * @param o checks object calling the method
     */
    public final void setSaltValue(String saltValue, Object o){
        if(o instanceof UsersDAO || o instanceof UserViewController)
            this.saltValue=saltValue;
    }
}
