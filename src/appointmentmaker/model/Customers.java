package appointmentmaker.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Class that holds Customer objects
 */
public class Customers {

    private final IntegerProperty custId;
    private final StringProperty custName;
    private final StringProperty custAddress;
    private final StringProperty custPostalCode;
    private final StringProperty custPhoneNumber;
    private final ObjectProperty<LocalDateTime> custCreateDate;
    private final StringProperty custCreatedBy;
    private final ObjectProperty<LocalDateTime> custLastUpdate;
    private final StringProperty custLastUpdatedBy;
    private final IntegerProperty custDivId;
    private final Countries countries;
    private final FirstLevelDivision firstLevelDivision;

    /**
     * Creates a Customer object
     */
    public Customers(){
        custId = new SimpleIntegerProperty();
        custName = new SimpleStringProperty();
        custAddress = new SimpleStringProperty();
        custPostalCode = new SimpleStringProperty();
        custPhoneNumber = new SimpleStringProperty();
        countries = new Countries();
        firstLevelDivision = new FirstLevelDivision();
        custCreateDate = new SimpleObjectProperty<>();
        custCreatedBy = new SimpleStringProperty();
        custLastUpdate = new SimpleObjectProperty<>();
        custLastUpdatedBy = new SimpleStringProperty();
        custDivId = new SimpleIntegerProperty();
    }

    /**
     * Gets Country object
     * @return Country object
     */
    public final Countries getCountries(){return countries;}

    /**
     * Gets FirstLevelDivision object
     * @return FirstLevelDivision object
     */
    public final FirstLevelDivision getFirstLevelDivision(){return firstLevelDivision;}

    /**
     * Gets customer id
     * @return customer id
     */
    public final int getCustId(){return custId.get();}

    /**
     * Sets customer id
     * @param id customer id to set
     */
    public final void setCustId(int id){this.custId.set(id);}

    /**
     * Gets customer id as a property
     * @return customer id property
     */
    public IntegerProperty custIdProperty(){return custId;}

    /**
     * Gets customer name
     * @return customer name
     */
    public final String getCustName() {
        return custName.get();
    }

    /**
     * Sets customer name
     * @param name customer name to set
     */
    public final void setCustName(String name){this.custName.set(name);}

    /**
     * Gets customer name as a property
     * @return customer name property
     */
    public StringProperty custNameProperty() {
        return custName;
    }

    /**
     * Gets customer address
     * @return customer address
     */
    public final String getCustAddress(){return custAddress.get();}

    /**
     * Sets customer address
     * @param address customer address to set
     */
    public final void setCustAddress(String address){this.custAddress.set(address);}

    /**
     * Gets customer address as a property
     * @return customer address property
     */
    public StringProperty custAddressProperty() {
        return custAddress;
    }

    /**
     * Gets customer postal code
     * @return customer postal code
     */
    public final String getCustomerPostalCode(){return custPostalCode.get();}

    /**
     * Sets customer postal code
     * @param zip customer postal code to set
     */
    public final void setCustPostalCode(String zip){this.custPostalCode.set(zip);}

    /**
     * Gets customer postal code as a property
     * @return customer postal code property
     */
    public StringProperty custPostalCodeProperty() {
        return custPostalCode;
    }

    /**
     * Gets customer phone number
     * @return customer phone number
     */
    public final String getCustPhoneNumber(){return custPhoneNumber.get();}

    /**
     * Sets customer phone number
     * @param number customer phone number to set
     */
    public final void setCustPhoneNumber(String number){this.custPhoneNumber.set(number);}

    /**
     * Gets customer phone number as a property
     * @return customer phone number property
     */
    public StringProperty custPhoneNumberProperty() {
        return custPhoneNumber;
    }

    /**
     * Gets customer created date
     * @return customer create date
     */
    public final LocalDateTime getCustCreateDate(){return custCreateDate.get();}

    /**
     * Sets customer created date
     * @param localDateTime customer created date to set
     */
    public final void setCustCreateDate(LocalDateTime localDateTime){this.custCreateDate.set(localDateTime);}

