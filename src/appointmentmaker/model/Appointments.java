package appointmentmaker.model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.time.LocalDateTime;

/**
 * Class that holds Appointment objects
 */
public class Appointments{

    private final Users user;
    private final Contacts contacts;
    private final Customers cx;
    private final IntegerProperty apptId;
    private final StringProperty apptTitle;
    private final StringProperty apptDesc;
    private final StringProperty apptType;
    private final StringProperty apptLocation;
    private final ObjectProperty<LocalDateTime> apptStartTime;
    private final ObjectProperty<LocalDateTime> apptEndTime;
    private final ObjectProperty<LocalDateTime> apptCreateTime;
    private final StringProperty apptCreatedBy;
    private final ObjectProperty<LocalDateTime> apptLastUpdateTime;
    private final StringProperty apptLastUpdatedBy;
    private final IntegerProperty cxId;
    private final IntegerProperty userId;
    private final IntegerProperty contactId;
    private final StringProperty contactName;

    /**
     * Creates an Appointment object
     */
    public Appointments(){
        user = new Users();
        contacts = new Contacts();
        cx = new Customers();
        apptId = new SimpleIntegerProperty();
        apptTitle = new SimpleStringProperty();
        apptDesc = new SimpleStringProperty();
        apptType = new SimpleStringProperty();
        apptLocation = new SimpleStringProperty();
        apptStartTime = new SimpleObjectProperty<>();
        apptEndTime = new SimpleObjectProperty<>();
        apptCreateTime = new SimpleObjectProperty<>();
        apptCreatedBy = new SimpleStringProperty();
        apptLastUpdateTime = new SimpleObjectProperty<>();
        apptLastUpdatedBy = new SimpleStringProperty();
        cxId = new SimpleIntegerProperty();
        userId = new SimpleIntegerProperty();
        contactId = new SimpleIntegerProperty();
        contactName = new SimpleStringProperty();
    }

    /**
     * Get Users object attached to an appointment
     * @return Users object
     */
    public Users getUser(){ return this.user;}

    /**
     * Get Contacts object attached to an appointment
     * @return Contacts object
     */
    public Contacts getContacts(){return this.contacts;}

    /**
     * Gets Customers object attached to an appointment
     * @return Customers object
     */
    public Customers getCustomers(){return this.cx;}

    /**
     * Gets appointment location
     * @return appointment location
     */
    public final String getApptLocation(){return apptLocation.get();}

    /**
     * Sets appointment location
     * @param location appointment location to set
     */
    public final void setApptLocation(String location){this.apptLocation.set(location);}

    /**
     * Gets appointment location as a property
     * @return appointment location property
     */
    public StringProperty apptLocationProperty(){return apptLocation;}

    /**
     * Gets appointment id
     * @return appointment id
     */
    public final int getApptId(){ return apptId.get();}

    /**
     * Sets appointment id
     * @param id appointment id to set
     */
    public final void setApptId(int id){this.apptId.set(id);}

    /**
     * Gets appointment id as a property
     * @return appointment id property
     */
    public IntegerProperty apptIdProperty(){return apptId;}

    /**
     * Gets appointment title
     * @return appointment title
     */
    public final String getApptTitle(){return apptTitle.get();}

    /**
     * Sets appointment title
     * @param title appointment title to set
     */
    public final void setApptTitle(String title){this.apptTitle.set(title);}

    /**
     * Gets appointment title as a property
     * @return appointment title property
     */
    public StringProperty apptTitleProperty() {
        return apptTitle;
    }

    /**
     * Gets appointment description
     * @return appointment description
     */
    public final String getApptDesc(){return apptDesc.get();}

    /**
     * Sets appointment description
     * @param desc appointment description to set
     */
    public final void setApptDesc(String desc){this.apptDesc.set(desc);}

    /**
     * Gets appointment description as a property
     * @return appointment description property
     */
    public StringProperty apptDescProperty() {
        return apptDesc;
    }

    /**
     * Gets appointment type
     * @return appointment type
     */
    public final String getApptType(){return apptType.get();}

    /**
     * Sets appointment type
     * @param type appointment type to set
     */
    public final void setApptType(String type){this.apptType.set(type);}

    /**
     * Gets appointment type as a property
     * @return appointment type property
     */
    public StringProperty apptTypeProperty() {
        return apptType;
    }

    /**
     * Gets appointment start time
     * @return appointment start time
     */
    public final LocalDateTime getApptStartTime(){return apptStartTime.get();}

