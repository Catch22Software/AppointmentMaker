package appointmentmaker.modeldao;

import appointmentmaker.model.Appointments;
import appointmentmaker.utility.DBUtility;
import appointmentmaker.utility.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * Class that handles Database access to the Appointments table
 */
public final class AppointmentsDAO {
    /**
     * Gets the complete list of appointments from the database
     * @return list of appointments
     */
    public ObservableList<Appointments> displayAllAppointments(){
        ResultSet resultSet = null;
        String sql = "SELECT * from "+ Utility.getTableName(2)+";";
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            resultSet = ps.executeQuery();
            if(resultSet!=null)
                return getAppointments(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return FXCollections.observableArrayList();
    }

    /**
     * Gets a list of appointments assigned to one User
     * @param userId id of User
     * @return list of appointments
     */
    public ObservableList<Appointments> displayAllAppointmentsUnderUserId(int userId){
        ResultSet resultSet = null;
        String sql = "SELECT * from "+ Utility.getTableName(2)+"" +
                " WHERE User_ID = ?;";
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setInt(1,userId);
            resultSet = ps.executeQuery();
            if(resultSet!=null)
                return getAppointments(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return FXCollections.observableArrayList();
    }

    /**
     * Gets a list of appointments assigned to one Customer
     * @param custId id of Customer
     * @return list of appointments
     */
    public ObservableList<Appointments> displayAllAppointmentsUnderCustomerId(int custId){
        ResultSet resultSet = null;
        String sql = "SELECT * from "+ Utility.getTableName(2)+"" +
                " WHERE Customer_ID = ?;";
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setInt(1,custId);
            resultSet = ps.executeQuery();
            if(resultSet!=null)
                return getAppointments(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return FXCollections.observableArrayList();
    }
    private ObservableList<Appointments> getAppointments(ResultSet resultSet) throws SQLException {
        ObservableList<Appointments> apptList = FXCollections.observableArrayList();

        while (resultSet.next()){
            Appointments appt = new Appointments();
            int contactId=0;
            appt.setApptId(resultSet.getInt("Appointment_ID"));
            appt.setApptTitle(resultSet.getString("Title"));
            appt.setApptDesc(resultSet.getString("Description"));
            appt.setApptLocation(resultSet.getString("Location"));
            appt.setApptType(resultSet.getString("Type"));
            var start = (resultSet.getTimestamp("Start")).toLocalDateTime();
            appt.setApptStartTime(start);
            var end = (resultSet.getTimestamp("End")).toLocalDateTime();
            appt.setApptEndTime(end);
            appt.setApptCreatedBy(resultSet.getString("Created_By"));
            appt.setApptLastUpdatedBy(resultSet.getString("Last_Updated_By"));
            var create = (resultSet.getTimestamp("Create_Date")).toLocalDateTime();
            appt.setApptCreateTime(create);
            var update = (resultSet.getTimestamp("Last_Update")).toLocalDateTime();
            appt.setApptLastUpdateTime(update);
            appt.setCxId(resultSet.getInt("Customer_ID"));
            appt.setContactId(resultSet.getInt("Contact_ID"));
            contactId = resultSet.getInt("Contact_ID");
            appt.setContactName(ContactsDAO.getContactNameFromId(contactId));
            appt.setUserId(resultSet.getInt("User_ID"));
            apptList.add(appt);
        }
        return apptList;
    }

    /**
     * Gets one appointment based of the appointment id
     * @param apptId id of appointment
     * @return appointment object
     */
    public Appointments displayOneAppointment(int apptId){
        ResultSet resultSet = null;
        String sql = "SELECT * from "+Utility.getTableName(2)+" WHERE Appointment_ID = ?;";
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setInt(1,apptId);
            resultSet = ps.executeQuery();
            int numberOfRows = 0;
            while(resultSet.next())
                numberOfRows++;
            if(numberOfRows==1){
                ps.setInt(1,apptId);
                resultSet = ps.executeQuery();
                return getSingleAppointment(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return new Appointments();
    }

    private Appointments getSingleAppointment(ResultSet resultSet) throws SQLException {
        Appointments appt = new Appointments();
        int contactId = 0;
        resultSet.next();
        appt.setApptId(resultSet.getInt("Appointment_ID"));
        appt.setApptTitle(resultSet.getString("Title"));
        appt.setApptDesc(resultSet.getString("Description"));
        appt.setApptLocation(resultSet.getString("Location"));
        appt.setApptType(resultSet.getString("Type"));
        var start = (resultSet.getTimestamp("Start")).toLocalDateTime();
        appt.setApptStartTime(start);
        var end = (resultSet.getTimestamp("End")).toLocalDateTime();
        appt.setApptEndTime(end);
        appt.setApptCreatedBy(resultSet.getString("Created_By"));
        appt.setApptLastUpdatedBy(resultSet.getString("Last_Updated_By"));
        var create = (resultSet.getTimestamp("Create_Date")).toLocalDateTime();
        appt.setApptCreateTime(create);
        var update = (resultSet.getTimestamp("Last_Update")).toLocalDateTime();
        appt.setApptLastUpdateTime(update);
        appt.setCxId(resultSet.getInt("Customer_ID"));
        appt.setContactId(resultSet.getInt("Contact_ID"));
        contactId = resultSet.getInt("Contact_ID");
        appt.setContactName(ContactsDAO.getContactNameFromId(contactId));
        appt.setUserId(resultSet.getInt("User_ID"));
        return appt;
    }

    /**
     * Updates appointment in Database
     * @param apptId id of appointment to update
     * @param appointments replacement appointment
     * @return number of rows changed in the Database
     */
    public int updateAppointment(int apptId, Appointments appointments){
        String sql = "UPDATE "+Utility.getTableName(2)+
                " SET Title = ?, Description = ?, Location = ? ," +
                " Type = ?,  Start = ?,  End = ? ,  Customer_ID = ?" +
                ",  User_ID = ?,  Contact_ID = ?, Last_Update = ? , Last_Updated_By = ? " +
                " WHERE Appointment_ID = ? ;";
        int rowsChanged = 0;
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setString(1,appointments.getApptTitle());
            ps.setString(2,appointments.getApptDesc());
            ps.setString(3,appointments.getApptLocation());
            ps.setString(4,appointments.getApptType());
            ps.setTimestamp(5,(Timestamp.valueOf(appointments.getApptStartTime())));
            ps.setTimestamp(6,(Timestamp.valueOf(appointments.getApptEndTime())));
            ps.setInt(7,appointments.getCxId());
            ps.setInt(8,appointments.getUserId());
            ps.setInt(9,appointments.getContactId());
            ps.setTimestamp(10,(Timestamp.valueOf(appointments.getApptLastUpdateTime())));
            ps.setString(11,appointments.getApptLastUpdatedBy());
            ps.setInt(12,apptId);
            rowsChanged = ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowsChanged;
    }

    /**
     * Deletes one appointment from the Database
     * @param apptId id of appointment to delete
     * @return number of rows changed in the Database
     */
    public int deleteAppointment(int apptId){
        String sql = "DELETE from "+Utility.getTableName(2)+" WHERE Appointment_ID = ? ;";
        int rowsChanged = 0;
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setInt(1,apptId);
            rowsChanged = ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowsChanged;
    }

    /**
     * Adds one appointment to the Database
     * @param appt appointment to add
     * @return number of rows changed in the Database
     */
    public int createAppointment(Appointments appt){
        String sql = "INSERT INTO "+Utility.getTableName(2)+
                "( Appointment_ID, Title, Description, Location, Type," +
                "Start, End, Create_Date, Created_By, Last_Update, " +
                "Last_Updated_By, Customer_ID, User_ID, Contact_ID )" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ;";
        int rowsChanged = 0;
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setNull(1, Types.INTEGER);
            ps.setString(2, appt.getApptTitle());
            ps.setString(3, appt.getApptDesc());
            ps.setString(4, appt.getApptLocation());
            ps.setString(5, appt.getApptType());
            ps.setTimestamp(6,(Timestamp.valueOf(appt.getApptStartTime())));
            ps.setTimestamp(7,(Timestamp.valueOf(appt.getApptEndTime())));
            ps.setTimestamp(8,Timestamp.valueOf(appt.getApptCreateTime()));
            ps.setString(9,appt.getApptCreatedBy());
            ps.setTimestamp(10,Timestamp.valueOf(appt.getApptLastUpdateTime()));
            ps.setString(11, appt.getApptLastUpdatedBy());
            ps.setInt(12,appt.getCxId());
            ps.setInt(13,appt.getUserId());
            ps.setInt(14,appt.getContactId());
            rowsChanged = ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowsChanged;
    }
}
