package appointmentmaker.modeldao;

import appointmentmaker.model.Users;
import appointmentmaker.utility.DBUtility;
import appointmentmaker.utility.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * Class that handles Database access to the Users table
 */
public class UsersDAO {
    /**
     * Gets a list of Users from the Database
     * @return list of Users
     */
    public final ObservableList<Users> displayAllUsers(){
        ResultSet resultSet = null;
        String sql = "SELECT * from "+ Utility.getTableName(3)+";";
        try (PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            resultSet = ps.executeQuery();
            if(resultSet!=null)
                return getUsers(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return FXCollections.observableArrayList();
    }

    private ObservableList<Users> getUsers(ResultSet resultSet) throws SQLException {
        ObservableList<Users> usersList = FXCollections.observableArrayList();

        while (resultSet.next()){
            Users user = new Users();
            user.setUserId(resultSet.getInt("User_ID"));
            user.setUserName(resultSet.getString("User_Name"));
            user.setPassHash(resultSet.getString("Password"));
            final var created_by = resultSet.getString("Created_By");
            user.setLastUpdated(resultSet.getTimestamp("Last_Update").toLocalDateTime());
            user.setLastUpdatedBy(resultSet.getString("Last_Updated_By"));
            user.setSaltValue(created_by,this);
            usersList.add(user);
        }
        return usersList;
    }

    /**
     * Gets one User from the Database
     * @param userName username to search
     * @return Users object
     */
    public Users displayOneUser(String userName){
        ResultSet resultSet = null;
        String sql = "SELECT * from "+Utility.getTableName(3)+" WHERE User_Name = ?;";
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)) {
            ps.setString(1, userName);
            resultSet = ps.executeQuery();
                int numberOfRows = 0;
                while (resultSet.next())
                    numberOfRows++;
                if (numberOfRows == 1) {
                    ps.setString(1, userName);
                    resultSet = ps.executeQuery();
                    return getSingleUser(resultSet);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return new Users();
        }

    private Users getSingleUser(ResultSet resultSet) throws SQLException {
        Users user = new Users();
        resultSet.next();
        user.setUserId(resultSet.getInt("User_ID"));
        user.setUserName(resultSet.getString("User_Name"));
        user.setPassHash(resultSet.getString("Password"));
        final var created_by = resultSet.getString("Created_By");
        user.setSaltValue(created_by,this);
        user.setLastUpdated(resultSet.getTimestamp("Last_Update").toLocalDateTime());
        user.setLastUpdatedBy(resultSet.getString("Last_Updated_By"));
        return user;
    }

    /**
     * Updates a User in the Databse
     * @param userid user id to update
     * @param users replacement User
     * @return number of rows changed in the Database
     */
    public final int updateUser(int userid, Users users){
        String sql = "UPDATE "+Utility.getTableName(3)+" SET User_Name = ?," +
                " Password = ?,  Created_By = ?,  Last_Update = ?," +
                " Last_Updated_By = ? WHERE User_ID = ?;";
        int rowsChanged = 0;
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setString(1,users.getUserName());
            ps.setString(2,users.getPassHash());
            final var saltValue = users.getSaltValue();
            ps.setString(3, saltValue);
            ps.setInt(6,userid);
            ps.setTimestamp(4, Timestamp.valueOf(users.getLastUpdated()));
            ps.setString(5,users.getLastUpdatedBy());
            rowsChanged = ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowsChanged;
    }

    /**
     * Deletes a User from the Database
     * @param userid user id of User to delete
     * @return number of rows changed in the Database
     */
    public final int deleteUser(int userid){
        String sql = "DELETE FROM "+Utility.getTableName(3)+" WHERE User_ID=? ;";
        int rowsChanged = 0;
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setInt(1,userid);
            rowsChanged = ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowsChanged;
    }

    /**
     * Creates a User in the Database
     * @param users new User to create
     * @return number of rows changed in the Database
     */
    public final int createUser(Users users){
        String sql = "INSERT INTO "+Utility.getTableName(3)+" " +
                "( User_ID, User_Name, Password, Created_By, Last_Update, Last_Updated_By ) " +
                "VALUES(?,?,?,?,?,?) ;";
        int rowsChanged = 0;
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setNull(1, Types.INTEGER);
            ps.setString(2,users.getUserName());
            ps.setString(3,users.getPassHash());
            final var saltValue = users.getSaltValue();
            ps.setString(4, saltValue);
            ps.setTimestamp(5, Timestamp.valueOf(users.getLastUpdated()));
            ps.setString(6,users.getLastUpdatedBy());
            rowsChanged = ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowsChanged;
    }
}