    /**
     * Sets appointment start time
     * @param localDateTime appointment start time to set
     */
    public final void setApptStartTime(LocalDateTime localDateTime){ this.apptStartTime.set(localDateTime);}

    /**
     * Gets appointment start time as a property
     * @return appointment start time property
     */
    public ObjectProperty<LocalDateTime> apptStartTimeProperty(){return apptStartTime;}

    /**
     * Gets appointment end time
     * @return appointment end time
     */
    public final LocalDateTime getApptEndTime(){return apptEndTime.get();}

    /**
     * Sets appointment end time
     * @param localDateTime appointment end time to set
     */
    public final void setApptEndTime(LocalDateTime localDateTime){this.apptEndTime.set(localDateTime);}

    /**
     * Gets appointment end time as a property
     * @return appointment end time property
     */
    public ObjectProperty<LocalDateTime> apptEndTimeProperty(){return apptEndTime;}

    /**
     * Gets Customer id attached to appointment
     * @return Customer id
     */
    public final int getCxId(){return cxId.get();}

    /**
     * Sets Customer id attached to appointment
     * @param id Customer id to set
     */
    public final void setCxId(int id){this.cxId.set(id);}

    /**
     * Gets Customer id attached to appointment as a property
     * @return Customer id property
     */
    public IntegerProperty cxIdProperty(){return cxId;}

    /**
     * Gets User id attached to appointment
     * @return User id
     */
    public final int getUserId(){return userId.get();}

    /**
     * Sets User id attached to appointment
     * @param id User id to set
     */
    public final void setUserId(int id){this.userId.set(id);}

    /**
     * Gets User id attached to appointment as a property
     * @return User id property
     */
    public IntegerProperty userIdProperty(){return userId;}

    /**
     * Gets Contact id attached to appointment
     * @return Contact id
     */
    public final int getContactId(){return contactId.get();}

    /**
     * Sets Contact id attached to appointment
     * @param id Contact id to set
     */
    public final void setContactId(int id){this.contactId.set(id);}

    /**
     * Gets Contact id attached to appointment as a property
     * @return Contact id property
     */
    public IntegerProperty contactIdProperty(){return contactId;}

    /**
     * Gets Contact name attached to appointment
     * @return Contact name
     */
    public final String getContactName(){return contactName.get();}

    /**
     * Sets Contact name attached to appointment
     * @param name Contact name to set
     */
    public final void setContactName(String name){this.contactName.set(name);}

    /**
     * Gets Contact name attached to appointment as a property
     * @return Contact name property
     */
    public StringProperty contactNameProperty(){return contactName;}

    /**
     * Gets appointment creation time
     * @return appointment creation time
     */
    public final LocalDateTime getApptCreateTime(){return apptCreateTime.get();}

    /**
     * Sets appointment creation time
     * @param localDateTime appointment creation time to set
     */
    public final void setApptCreateTime(LocalDateTime localDateTime){this.apptCreateTime.set(localDateTime);}

    /**
     * Gets appointment creation time as a property
     * @return appointment creation time property
     */
    public ObjectProperty<LocalDateTime> apptCreateTimeProperty(){return apptCreateTime;}

    /**
     * Gets appointment created by
     * @return appointment created by
     */
    public final String getApptCreatedBy(){return apptCreatedBy.get();}

    /**
     * Sets appointment created by
     * @param name appointment created by to set
     */
    public final void setApptCreatedBy(String name){this.apptCreatedBy.set(name);}

    /**
     * Gets appointment created by as a property
     * @return appointment created by property
     */
    public StringProperty apptCreatedByProperty(){return apptCreatedBy;}

    /**
     * Gets appointment last updated
     * @return appointment last updated
     */
    public final LocalDateTime getApptLastUpdateTime(){return apptLastUpdateTime.get();}

    /**
     * Sets appointment last updated
     * @param localDateTime appointment last updated to set
     */
    public final void setApptLastUpdateTime(LocalDateTime localDateTime){this.apptLastUpdateTime.set(localDateTime);}

    /**
     * Gets appointment last updated as a property
     * @return appointment last updated property
     */
    public ObjectProperty<LocalDateTime> apptLastUpdateTimeProperty(){return apptLastUpdateTime;}

    /**
     * Gets appointment last updated by
     * @return appointment last updated by
     */
    public final String getApptLastUpdatedBy(){return apptLastUpdatedBy.get();}

