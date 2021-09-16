package appointmentmaker.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Class that holds Country objects
 */
public class Countries {

    private final StringProperty countryName;
    private final IntegerProperty countryId;

    /**
     * Creates Country objects
     */
    public Countries(){
        countryName = new SimpleStringProperty();
        countryId = new SimpleIntegerProperty();
    }

    /**
     * Gets country name
     * @return country name
     */
    public final String getCountryName(){return countryName.get();}

    /**
     * Sets country name
     * @param name country name to set
     */
    public final void setCountryName(String name){this.countryName.set(name);}

    /**
     * Gets country name as a property
     * @return country name property
     */
    public StringProperty countryNameProperty() {
        return countryName;
    }

    /**
     * Gets country id
     * @return coutnry id
     */
    public final int getCountryId(){return countryId.get();}

    /**
     * Sets country id
     * @param CountryId country id to set
     */
    public final void setCountryId(int CountryId){this.countryId.set(CountryId);}

    /**
     * Gets country id as a property
     * @return country id property
     */
    public IntegerProperty CountryIdProperty(){return countryId;}
}
