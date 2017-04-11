// query6 : Find the Average friend count per user for the Users
// collection.  We define the `friend count` as the number of friends
// of a user. The average friend count per user is the average `friend
// count` towards a collection of users. In this function we ask you
// to find the `average friend count per user` for the users in the
// Users collection.
//
// Return a decimal variable as the average user friend count of all users
// in the Users collection.



function find_average_friendcount(dbname){
  db = db.getSiblingDB(dbname)
  // Implementation goes here
  var friend_sum=0;
  var num=0;
  /*var userinfo=db.users.find();
  while (userinfo.hasNext()){
  	var thisuser=userinfo.next();
  	num+=1;
  	friend_sum+=thisuser.friends.length;
  }*/
  
  db.users.find().forEach(
  	function(friend){
  		friend_sum+=friend.friends.length;
  		num+=1;
  	}
  	)
var friend_avg= friend_sum/num;
return friend_avg;

}
