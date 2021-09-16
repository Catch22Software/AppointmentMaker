package appointmentmaker.modeldao;

import appointmentmaker.model.Customers;
import appointmentmaker.utility.DBUtility;
import appointmentmaker.utility.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.sql.*;

/**
 * Class that handles Database access ot the Customers table
 */
public final class CustomersDAO {
    /**
     * Gets a list of Customers from the Database
     * @return list of Customers
     */
    public ObservableList<Customers> displayAllCustomers(){
        ResultSet resultSet = null;
        String sql = "SELECT * from "+ Utility.getTableName(1)+";";
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            resultSet = ps.executeQuery();
            if(resultSet!=null)
                return getCustomers(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return FXCollections.observableArrayList();
    }

    private ObservableList<Customers> getCustomers(ResultSet resultSet) throws SQLException {
        ObservableList<Customers> cxList = FXCollections.observableArrayList();

        while (resultSet.next()){
            Customers cx = new Customers();
            cx.setCustId(resultSet.getInt("Customer_ID"));
            cx.setCustName(resultSet.getString("Customer_Name"));
            cx.setCustAddress(resultSet.getString("Address"));
            cx.setCustPhoneNumber(resultSet.getString("Phone"));
            cx.setCustPostalCode(resultSet.getString("Postal_Code"));
            var create = resultSet.getTimestamp("Create_Date").toLocalDateTime();
            cx.setCustCreateDate(create);
            cx.setCustCreatedBy(resultSet.getString("Created_By"));
            var update = resultSet.getTimestamp("Last_Update").toLocalDateTime();
            cx.setCustLastUpdate(update);
            cx.setCustLastUpdatedBy(resultSet.getString("Last_Updated_By"));
            cx.setCustDivId(resultSet.getInt("Division_ID"));
            cxList.add(cx);
        }
        return cxList;
    }

    /**
     * Gets one Customer from the Database based of Customer id
     * @param custId Customer id
     * @return Customers object
     */
    public Customers displayOneCustomer(int custId){
        ResultSet resultSet = null;
        String sql = "SELECT * from "+Utility.getTableName(1)+" WHERE Customer_ID = ?;";
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setInt(1,custId);
            resultSet = ps.executeQuery();
            int numberOfRows = 0;
            while(resultSet.next())
                numberOfRows++;
            if(numberOfRows==1){
                ps.setInt(1,custId);
                resultSet = ps.executeQuery();
                return getSingleCustomer(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return new Customers();
    }

    private Customers getSingleCustomer(ResultSet resultSet) throws SQLException {
        Customers customer = new Customers();
        resultSet.next();
        customer.setCustId(resultSet.getInt("Customer_ID"));
        customer.setCustName(resultSet.getString("Customer_Name"));
        customer.setCustAddress(resultSet.getString("Address"));
        customer.setCustPostalCode(resultSet.getString("Postal_Code"));
        customer.setCustPhoneNumber(resultSet.getString("Phone"));
        var create = resultSet.getTimestamp("Create_Date").toLocalDateTime();
        customer.setCustCreateDate(create);
        customer.setCustCreatedBy(resultSet.getString("Created_By"));
        var update = resultSet.getTimestamp("Last_Update").toLocalDateTime();
        customer.setCustLastUpdate(update);
        customer.setCustLastUpdatedBy(resultSet.getString("Last_Updated_By"));
        customer.setCustDivId(resultSet.getInt("Division_ID"));
        return customer;
    }

    /**
     * Updates Customer in the Database
     * @param custId Customer id to look up in Database
     * @param customers Customer object to update in the Database
     * @return number of rows changed in the Database
     */
    public int updateCustomer(int custId, Customers customers){
        String sql = "UPDATE "+Utility.getTableName(1)+
                " SET Customer_Name = ? , Address = ?,  Postal_Code = ?," +
                " Phone = ?, Last_Update = ? , Last_Updated_By = ? " +
                ", Division_ID = ? WHERE Customer_ID = ? ;";
        int rowsChanged = 0;
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setString(1,customers.getCustName());
            ps.setString(2,customers.getCustAddress());
            ps.setString(3,customers.getCustomerPostalCode());
            ps.setString(4,customers.getCustPhoneNumber());
            ps.setTimestamp(5, Timestamp.valueOf(customers.getCustLastUpdate()));
            ps.setString(6,customers.getCustLastUpdatedBy());
            ps.setInt(7,customers.getCustDivId());
            ps.setInt(8,custId);
            rowsChanged = ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowsChanged;
    }

    /**
     * Deletes a Customer in the Database
     * @param custId id of Customer
     * @return number of rows changed in the Database
     */
    public int deleteCustomer(int custId){
        String sql = "DELETE FROM "+Utility.getTableName(1)+" WHERE Customer_ID = ? ;";
        int rowsChanged = 0;
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setInt(1,custId);
            rowsChanged = ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowsChanged;
    }

    /**
     * Creates new Customer in the Database
     * @param customers Customer to add to the Database
     * @return number of rows changed in the Database
     */
    public int createCustomer(Customers customers){
        String sql = "INSERT INTO "+Utility.getTableName(1)+" " +
                "(Customer_ID, Customer_Name, Address, Postal_Code, " +
                "Phone, Create_Date, Created_By, Last_Update" +
                ", Last_Updated_By, Division_ID ) VALUES (?,?,?,?,?,?,?,?,?,? ) ;";
        int rowsChanged = 0;
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setNull(1, Types.INTEGER);
            ps.setString(2,customers.getCustName());
            ps.setString(3,customers.getCustAddress());
            ps.setString(4,customers.getCustomerPostalCode());
            ps.setString(5,customers.getCustPhoneNumber());
            ps.setTimestamp(6, Timestamp.valueOf(customers.getCustCreateDate()));
            ps.setString(7,customers.getCustCreatedBy());
            ps.setTimestamp(8, Timestamp.valueOf(customers.getCustLastUpdate()));
            ps.setString(9,customers.getCustLastUpdatedBy());
            ps.setInt(10,customers.getCustDivId());
            rowsChanged = ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowsChanged;
    }

    /**
     * Gets Division and Country name for Customer object from the Database from Division id
     * @param customers Customer object to get the Division id from
     * @return Customer object containing names of the Division and Country associated with it
     */
    public Customers getCustomerDivNameAndCountryName(Customers customers){
        Pair<String, String> names;
        int divId = customers.getCustDivId();
        if(divId <=0)
            return null;
        names = getDivNameAndCountryIdFromId(divId);
        assert names != null;
        customers.getCountries().setCountryName(names.getKey());
        customers.getFirstLevelDivision().setDivisionName(names.getValue());
        return customers;
    }

    private Pair<String, String> getDivNameAndCountryIdFromId(int divId) {
        ResultSet resultSet = null;
        String sql = "SELECT * from "+Utility.getTableName(0)+" WHERE Division_ID = ?;";
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setInt(1,divId);
            resultSet = ps.executeQuery();
            int numberOfRows = 0;
            while(resultSet.next())
                numberOfRows++;
            if(numberOfRows==1){
                ps.setInt(1,divId);
                resultSet = ps.executeQuery();
                return getResultsFromDivId(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return null;
    }

    private Pair<String, String> getResultsFromDivId(ResultSet resultSet) throws SQLException {
        Pair<String, Integer> firstResults;
        resultSet.next();
        String divName = resultSet.getString("Division");
        Integer countid = resultSet.getInt("Country_ID");
        firstResults = new Pair<>(divName,countid);
        String countryName = getCountryNameFromCountryID(firstResults.getValue());
        return new Pair<>(countryName, divName);
    }

    private String getCountryNameFromCountryID(Integer value) {
        ResultSet resultSet = null;
        String sql = "SELECT * from "+Utility.getTableName(5)+" WHERE Country_ID = ?;";
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setInt(1,value);
            resultSet = ps.executeQuery();
            int numberOfRows = 0;
            while(resultSet.next())
                numberOfRows++;
            if(numberOfRows==1){
                ps.setInt(1,value);
                resultSet = ps.executeQuery();
                return getResultsFromCountryID(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return null;
    }

    private String getResultsFromCountryID(ResultSet resultSet) throws SQLException {
        resultSet.next();
        return resultSet.getString("Country");
    }
}