    /**
     * Sets appointment last updated by
     * @param name appointment last updated by to set
     */
    public final void setApptLastUpdatedBy(String name){this.apptLastUpdatedBy.set(name);}

    /**
     * Gets appointment last updated by as a property
     * @return appointment last updated by property
     */
    public StringProperty apptLastUpdatedByProperty(){return apptLastUpdatedBy;}

    private static boolean isApptTimesAllowed(Appointments origAppointment, Appointments test){
        // checks if two appointments' start and end times overlap
        if(test.getCxId()==origAppointment.getCxId()){

            return (((test.getApptEndTime().isEqual(origAppointment.getApptStartTime()))
                    || (test.getApptEndTime().isBefore(origAppointment.getApptStartTime())))
                    ||((test.getApptStartTime().isEqual(origAppointment.getApptEndTime()))
                    || (test.getApptStartTime().isAfter(origAppointment.getApptEndTime())))
                    ||(((test.getApptStartTime().isBefore(origAppointment.getApptStartTime()))
                    && (test.getApptEndTime().isBefore(origAppointment.getApptStartTime())))
                        || ((test.getApptStartTime().isAfter(origAppointment.getApptEndTime()))
                    && (test.getApptEndTime().isAfter(origAppointment.getApptEndTime())))));
        }
        return true;
    }

    /**
     * Overridden toString method
     * @return Appointment object as a String
     */
    @Override
    public String toString() {
        return "Appointments{" +
                "user=" + user +
                ", contacts=" + contacts +
                ", cx=" + cx +
                ", apptId=" + apptId +
                ", apptTitle=" + apptTitle +
                ", apptDesc=" + apptDesc +
                ", apptType=" + apptType +
                ", apptLocation=" + apptLocation +
                ", apptStartTime=" + apptStartTime +
                ", apptEndTime=" + apptEndTime +
                ", apptCreateTime=" + apptCreateTime +
                ", apptCreatedBy=" + apptCreatedBy +
                ", apptLastUpdateTime=" + apptLastUpdateTime +
                ", apptLastUpdatedBy=" + apptLastUpdatedBy +
                ", cxId=" + cxId +
                ", userId=" + userId +
                ", contactId=" + contactId +
                ", contactName=" + contactName +
                '}';
    }

    /**
     * Checks if appointment is allowed to be added against list and possible edited appointment
     * @param apptList list of appointments
     * @param test appointment to be added
     * @param oneToEdit appointment to be edited (can be NULL)
     * @return true if appointment is allowed
     *
     * <h3> ***LAMBDA USAGE BELOW #1***</h3>
     *<p>
     *     <b><i>
     *        Lambda usage in code in order to set new Predicate on filtered list
     *        to not include the edited appointment
     *        as long as the test appointment is not equal to the edited one.
     *     </i></b>
     *</p>
     */
    public static boolean isApptTimesAllowedAgainstList(ObservableList<Appointments> apptList, Appointments test
            , Appointments oneToEdit){
        boolean bln = true;
        if(oneToEdit!=null){
            // NULL if creating new appointment instead of editing one
            if(isEquals(test,oneToEdit)){
                bln = false;
            }
            else{
                FilteredList<Appointments> filteredList =
                        new FilteredList<>(apptList,appointments
                                -> appointments.getApptId()!=oneToEdit.getApptId());
                for(Appointments a: filteredList){
                    bln = isApptTimesAllowed(a,test);
                    if(!bln)
                        break;
                }
            }
        }
        else{
            for(Appointments a: apptList){
                bln = isApptTimesAllowed(a,test);
                if(!bln)
                    break;
            }
        }
        return bln;
    }
    private static boolean isEquals(Appointments a, Appointments original){
        // checks if two appointments are equal, not overriding the built-in equals method
        if(original==a)
            return true;
        return ((a.getApptId()==original.getApptId())
                && (a.getApptTitle().equals(original.getApptTitle()))
                && (a.getApptDesc().equals(original.getApptDesc()))
                && (a.getApptLocation().equals(original.getApptLocation()))
                && (a.getApptType().equals(original.getApptType()))
                &&(a.getApptStartTime().equals(original.getApptStartTime()))
                && (a.getApptEndTime().equals(original.getApptEndTime()))
                &&(a.getCxId()==original.getCxId())
                && (a.getUserId()==original.getUserId())
                &&(a.getContactId()==original.getContactId()));
    }
}
