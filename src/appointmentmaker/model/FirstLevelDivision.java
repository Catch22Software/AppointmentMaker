package appointmentmaker.model;

import javafx.beans.property.*;

/**
 * Class for holding FirstLevelDivision objects
 */
public class FirstLevelDivision {

    private final StringProperty divisionName;
    private final IntegerProperty divisionId;

    /**
     * Creates FirstLevelDivision object
     */
    public FirstLevelDivision() {
        divisionName = new SimpleStringProperty();
        divisionId = new SimpleIntegerProperty();
    }

    /**
     * Gets division name
     * @return division name
     */
    public final String getDivisionName() {
        return divisionName.get();
    }

    /**
     * Sets division name
     * @param divisionName division name to set
     */
    public final void setDivisionName(String divisionName) {
        this.divisionName.set(divisionName);
    }

    /**
     * Gets division name as a property
     * @return division name property
     */
    public StringProperty divisionNameProperty() {
        return divisionName;
    }

    /**
     * Gets division id
     * @return division id
     */
    public final int getDivisionId(){return divisionId.get();}

    /**
     * Sets division id
     * @param divisionId division id to set
     */
    public final void setDivisionId(int divisionId){this.divisionId.set(divisionId);}

    /**
     * Gets division id as a property
     * @return division id property
     */
    public IntegerProperty divisionIdProperty(){return divisionId;}
}