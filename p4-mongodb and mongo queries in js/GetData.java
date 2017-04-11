import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;
import java.util.Vector;



//json.simple 1.1
// import org.json.simple.JSONObject;
// import org.json.simple.JSONArray;

// Alternate implementation of JSON modules.
import org.json.JSONObject;
import org.json.JSONArray;

public class GetData{
	
    static String prefix = "tajik.";
	
    // You must use the following variable as the JDBC connection
    Connection oracleConnection = null;
	
    // You must refer to the following variables for the corresponding 
    // tables in your database

    String cityTableName = null;
    String userTableName = null;
    String friendsTableName = null;
    String currentCityTableName = null;
    String hometownCityTableName = null;
    String programTableName = null;
    String educationTableName = null;
    String eventTableName = null;
    String participantTableName = null;
    String albumTableName = null;
    String photoTableName = null;
    String coverPhotoTableName = null;
    String tagTableName = null;

    // This is the data structure to store all users' information
    // DO NOT change the name
    JSONArray users_info = new JSONArray();		// declare a new JSONArray

	
    // DO NOT modify this constructor
    public GetData(String u, Connection c) {
	super();
	String dataType = u;
	oracleConnection = c;
	// You will use the following tables in your Java code
	cityTableName = prefix+dataType+"_CITIES";
	userTableName = prefix+dataType+"_USERS";
	friendsTableName = prefix+dataType+"_FRIENDS";
	currentCityTableName = prefix+dataType+"_USER_CURRENT_CITY";
	hometownCityTableName = prefix+dataType+"_USER_HOMETOWN_CITY";
	programTableName = prefix+dataType+"_PROGRAMS";
	educationTableName = prefix+dataType+"_EDUCATION";
	eventTableName = prefix+dataType+"_USER_EVENTS";
	albumTableName = prefix+dataType+"_ALBUMS";
	photoTableName = prefix+dataType+"_PHOTOS";
	tagTableName = prefix+dataType+"_TAGS";
    }
	
	
	
	
    //implement this function

    @SuppressWarnings("unchecked")
    public JSONArray toJSON() throws SQLException{ 
		
	// Your implementation goes here....		
    	JSONArray users_info = new JSONArray();
    	try (Statement stmt =
                oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY)) {
    		ResultSet rst = stmt.executeQuery("SELECT DISTINCT U.USER_ID, U.FIRST_NAME, U.LAST_NAME, U.GENDER, "
                        + "U.YEAR_OF_BIRTH, U.MONTH_OF_BIRTH, U.DAY_OF_BIRTH, C.CITY_NAME, C.STATE_NAME, C.COUNTRY_NAME "
    				+" FROM " 
                        + userTableName + " U, " + hometownCityTableName + " H, " + cityTableName + " C " 
                    + " WHERE U.USER_ID = H.USER_ID AND C.CITY_ID=H.HOMETOWN_CITY_ID ");
    		while (rst.next()) {
    			JSONObject user = new JSONObject();
    			JSONObject hometown = new JSONObject();
    			String firstName = rst.getString(2);
                String lastName = rst.getString(3);
                Long uid = rst.getLong(1);
                String gender = rst.getString(4);
                int year = rst.getInt(5);
                int month = rst.getInt(6);
                int day=rst.getInt(7);
                String cityname=rst.getString(8);
                String statename=rst.getString(9);
                String countryname=rst.getString(10);
                hometown.put("state",statename);
                hometown.put("country",countryname);
                hometown.put("city",cityname);
                user.put("user_id",uid);
                user.put("first_name",firstName);
                user.put("last_name",lastName);
                user.put("gender",gender);
                user.put("YOB",year);
                user.put("MOB",month);
                user.put("DOB",day);
                user.put("hometown",hometown);
                JSONArray friends = new JSONArray();
                try (Statement stmt2 =
                        oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                ResultSet.CONCUR_READ_ONLY)) {
            		ResultSet rst2 = stmt2.executeQuery("SELECT DISTINCT F.USER1 FROM "
            				+ "(SELECT F1.USER1_ID AS USER1, F1.USER2_ID AS USER2 FROM "
            				+ friendsTableName +" F1 " 
            				+ " UNION "
            				+  " SELECT F2.USER2_ID AS USER1, F2.USER1_ID AS USER2 FROM "
            				+ friendsTableName + " F2 "	
            				+") F "
            				+" WHERE F.USER2 ="+ uid
            				+" AND F.USER1>F.USER2 ");    
            		while (rst2.next()) {
            			Long fuid= rst2.getLong(1);           		
            			friends.put(fuid);
            		}
            		rst2.close();
                    stmt2.close();
    		}
                user.put("friends",friends);
        		users_info.put(user);
                 
    		}
    		rst.close();
            stmt.close();  
    		
    	}
    	 
		
	// This is an example usage of JSONArray and JSONObject
	// The array contains a list of objects
	// All user information should be stored in the JSONArray object: users_info
	// You will need to DELETE this stuff. This is just an example.

	// A JSONObject is an unordered collection of name/value pairs. Add a few name/value pairs.
	//JSONObject test = new JSONObject();	// declare a new JSONObject
	// A JSONArray consists of multiple JSONObjects. 
	//JSONArray users_info = new JSONArray();

	//test.put("user_id", "testid");		// populate the JSONObject
	//test.put("first_name", "testname");

	//JSONObject test2 = new JSONObject();
	//test2.put("user_id", "test2id");
	//test2.put("first_name", "test2name");

	// users_info.add(test);			// add the JSONObject to JSONArray	
	// users_info.add(test2);			// add the JSONObject to JSONArray	

	// Use put method if using the alternate JSON modules.
	//users_info.put(test);		// add the JSONObject to JSONArray     
	//users_info.put(test2);		// add the JSONObject to JSONArray	
	return users_info;
    }

    // This outputs to a file "output.json"
    public void writeJSON(JSONArray users_info) {
	// DO NOT MODIFY this function
	try {
	    FileWriter file = new FileWriter(System.getProperty("user.dir")+"/output.json");
	    file.write(users_info.toString());
	    file.flush();
	    file.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}
		
    }
}

