package appointmentmaker.utility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class that handles utility level procedures
 */
public final class Utility {

    private static final List<String> TABLE_NAMES = List.of("WJ0618n.first_level_divisions",
            "WJ0618n.customers",
            "WJ0618n.appointments",
            "WJ0618n.users",
            "WJ0618n.contacts",
            "WJ0618n.countries");

    private static ResourceBundle bundle = null;
    private static int currentUserIdValue =-1;

    private Utility(){ // prevents Utility objects from being created
    }

    /**
     * Gets Resource Bundle for language translation
     * @return Resource Bundle
     */
    public static ResourceBundle getBundle(){return bundle;}

    /**
     * Checks for System default language and will assign the French Resource Bundle if appropriate
     */
    public static void setBundleOnce(){
        if(bundle==null){
            if(Locale.getDefault().getLanguage().equals("fr")) {
                bundle = ResourceBundle.getBundle("appointmentmaker/mybundle"
                        , Locale.FRENCH);
            }
            else{
                bundle = ResourceBundle.getBundle("appointmentmaker/mybundle"
                        ,Locale.ENGLISH);
            }
        }
    }

    /**
     * Gets the Table name from the list of names
     * @param index index to get from the list of names
     * @return table name
     */
    public static String getTableName(int index){return TABLE_NAMES.get(index);}

    /**
     * Handles updating the login activity log
     * @param userName username to add
     * @param now timestamp to add to log in respect to current user's system default time
     * @param b true if username was found in the Database
     * @param b1 true if password entered was valid in the Database
     */
    public static void appendToLoginActivity(String userName, ZonedDateTime now, boolean b, boolean b1) {

        try(FileWriter fw = new FileWriter("src/appointmentmaker/utility/Logs/login_activity.txt",
                true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)){
            String time =DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss").format(now);
            time+=" TimeZone: "+ ZoneId.systemDefault();
            String line="";
            if(!b && !b1)
                line+="User: "+userName+" has attempted to login with an incorrect UserName at "+ time+" **FAILURE** ";
            else if(b && !b1)
                line+="User: "+userName+" has attempted to login with an incorrect password at "+ time+" **FAILURE** ";
            else
                line+="User: "+userName+" has attempted to login successfully at "+time+" **SUCCESS** ";
            out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the current logged-in user id value
     * @return user id
     */
    public static int getCurrentUserIdValue() {
        return currentUserIdValue;
    }

    /**
     * Sets the current logged-in user id value
     * @param currentUserIdValue user id to set
     */
    public static void setCurrentUserIdValue(int currentUserIdValue) {
        Utility.currentUserIdValue = currentUserIdValue;
    }

    /**
     * Inner class to address creating localized weeks in respect to which day starts the official week for the local.
     */
    public static class LocalizedWeek{
        private static final DayOfWeek firstDayOfWeek=
                WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        private static final DayOfWeek lastDayOfWeek=
                DayOfWeek.of(((firstDayOfWeek.getValue()+5) % DayOfWeek.values().length)+1);

        private LocalizedWeek(){} // prevent objects from being created

        private static LocalDate getFirstDay(int year, int day){
            return LocalDate.ofYearDay(year,day)
                    .with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
        }
        private static LocalDate getLastDay(int year, int day){
            return LocalDate.ofYearDay(year,day)
                    .with(TemporalAdjusters.nextOrSame(lastDayOfWeek));
        }
        private static String toStringOnce(int year, int day){
            return String.format("Week from %s to %s", getFirstDay(year,day), getLastDay(year,day));
        }

        /**
         * Creates custom ComboBox with the weeks of the selected year based on the current locale.
         * Will also contain partial weeks both at the beginning and end of the selected year as well.
         * @param year selected year
         * @return list to populate ComboBox
         *
         * <h3> ***LAMBDA USAGE BELOW #2***</h3>
         * <p>
         * <b><i>
         *     Lambda usage in code in order to generate the custom list
         *     of Strings containing each week
         *       and accounts for each week being seven days to the appropriate dates
         *       are gathered in order to be displayed.
         *       </i></b>
         * </p>
         */
        public static ObservableList<String> createWeekComboBoxBasedOnYear(String year){
            List<String> listOfWeeks;
            AtomicInteger day =new AtomicInteger(1);
            listOfWeeks =Stream
                    .iterate(toStringOnce(Integer.parseInt(year),day.get()),s
                            ->toStringOnce(Integer.parseInt(year),day.addAndGet(7)))
                    .filter(c -> day.get()<367)
                    .limit(53)
                    .collect(Collectors.toList());
            return FXCollections.observableList(listOfWeeks);
        }
    }
}
