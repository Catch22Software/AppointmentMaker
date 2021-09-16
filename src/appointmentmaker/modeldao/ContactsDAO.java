package appointmentmaker.modeldao;

import appointmentmaker.model.Contacts;
import appointmentmaker.utility.DBUtility;
import appointmentmaker.utility.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class that handles Database access to the Contacts table
 */
public final class ContactsDAO {
    /**
     * Gets a list of all Contacts from the Database
     * @return list of Contacts
     */
    public ObservableList<Contacts> displayAllContacts(){
        ResultSet resultSet = null;
        String sql = "SELECT * from "+ Utility.getTableName(4)+";";
        try (PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            resultSet = ps.executeQuery();
            if(resultSet!=null)
                return getContacts(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return FXCollections.observableArrayList();
    }

    private ObservableList<Contacts> getContacts(ResultSet resultSet) throws SQLException {
        ObservableList<Contacts> contactsList = FXCollections.observableArrayList();

        while (resultSet.next()){
            Contacts contact = new Contacts();
            contact.setContactId(resultSet.getInt("Contact_ID"));
            contact.setContactName(resultSet.getString("Contact_Name"));
            contactsList.add(contact);
        }
        return contactsList;
    }

    /**
     * Gets one Contact from the Database
     * @param contactName name of Contact
     * @return Contacts object
     */
    public Contacts displayOneContact(String contactName){
        ResultSet resultSet = null;
        String sql = "SELECT * from "+Utility.getTableName(4)+" WHERE Contact_Name = ?;";
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)) {
            ps.setString(1, contactName);
            resultSet = ps.executeQuery();
            int numberOfRows = 0;
            while (resultSet.next())
                numberOfRows++;
            if (numberOfRows == 1) {
                ps.setString(1, contactName);
                resultSet = ps.executeQuery();
                return getSingleContact(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return new Contacts();
    }

    private Contacts getSingleContact(ResultSet resultSet) throws SQLException {
        Contacts contacts = new Contacts();
        resultSet.next();
        contacts.setContactId(resultSet.getInt("Contact_ID"));
        contacts.setContactName(resultSet.getString("Contact_Name"));
        return contacts;
    }

    /**
     * Gets Contacts object from Database from one Contact id
     * @param id Contact id
     * @return Contacts object
     */
    public static String getContactNameFromId(int id){
        ResultSet resultSet = null;
        String sql = "SELECT Contact_Name from "+Utility.getTableName(4)+
                " WHERE Contact_ID = ? ;";
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setInt(1,id);
            resultSet = ps.executeQuery();
            int numberOfRows = 0;
            while (resultSet.next())
                numberOfRows++;
            if (numberOfRows == 1) {
                ps.setInt(1,id);
                resultSet = ps.executeQuery();
                return getSingleContactName(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return "";
    }
    private static String getSingleContactName(ResultSet resultSet) throws SQLException {
        resultSet.next();
        return resultSet.getString("Contact_Name");
    }
}
