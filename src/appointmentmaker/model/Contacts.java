package appointmentmaker.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Class that holds Contact objects
 */
public class Contacts{

    private final IntegerProperty contactId;
    private final StringProperty contactName;

    /**
     * Creates a Contact object
     */
    public Contacts(){
        contactId = new SimpleIntegerProperty();
        contactName = new SimpleStringProperty();
    }

    /**
     * Gets contact id
     * @return contact id
     */
    public final int getContactId(){return contactId.get();}

    /**
     * Sets contact id
     * @param id contact id to set
     */
    public final void setContactId(int id){this.contactId.set(id);}

    /**
     * Gets contact id as a property
     * @return contact id property
     */
    public IntegerProperty contactIdProperty(){ return contactId;}

    /**
     * Gets contact name
     * @return contact name
     */
    public final String getContactName(){return contactName.get();}

    /**
     * Sets contact name
     * @param name contace name to set
     */
    public final void setContactName(String name){ this.contactName.set(name);}

    /**
     * Gets contact name as a property
     * @return contact name property
     */
    public StringProperty contactNameProperty(){return contactName;}
}