    /**
     * Gets customer created date as a property
     * @return customer created date property
     */
    public ObjectProperty<LocalDateTime> custCreateDateProperty(){return custCreateDate;}

    /**
     * Gets customer created by
     * @return customer created by
     */
    public final String getCustCreatedBy() {
        return custCreatedBy.get();
    }

    /**
     * Sets customer created by
     * @param name customer created by to set
     */
    public final void setCustCreatedBy(String name){this.custCreatedBy.set(name);}

    /**
     * Gets customer created by as a property
     * @return customer created by property
     */
    public StringProperty custCreatedByProperty() {return custCreatedBy;}

    /**
     * Gets customer last update time
     * @return customer last update
     */
    public final LocalDateTime getCustLastUpdate() {return custLastUpdate.get();}

    /**
     * Sets customer last update time
     * @param localDateTime customer last update to set
     */
    public final void setCustLastUpdate(LocalDateTime localDateTime){this.custLastUpdate.set(localDateTime);}

    /**
     * Gets customer last update time as a property
     * @return customer last update time property
     */
    public ObjectProperty<LocalDateTime> custLastUpdateProperty() {return custLastUpdate;}

    /**
     * Gets customer last updated by
     * @return customer last updated by
     */
    public final String getCustLastUpdatedBy() {return custLastUpdatedBy.get();}

    /**
     * Sets customer last updated by
     * @param name customer last updated by to set
     */
    public final void setCustLastUpdatedBy(String name){this.custLastUpdatedBy.set(name);}

    /**
     * Gets customer last updated by as a property
     * @return customer last updated by property
     */
    public StringProperty custLastUpdatedByProperty() {return custLastUpdatedBy;}

    /**
     * Gets customer division id
     * @return customer division id
     */
    public final int getCustDivId() {return custDivId.get();}

    /**
     * Sets customer division id
     * @param id customer division id to set
     */
    public final void setCustDivId(int id){this.custDivId.set(id);}

    /**
     * Gets customer division id as a property
     * @return customer division id property
     */
    public IntegerProperty custDivIdProperty() {return custDivId;}

    private static boolean isEquals(Customers origCustomer, Customers testCustomer){
        // checks customer for equality against name,address,postal code phone number and division id
        if(testCustomer.getCustName().trim()
                .equalsIgnoreCase(origCustomer.getCustName().trim())){
            if (testCustomer.getCustAddress().trim()
                    .equalsIgnoreCase(origCustomer.getCustAddress().trim())){
                if(testCustomer.getCustomerPostalCode().trim()
                        .equalsIgnoreCase(origCustomer.getCustomerPostalCode().trim())){
                    if(testCustomer.getCustPhoneNumber().trim()
                            .equalsIgnoreCase(origCustomer.getCustPhoneNumber().trim())){
                        return testCustomer.getCustDivId() == origCustomer.getCustDivId();
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if list of Customer objects contain the test Customer
     * @param cxList list of Customer objects
     * @param test test Customer
     * @return true if list contains test
     */
    public static boolean isListContainTestItem(List<Customers> cxList, Customers test){
        boolean bln = false;
        for(Customers c : cxList){
            bln = isEquals(test,c);
            if(bln){
                break;
            }
        }
        return bln;
    }

    /**
     * Deep copies Customer object
     * @param original Original Customer object
     */
    public void copyCustomerObject(Customers original ){
        this.setCustId(original.getCustId());
        this.setCustName(original.getCustName());
        this.setCustAddress(original.getCustAddress());
        this.setCustPostalCode(original.getCustomerPostalCode());
        this.setCustPhoneNumber(original.getCustPhoneNumber());
        this.setCustCreateDate(original.getCustCreateDate());
        this.setCustCreatedBy(original.getCustCreatedBy());
        this.setCustLastUpdate(original.getCustLastUpdate());
        this.setCustLastUpdatedBy(original.getCustLastUpdatedBy());
        this.setCustDivId(original.getCustDivId());
    }
}
