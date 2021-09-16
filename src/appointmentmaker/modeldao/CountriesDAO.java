package appointmentmaker.modeldao;

import appointmentmaker.model.Countries;
import appointmentmaker.utility.DBUtility;
import appointmentmaker.utility.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class that handles Database access to the Countries table
 */
public final class CountriesDAO {
    /**
     * Gets a list of Countries from the Database
     * @return list of Countries
     */
    public ObservableList<Countries> displayAllCountries(){
        ResultSet resultSet = null;
        String sql = "SELECT * from "+ Utility.getTableName(5)+";";
        try (PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)){
            resultSet = ps.executeQuery();
            if(resultSet!=null)
                return getCountries(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return null;
    }

    private ObservableList<Countries> getCountries(ResultSet resultSet) throws SQLException {
        ObservableList<Countries> countriesList = FXCollections.observableArrayList();

        while (resultSet.next()){
            Countries countries = new Countries();
            countries.setCountryName(resultSet.getString("Country"));
            countries.setCountryId(resultSet.getInt("Country_ID"));
            countriesList.add(countries);
        }
        return countriesList;
    }

    /**
     * Gets one Country from the Database from one Country name
     * @param countryName name of Country
     * @return Country object
     */
    public Countries displayOneCountries(String countryName){
        ResultSet resultSet = null;
        String sql = "SELECT * from "+Utility.getTableName(5)+" WHERE Country = ?;";
        try(PreparedStatement ps = DBUtility.dbGetConnection().prepareStatement(sql)) {
            ps.setString(1, countryName);
            resultSet = ps.executeQuery();
            int numberOfRows = 0;
            while (resultSet.next())
                numberOfRows++;
            if (numberOfRows == 1) {
                ps.setString(1, countryName);
                resultSet = ps.executeQuery();
                return getSingleCountries(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DBUtility.dbCloseRsQuietly(resultSet);
        }
        return new Countries();
    }

    private Countries getSingleCountries(ResultSet resultSet) throws SQLException {
       Countries countries = new Countries();
       resultSet.next();
       countries.setCountryName(resultSet.getString("Country"));
       countries.setCountryId(resultSet.getInt("Country_ID"));
       return countries;
    }
}
