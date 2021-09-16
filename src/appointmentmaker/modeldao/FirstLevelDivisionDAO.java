package appointmentmaker.modeldao;

import appointmentmaker.model.FirstLevelDivision;
import appointmentmaker.utility.DBUtility;
import appointmentmaker.utility.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class that handles Database access to the FirstLevelDivision table
 */
public final class FirstLevelDivisionDAO {
    /**
     * Gets a list of FirstLevelDivisions from the Database
     * @return list of Divisions
     */
    public ObservableList<FirstLevelDivision> displayAllFirstLevelDivision(){
        ResultSet resultSet = null;
        String sql = "SELECT * from "+ Utility.getTableName(0)+";";
        try (PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            resultSet = ps.executeQuery();
            if(resultSet!=null)
                return getFirstLevelDivision(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return null;
    }

    /**
     * Gets a list of FirstLevelDivisions associated with one Country
     * @param countryId country id to perform search
     * @return list of Divisions
     */
    public ObservableList<FirstLevelDivision> displayAllFirstLevelDivisionAssosicatedWithCountry(int countryId){
        ResultSet resultSet = null;
        String sql = "SELECT * from "+ Utility.getTableName(0)+" WHERE Country_ID = ? ;";
        try (PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            ps.setInt(1,countryId);
            resultSet = ps.executeQuery();
            if(resultSet!=null)
                return getFirstLevelDivision(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return null;
    }

    private ObservableList<FirstLevelDivision> getFirstLevelDivision(ResultSet resultSet) throws SQLException {
        ObservableList<FirstLevelDivision> FirstLevelDivisionList = FXCollections.observableArrayList();

        while (resultSet.next()){
            FirstLevelDivision firstLevelDivision = new FirstLevelDivision();
            firstLevelDivision.setDivisionName(resultSet.getString("Division"));
            firstLevelDivision.setDivisionId(resultSet.getInt("Division_ID"));
            FirstLevelDivisionList.add(firstLevelDivision);
        }
        return FirstLevelDivisionList;
    }

    /**
     * Gets one FirstLevelDivision from the Database with a given Division name
     * @param divisionName Division name to search for
     * @return Division object
     */
    public FirstLevelDivision displayOneFirstLevelDivision(String divisionName){
        ResultSet resultSet = null;
        String sql = "SELECT * from "+Utility.getTableName(0)+" WHERE Division = ?;";
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)) {
            ps.setString(1, divisionName);
            resultSet = ps.executeQuery();
            int numberOfRows = 0;
            while (resultSet.next())
                numberOfRows++;
            if (numberOfRows == 1) {
                ps.setString(1, divisionName);
                resultSet = ps.executeQuery();
                return getSingleFirstLevelDivision(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return new FirstLevelDivision();
    }

    private FirstLevelDivision getSingleFirstLevelDivision(ResultSet resultSet) throws SQLException {
        FirstLevelDivision firstLevelDivision = new FirstLevelDivision();
        resultSet.next();
        firstLevelDivision.setDivisionName(resultSet.getString("Division"));
        firstLevelDivision.setDivisionId(resultSet.getInt("Division_ID"));
        return firstLevelDivision;
    }
}
