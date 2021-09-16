package appointmentmaker.utility;

import appointmentmaker.model.Users;
import appointmentmaker.modeldao.LoginDAO;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.UUID;

/**
 * Class that handles hashing a plain text password to be stored in Database
 */
public final class PassWrdHash {


    private static String saltStringValForDb;
    private static byte[] salt;
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private final static int MIN_NUM_OF_CHARS = 4;

    /**
     * Creates hashed password and associated random salt value to store
     * @param pwd plain text password as a character array
     * @return String array containing password hash and salt value
     */
    public static String[] doPrivateStuffWithPlainTextPassword(char[] pwd){
        var coolStuffz = new String[2];
        coolStuffz[0]=createPassword(pwd);
        coolStuffz[1]=getSalt();
        return coolStuffz;

    }

    /**
     * Validates username password combo in Database
     * @param userName username to check
     * @param supposedPassWord plain text password as a character array to check
     * @return Validation result
     */
    public static String validateUser(String userName, char[] supposedPassWord) {

        String returnValue="";
        LoginDAO loginDAO = new LoginDAO();
        Users user = loginDAO.displayOneUser(userName);
        if(user.getPassHash()==null){
            Utility.appendToLoginActivity(userName, ZonedDateTime.now(),false, false);
            // writes to log in activity log
            returnValue +=Utility.getBundle().getString("PassHashreturn1");
            return returnValue;
        }
        var actualPassWordHash = user.getPassHash(); // gets hash from Database
        var actualSalt = user.getSaltValue(); // gets salt from Database
        String validateHash = generateHash(supposedPassWord,valueToAddToPassHash(actualSalt));
        // creates hash to check
        if (validateHash != null){
            boolean bln =validateHash.equals(actualPassWordHash);
            if(bln) {
                Utility.appendToLoginActivity(userName,ZonedDateTime.now(),true,true);
                // writes to log in activity log
                Utility.setCurrentUserIdValue(user.getUserId());
                returnValue+=Utility.getBundle().getString("PassHashreturn2");
            }
            else{
                Utility.appendToLoginActivity(userName,ZonedDateTime.now(),true,false);
                // writes to log in activity log
                returnValue+=Utility.getBundle().getString("PassHashreturn3");
            }
        }
        return returnValue;
    }

    private static String createPassword(char[] pwd) {
        //creates password if length is greater than or equal to four
        String data;
        data = new String(pwd);
                if (data.isBlank() || data.isEmpty())
                    return "BLANK PASSWORD";
                int pwdLength = data.length();
                if (pwdLength < MIN_NUM_OF_CHARS)
                    return "PASSWORD DOES NOT MEET MIN LENGTH OF FOUR. ";
        saltStringValForDb=storeNewSaltValueInDb(); // stores new encoded salt value in Database
        salt=valueToAddToPassHash(saltStringValForDb); // gets decoded salt value to run password hash before checking
        return generateHash(pwd,PassWrdHash.salt);
        }

    private static String generateHash(char[] pwd, byte[] salt){
        // generates the password hash to check

        String data = new String(pwd);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256"); // uses SHA-256 hashing algorithm
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        digest.reset(); // insures no leftover data in algorithm hash
        digest.update(salt); // adds salt to hash
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.US_ASCII)); // adds password and performs hash
        return bytesToStringHex(hash); // translates byte data to hex to String
    }

    private static String bytesToStringHex(byte[] bytes){ // translates byte data to hex

        char[] hexChars = new char[bytes.length*2];
        for(int j =0;j< bytes.length; j++){
            int v = bytes[j] & 0xFF ;
            hexChars[j*2] = hexArray[v >>> 4];
            hexChars[j* 2+1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static String getSalt() { // gets encoded salt value
        return saltStringValForDb;
    }

    private static String encode(String str){ // encodes String to base 64

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encoded = encoder.encode(str.getBytes(StandardCharsets.US_ASCII));
        return new String(encoded);
    }

    private static String generateString(){
        // generates random UUID String without the "-" that is 25 chars long to make sure it fits within the Database type length constraints
        String uuid = UUID.randomUUID().toString();
        uuid =uuid.replace("-","");
        if(uuid.length()>50){
            uuid=uuid.substring(0,uuid.charAt(25));
        }
        return uuid;
    }

    private static String storeNewSaltValueInDb(){ // encodes salt value to be stored in Database
        String regular = generateString();
        return encode(regular);
    }
    private static byte[] valueToAddToPassHash(String dbHeldSaltValue){
        // decodes salt value to be added to password hash to check against password input
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(dbHeldSaltValue.getBytes(StandardCharsets.US_ASCII));
    }
}
