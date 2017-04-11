
// query 4: find user pairs such that, one is male, second is female,
// their year difference is less than year_diff, and they live in same
// city and they are not friends with each other. Store each user_id
// pair as arrays and return an array of all the pairs. The order of
// pairs does not matter. Your answer will look something like the following:
// [
//      [userid1, userid2],
//      [userid1, userid3],
//      [userid4, userid2],
//      ...
//  ]
// In the above, userid1 and userid4 are males. userid2 and userid3 are females.
// Besides that, the above constraints are satisifed.
// userid is the field from the userinfo table. Do not use the _id field in that table.

  
function suggest_friends(year_diff, dbname) {
    db = db.getSiblingDB(dbname)
    var recfriends = new Array();

    //implementation goes here
    var male =db.users.find({"gender":"male"},{user_id:1,_id:0,friends:1,YOB:1,hometown:1});
    var found =function(friendslist,userid){
        for(var i=0;i<friendslist.length;i++){
            if (friendslist[i]==userid){
                return true;
            }
        }
        return false;
    }

    while(male.hasNext()){
    	var thismale=male.next();
        var female=db.users.find({"gender":"female"},{user_id:1,_id:0,friends:1,YOB:1,hometown:1});
    	while(female.hasNext()){
    		var thisfemale=female.next();
    		if(!found(thismale.friends,thisfemale.user_id)){
                if(!found(thisfemale.friends,thismale.user_id)){
                    if(Math.abs(thismale.YOB-thisfemale.YOB)<year_diff){
                        if(thismale.hometown.city===thisfemale.hometown.city){
                            var pairs=new Array(thismale.user_id,thisfemale.user_id);
                            recfriends.push(pairs);
                        }
                    }
                }
            }
    	}
    }

return recfriends;
  
    // Return an array of arrays.
}
