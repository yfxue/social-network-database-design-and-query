package project2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyFakebookOracle extends FakebookOracle {

    static String prefix = "tajik.";

    // You must use the following variable as the JDBC connection
    Connection oracleConnection = null;

    // You must refer to the following variables for the corresponding tables in your database
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


    // DO NOT modify this constructor
    public MyFakebookOracle(String dataType, Connection c) {
        super();
        oracleConnection = c;
        // You will use the following tables in your Java code
        cityTableName = prefix + dataType + "_CITIES";
        userTableName = prefix + dataType + "_USERS";
        friendsTableName = prefix + dataType + "_FRIENDS";
        currentCityTableName = prefix + dataType + "_USER_CURRENT_CITY";
        hometownCityTableName = prefix + dataType + "_USER_HOMETOWN_CITY";
        programTableName = prefix + dataType + "_PROGRAMS";
        educationTableName = prefix + dataType + "_EDUCATION";
        eventTableName = prefix + dataType + "_USER_EVENTS";
        albumTableName = prefix + dataType + "_ALBUMS";
        photoTableName = prefix + dataType + "_PHOTOS";
        tagTableName = prefix + dataType + "_TAGS";
    }


    @Override
    // ***** Query 0 *****
    // This query is given to your for free;
    // You can use it as an example to help you write your own code
    //
    public void findMonthOfBirthInfo() {

        // Scrollable result set allows us to read forward (using next())
        // and also backward.
        // This is needed here to support the user of isFirst() and isLast() methods,
        // but in many cases you will not need it.
        // To create a "normal" (unscrollable) statement, you would simply call
        // Statement stmt = oracleConnection.createStatement();
        //
        try (Statement stmt =
                     oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY)) {

            // For each month, find the number of users born that month
            // Sort them in descending order of count
            ResultSet rst = stmt.executeQuery("select count(*), month_of_birth from " +
                    userTableName +
                    " where month_of_birth is not null group by month_of_birth order by 1 desc");

            this.monthOfMostUsers = 0;
            this.monthOfLeastUsers = 0;
            this.totalUsersWithMonthOfBirth = 0;

            // Get the month with most users, and the month with least users.
            // (Notice that this only considers months for which the number of users is > 0)
            // Also, count how many total users have listed month of birth (i.e., month_of_birth not null)
            //
            while (rst.next()) {
                int count = rst.getInt(1);
                int month = rst.getInt(2);
                if (rst.isFirst())
                    this.monthOfMostUsers = month;
                if (rst.isLast())
                    this.monthOfLeastUsers = month;
                this.totalUsersWithMonthOfBirth += count;
            }

            // Get the names of users born in the "most" month
            rst = stmt.executeQuery("select user_id, first_name, last_name from " +
                    userTableName + " where month_of_birth=" + this.monthOfMostUsers);
            while (rst.next()) {
                Long uid = rst.getLong(1);
                String firstName = rst.getString(2);
                String lastName = rst.getString(3);
                this.usersInMonthOfMost.add(new UserInfo(uid, firstName, lastName));
            }

            // Get the names of users born in the "least" month
            rst = stmt.executeQuery("select first_name, last_name, user_id from " +
                    userTableName + " where month_of_birth=" + this.monthOfLeastUsers);
            while (rst.next()) {
                String firstName = rst.getString(1);
                String lastName = rst.getString(2);
                Long uid = rst.getLong(3);
                this.usersInMonthOfLeast.add(new UserInfo(uid, firstName, lastName));
            }

            // Close statement and result set
            rst.close();
            stmt.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    @Override
    // ***** Query 1 *****
    // Find information about users' names:
    // (1) The longest first name (if there is a tie, include all in result)
    // (2) The shortest first name (if there is a tie, include all in result)
    // (3) The most common first name, and the number of times it appears (if there
    //      is a tie, include all in result)
    //
    public void findNameInfo() { 
    	// Query1
        // Find the following information from your database and store the information as shown
    	try (Statement stmt =
                oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY)) {
    		//find first_name and its length 
    		//with the descending order of first name's length
    		ResultSet rst = stmt.executeQuery("select distinct first_name, length(first_name) from " +
                    userTableName +
                    " where first_name is not null order by length(first_name) desc");

    		int maxlength=0;
    	    int minlength=0;
    	    //get first name's max length and minimum length
    		while (rst.next()) {
                 if (rst.isFirst())
                     maxlength=rst.getInt(2);
                 if (rst.isLast())
                     minlength=rst.getInt(2);
                 }
    		//find first_name and its length 
    		//with the descending order of first name's length
    		rst = stmt.executeQuery("select distinct first_name, length(first_name) from " +
                     userTableName +
                     " where first_name is not null order by length(first_name) desc");
    		//get longest first name and shortest first name 
            while(rst.next()){           	
                 if(rst.getInt(2)==maxlength)
                	 this.longestFirstNames.add(rst.getString(1));
                 if(rst.getInt(2)==minlength)
                	 this.shortestFirstNames.add(rst.getString(1));    
             }
             
            this.mostCommonFirstNamesCount = 0;
            int count=0;
            //get first name and its appear times
            rst = stmt.executeQuery("select count(*) , first_name from " +
             userTableName +
             " where first_name is not null group by first_name order by 1 desc");
            //find the largest appear time
            while (rst.next()){
    	 
            	if(rst.isFirst()){
            		count=rst.getInt(1);
            		this.mostCommonFirstNamesCount = count;
            		}
            }
            //get first name and its appear times
            rst = stmt.executeQuery("select count(*) , first_name from " +
            		userTableName +
            		" where first_name is not null group by first_name order by 1 desc");
    	   //find first name with largest appear time
            while (rst.next()){
            	int num = rst.getInt(1);
            	if(num == count){
            		count=rst.getInt(1);
            		this.mostCommonFirstNames.add(rst.getString(2));
            	}
            }
     
        // Close statement and result set
        rst.close();
        stmt.close();
    } catch (SQLException err) {
        System.err.println(err.getMessage());
    }
}

    @Override
    // ***** Query 2 *****
    // Find the user(s) who have no friends in the network
    //
    // Be careful on this query!
    // Remember that if two users are friends, the friends table
    // only contains the pair of user ids once, subject to
    // the constraint that user1_id < user2_id
    //
    public void lonelyUsers() {
    	try (Statement stmt =
                oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY)) {
       // Find users whose user_ids are not in the friends table      
       ResultSet rst = stmt.executeQuery(
    		   "select user_id, first_name, last_name from " 
                + userTableName
               +" minus "
               +"select f.user1_id, u.first_name, u.last_name from " 
               + friendsTableName +" f, "
               + userTableName + " u "
               +" where f.user1_id=u.user_id " 
               + "minus"
               +" select f.user2_id, u.first_name, u.last_name from "
               + friendsTableName +" f, " 
               + userTableName + " u " +
               " where f.user2_id=u.user_id");
        // Find the following information from your database and store the information as shown
       while (rst.next()){
    	   Long userid=rst.getLong(1);
    	   String firstname=rst.getString(2);
    	   String lastname=rst.getString(3);
        this.lonelyUsers.add(new UserInfo(userid, firstname, lastname));
       }
        rst.close();
        stmt.close();
    } catch (SQLException err) {
        System.err.println(err.getMessage());
    }
}
    

    @Override
    // ***** Query 3 *****
    // Find the users who do not live in their hometowns
    // (I.e., current_city != hometown_city)
    //
    public void liveAwayFromHome() throws SQLException {
    	try (Statement stmt =
                 oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                         ResultSet.CONCUR_READ_ONLY)) {
    		//find id,first name,last name of users who do not live in their home town
    		ResultSet rst = stmt.executeQuery("select u.user_id,u.first_name,u.last_name from " +
    				userTableName +" u,"+currentCityTableName +" c,"+hometownCityTableName+
    				" h where u.user_id = c.user_id and u.user_id = h.user_id and c.current_city_id <> h.hometown_city_id");
    		//get id,first name,last name of users who do not live in their home town
    		while (rst.next()) {
    			Long id = rst.getLong(1);
            	String first = rst.getString(2);
            	String last = rst.getString(3);
            	this.liveAwayFromHome.add(new UserInfo(id, first, last));

    		}

    		rst.close();
    		stmt.close();
    	} catch (SQLException err) {
        System.err.println(err.getMessage());
    	}
        //this.liveAwayFromHome.add(new UserInfo(11L, "Heather", "Movalot"));
    }
    
    @Override
    // **** Query 4 ****
    // Find the top-n photos based on the number of tagged users
    // If there are ties, choose the photo with the smaller numeric PhotoID first
    //
    public void findPhotosWithMostTags(int n) {
    	try (Statement stmt =
                oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY)) {
 //First sort the photos according to the number of tags they have (descending) and their tag_photo_id (ascending)
 //select the top n rows and find their info accordingly in photos and albums tables
       ResultSet rst = stmt.executeQuery("select a.album_id, a.album_name, p.photo_id, p.photo_caption, p.photo_link from " 
    		   							+ tagTableName + " t, "
    		   							+ photoTableName +" p, "
    		   							+ albumTableName + " a, "
    		   							     + "(select * from "
    		   							         + "(select tag_photo_id, count(*) from "
    		   							         + tagTableName 
    		   							         + " group by tag_photo_id "
    		   							         + "order by count(*) desc, tag_photo_id asc )"
    		   							     + " where rownum <=" + n + ") mt "
    		   							     +" where a.album_id=p.album_id "
    		   							     +" and p.photo_id = t.tag_photo_id "
    		   							     +" and t.tag_photo_id = mt.tag_photo_id ");
  
       while (rst.next()){
    	   String photoId = rst.getString(3);
    	   String albumId = rst.getString(1);
    	   String albumName = rst.getString(2);
    	   String photoCaption = rst.getString(4);
    	   String photoLink = rst.getString(5);
    	   PhotoInfo p = new PhotoInfo(photoId, albumId, albumName, photoCaption, photoLink);
    	   TaggedPhotoInfo tp = new TaggedPhotoInfo(p); 
       	try (Statement stmt2 =
                oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY)) {

// stmt2 & rst2 are used to find the user information who are tagged in the top n photos
       			ResultSet rst2 = stmt2.executeQuery("select u2.user_id, u2.first_name, u2.last_name from " 
       												+ userTableName + " u2,"
       												+ tagTableName + " t2 "
       												+ "where u2.user_id = t2.tag_subject_id"
       												+" and t2.tag_photo_id = "+photoId);
       					while(rst2.next()){
       						Long userid=rst2.getLong(1);
       						String firstname=rst2.getString(2);
       						String lastname=rst2.getString(3);
       						tp.addTaggedUser(new UserInfo(userid, firstname, lastname));
       					}
       					rst2.close();
       					stmt2.close();

       		}
           this.photosWithMostTags.add(tp);
       }
       
        rst.close();
        stmt.close();
    } catch (SQLException err) {
        System.err.println(err.getMessage());
    }
}

    @Override
    // **** Query 5 ****
    // Find suggested "match pairs" of users, using the following criteria:
    // (1) One of the users is female, and the other is male
    // (2) Their age difference is within "yearDiff"
    // (3) They are not friends with one another
    // (4) They should be tagged together in at least one photo
    //
    // You should return up to n "match pairs"
    // If there are more than n match pairs, you should break ties as follows:
    // (i) First choose the pairs with the largest number of shared photos
    // (ii) If there are still ties, choose the pair with the smaller user_id for the female
    // (iii) If there are still ties, choose the pair with the smaller user_id for the male
    //
    public void matchMaker(int n, int yearDiff) {
     	 try (Statement stmt =
                 oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                         ResultSet.CONCUR_READ_ONLY)) {
     		 //find up to n pair of users' corresponding id,name,year 
     	     // (1) One of the users is female, and the other is male
     	     // (2) Their age difference is within "yearDiff"
     	     // (3) They are not friends with one another
     	     // (4) They should be tagged together in at least one photo
     		 // If there are more than n match pairs, you should break ties as follows:
     	     // (i) First choose the pairs with the largest number of shared photos
     	     // (ii) If there are still ties, choose the pair with the smaller user_id for the female
     	     // (iii) If there are still ties, choose the pair with the smaller user_id for the male
     	     //
     		 ResultSet rst = stmt.executeQuery("select m_u.user_id,m_u.first_name,m_u.last_name,m_u.year_of_birth,f_u.user_id,f_u.first_name,f_u.last_name,f_u.year_of_birth "
     				 + " from "+userTableName+" m_u, "
     				 +userTableName+" f_u,"
     				 + " (select * from "
     				 + "(select m_u.user_id as user1,f_u.user_id as user2 "
     				 + "from "+userTableName+" m_u, "+userTableName+" f_u,"+tagTableName+" t, "+photoTableName+" p "
        			 + "where m_u.gender = 'male' "
        			 + "and f_u.gender = 'female' "
        			 + "and t.tag_photo_id = p.photo_id "
        			 + "and t.tag_subject_id = m_u.user_id "
        			 + "and t.tag_subject_id = f_u.user_id "
        			 + "and ABS(m_u.year_of_birth - f_u.year_of_birth) <= "+yearDiff
        		     + " group by (m_u.user_id,f_u.user_id) "
        			 + "order by count(*) desc,f_u.user_id asc,m_u.user_id asc) "
        			 + "where rownum <= "+n+") pair "
        			 + "where m_u.user_id = pair.user1 "
        			 + "and f_u.user_id = pair.user2 ");
     		 //get pair's id,first name,last name
     		 while (rst.next()) {
     			 Long m_id = rst.getLong(1);
     			 String m_first = rst.getString(2);
     			 String m_last = rst.getString(3);
     			 int m_year = rst.getInt(4);
     			 Long f_id = rst.getLong(5);
     			 String F_first = rst.getString(6);
     			 String F_last = rst.getString(7);
     			 int F_year = rst.getInt(8);
     			 MatchPair mp = new MatchPair(f_id, F_first, F_last,F_year, m_id, m_first, m_last, m_year);
     			 
     			 try (Statement stmt2 =
     					 oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY)) {

     	     		 //find a pair of user's corresponding tagged photo_id,album id ,album name photo caption,photo link
     				 ResultSet rst2 = stmt.executeQuery("select p.photo_id,p.album_id,a.album_name,p.photo_caption,p.photo_link "
     						 + "from "+tagTableName+" t, "+photoTableName+" p," + albumTableName +" a "
     						 + "where t.tag_subject_id =  "+f_id
     						 + " and t.tag_subject_id =  "+m_id
     						 + " and p.photo_id = t.tag_photo_id "
     						 + " and a.album_id = p.album_id");
     				 //add photo_id,album id ,album name photo caption,photo link to corresponding pairs
     				 while(rst2.next()){
     					 String pid  = rst2.getString(1);
     					 String aid  = rst2.getString(2);
     					 String aname  = rst2.getString(3);
     					 String pcap  = rst2.getString(4);
     					 String plink  = rst2.getString(5);
     					 mp.addSharedPhoto(new PhotoInfo(pid, aid, aname,pcap, plink));
     				 }  
     				 rst2.close();
     				 stmt2.close();
     			 }
     			 //add pairs to bestMatch
     			 this.bestMatches.add(mp);
     		 }

     		 rst.close();
     		 stmt.close();
     	 } catch (SQLException err) {
     		 System.err.println(err.getMessage());
     	 }

    }
    
 
    // **** Query 6 ****
    // Suggest users based on mutual friends
    //
    // Find the top n pairs of users in the database who have the most
    // common friends, but are not friends themselves.
    //
    // Your output will consist of a set of pairs (user1_id, user2_id)
    // No pair should appear in the result twice; you should always order the pairs so that
    // user1_id < user2_id
    //
    // If there are ties, you should give priority to the pair with the smaller user1_id.
    // If there are still ties, give priority to the pair with the smaller user2_id.
    //
    @Override
    public void suggestFriendsByMutualFriends(int n) {
    	try (Statement stmt =
                oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY)) {

    //First find pairs of users who have mutual friends (note that we should go through all the users in the friends table
    //	  which is done by using unions for each combination of user1_id and user2_id in f1 and f2)
    //Add constraint that user1_id<user2_id and select the pairs whose ids are not in the friends table
    // Sort the pairs by numbers of mutual friends (descending) as well as their ids (ascending)
    // Select the first n rows
       ResultSet rst = stmt.executeQuery("SELECT * from "
       									+ "(select mf.u1, mf.u2,  "
       									+ " u1.first_name as u1firstname, u1.last_name as u1lastname, "
       									+ "u2.first_name as u2firstname, u2.last_name as u2lastname from "
       									+ userTableName +" u1, "
       									+ userTableName +" u2, "
       								    	+" (select m.p1 as u1, m.p2 as u2 from "
       								        	+" (select distinct f1.user1_id as p1, "
       								        	+ "f2.user1_id as p2, "
       								        	+ "f2.user2_id as p3 from "
       								        	+ friendsTableName +" f1, " 
       								        	+ friendsTableName +" f2 "
       								        	+" where f1.user1_id<>f2.user1_id "
       								        	+ "and f1.user2_id=f2.user2_id "
       								        	+" union "
       								        	+ "select distinct f1.user1_id as p1, "
       								        	+ "f2.user2_id as p2, "
       								        	+" f2.user1_id as p3 from "
       								        	+ friendsTableName +" f1,"
       								        	+ friendsTableName +" f2 "
       								        	+" where f1.user2_id=f2.user1_id "
       								        	+ "and f1.user1_id <> f2.user2_id "
       								        	+" union "
       								        	+ "select distinct f1.user2_id as p1, "
       								        	+ "f2.user2_id as p2, "
       								        	+ "f1.user1_id as p3 from "
       								        	+ friendsTableName +" f1, "
       								        	+ friendsTableName +" f2"
       								        	+" where f1.user2_id<>f2.user2_id "
       								        	+ "and f1.user1_id=f2.user1_id "
       								        	+" order by p3) M "
       								    	+" where m.p1<m.p2 "
       								    	+ " and (m.p1, m.p2) not in "
       								            	+" (select distinct f.user1_id, f.user2_id from "
       								            	+ friendsTableName +" f )"
       									    +" group by m.p1, m.p2 "
       									    +" order by count(*) desc, m.p1 asc, m.p2 asc) mf "
       									+" where mf.u1=u1.user_id "
       									+" and mf.u2=u2.user_id) "
       									+ "where rownum<=" + n );
    	  	
       while(rst.next()){
        Long user1_id = rst.getLong(1);
        String user1FirstName = rst.getString(3);
        String user1LastName = rst.getString(4);
        Long user2_id = rst.getLong(2);
        String user2FirstName = rst.getString(5);
        String user2LastName = rst.getString(6);
        UsersPair p = new UsersPair(user1_id, user1FirstName, user1LastName, user2_id, user2FirstName, user2LastName);
 // the second query is to find the mutual friends of the pairs found above    
        try (Statement stmt2 =
                oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY)) {
        	ResultSet rst2 = stmt2.executeQuery("select u.user_id, u.first_name, u.last_name "
        			+ " from (( select f1.user2_id as mutual_friends_id from "
        			          + friendsTableName +" f1 "
        			          +" where f1.user1_id= "+ user1_id
        			          +" union "
        			          + "select f1.user1_id as mutual_friends_id from "
        			          + friendsTableName + " f1" 
        			          +" where f1.user2_id= "+ user1_id +")"
        			          +" intersect "
        			          +" ( select f1.user2_id as mutual_friends_id from "
        			          + friendsTableName +" f1 "
        			          +" where f1.user1_id= "+ user2_id
        			          +" union "
        			          + "select f1.user1_id as mutual_friends_id from "
        			          + friendsTableName + " f1 " 
        			          +" where f1.user2_id= "+ user2_id + ")) mutual, "
        			+ userTableName +" u"
        			+" where u.user_id=mutual.mutual_friends_id "
        			+" order by u.user_id asc");
            while(rst2.next()){
            	Long sharedid=rst2.getLong(1);
            	String sharedfirstname=rst2.getString(2);
            	String sharedlastname=rst2.getString(3);
    	   p.addSharedFriend(sharedid, sharedfirstname, sharedlastname);          
            }    
            rst2.close();
            stmt2.close();
        }
        this.suggestedUsersPairs.add(p);
       }
        rst.close();
        stmt.close();
    	}
       catch (SQLException err) {
        System.err.println(err.getMessage());
    }

    }  
    
    
    @Override
    // ***** Query 7 *****
    //
    // Find the name of the state with the most events, as well as the number of
    // events in that state.  If there is a tie, return the names of all of the (tied) states.
    //
    public void findEventStates() {
    	try (Statement stmt =
                oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY)) {
    // Select and sort the states by number of events hosted (descending)

       ResultSet rst = stmt.executeQuery("select state_name, count(*) as num "
    		   							+ "from "+ cityTableName+" c,"
    		   							+ eventTableName +" e "
    		   							+ "where c.city_id = e.EVENT_CITY_ID "
    		   							+ "group by state_name "
    		   							+ "order by num desc");
       // Get the state with the most events
       int num = 0;
       while (rst.next()) {
    	   if(rst.isFirst()){
    		   num = rst.getInt(2);
    		   this.eventCount = num;
    	   }
           String statename = rst.getString(1);
           int count = rst.getInt(2);
       // If there's a tie, list the state name also    
          if(count == num){
        	  this.popularStateNames.add(statename);
          }

       }

       rst.close();
       stmt.close();
   } catch (SQLException err) {
       System.err.println(err.getMessage());
   }

    }

  //@Override
    // ***** Query 8 *****
    // Given the ID of a user, find information about that
    // user's oldest friend and youngest friend
    //
    // If two users have exactly the same age, meaning that they were born
    // on the same day, then assume that the one with the larger user_id is older
    //
    public void findAgeInfo(Long user_id) {
    	try (Statement stmt =
                oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY)) {
    //Combine the friends tables so that each column contains all the user_ids who have a friend
    //Select and sort the user information by year, month, and day of birth (ascending), as well as user_id if there's a tie

       ResultSet rst = stmt.executeQuery("select u1.user_id,u1.first_name,u1.last_name "
    		   								+"from "+userTableName+" u1, "
    		   									+"(select f1.user1_id as user_id "
    		   									+"from "+friendsTableName+" f1 "
    		   									+"where f1.user2_id = "+user_id
    		   									+"union "
    		   									+"select f2.user2_id  as user_id "
    		   									+"from "+friendsTableName+" f2 "
    		   									+"where f2.user1_id = "+user_id+") u2 "
    		   								+"where u2.user_id = u1.user_id "
    		   								+"order by u1.year_of_birth asc,u1.month_of_birth asc,u1.day_of_birth asc,u1.user_id desc");
    //The first entry is the oldest friend and the last entry is the youngest
       while (rst.next()) {
    	   Long id = rst.getLong(1);
    	   String first = rst.getString(2);
    	   String last = rst.getString(3);
    	   if(rst.isFirst()){
    		   this.oldestFriend = new UserInfo(id, first, last);
    	   }
    	   if(rst.isLast()){
    	        this.youngestFriend = new UserInfo(id, first, last);
    	   }
       }

       rst.close();
       stmt.close();
   } catch (SQLException err) {
       System.err.println(err.getMessage());
   }

    }

    @Override
    //	 ***** Query 9 *****
    //
    // Find pairs of potential siblings.
    //
    // A pair of users are potential siblings if they have the same last name and hometown, if they are friends, and
    // if they are less than 10 years apart in age.  Pairs of siblings are returned with the lower user_id user first
    // on the line.  They are ordered based on the first user_id and in the event of a tie, the second user_id.
    //
    //
    public void findPotentialSiblings() {
    	try (Statement stmt =
                oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY)) {

    		//find id, first name ,last name of potential siblings 
    		//with the ascending order of first user's id,if there is a tie,the second user_id. 
    		ResultSet rst = stmt.executeQuery("select u1.user_id,u1.first_name,u1.last_name,u2.user_id,u2.first_name,u2.last_name"
    				+" from "+userTableName+" u1,"+userTableName+" u2, "+hometownCityTableName+" h1,"+hometownCityTableName+" h2, "+friendsTableName+" f"
    				+" where u1.user_id < u2.user_id"
    				+" and u1.user_id  = f.user1_id"
    		   		+" and u2.user_id = f.user2_id"
    		   		+" and u1.last_name = u2.last_name"
    		   		+" and u1.user_id = h1.user_id"
    		   		+" and u2.user_id = h2.user_id"
    		   		+" and h1.hometown_city_id = h2.hometown_city_id"
    		   		+" and ABS(u1.year_of_birth - u2.year_of_birth) < 10"
    		   		+" order by u1.user_id,u2.user_id");
    		//add id, first name ,last name of potential siblings to siblingInfo
    		while (rst.next()) {
    			Long user1_id = rst.getLong(1);
    			String user1FirstName =  rst.getString(2);
    			String user1LastName = rst.getString(3);
    			Long user2_id = rst.getLong(4);
    			String user2FirstName = rst.getString(5);
    			String user2LastName = rst.getString(6);
    			SiblingInfo s = new SiblingInfo(user1_id, user1FirstName, user1LastName, user2_id, user2FirstName, user2LastName);
    			this.siblings.add(s);
    		}

    		rst.close();
    		stmt.close();
    	} catch (SQLException err) {
    		System.err.println(err.getMessage());
    	}


    }

}
