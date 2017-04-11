//find the oldest friend for each user who has a friend. For simplicity, use only year of birth to determine age, if there is a tie, use the one with smallest user_id
//return a javascript object : key is the user_id and the value is the oldest_friend id
//You may find query 2 and query 3 helpful. You can create selections if you want. Do not modify users collection.
//
//You should return something like this:(order does not matter)
//{user1:userx1, user2:userx2, user3:userx3,...}

function oldest_friend(dbname){
  db = db.getSiblingDB(dbname)

  //implementation goes here
  //return an javascript object described above


  var uwfriends = db.users.find();
  var obj={};
  db.flat_users.aggregate([
  		{$group :{_id:"$friends",f_uid:{$push:"$user_id"}}},
  		{$out:"sf"}
  	]);

  var findyob =function(userid){
	var aa = db.users.find({"user_id":userid},{YOB:1,_id:0});
        
        	var thisuser=aa.next();
		//print(userid);
		//print("****");
		//print(thisuser.YOB);
        	
        	return thisuser.YOB;
		
       
    }

  

  while(uwfriends.hasNext()){
  	var thisuser=uwfriends.next();
  	//var oldestobj=db.users.find({"user_id":thisuser.friends[0]},{YOB:1,_id:0});
	var oldest_id=0;

  	var oldest=5000;

  	for(var i=0;i<thisuser.friends.length;i++){
  		var thisyob = findyob(thisuser.friends[i]);
	//print(thisyob);
  		if(thisyob<oldest){
  			oldest=thisyob;
  			oldest_id=thisuser.friends[i];
  		}
  		else if(thisyob===oldest){
  			if(oldest_id>thisuser.friends[i]){
  				oldest_id=thisuser.friends[i];
  			}
  		}
  	}
  	var smallerfriends=db.sf.find();
  	while(smallerfriends.hasNext()){
  		var thisfriend=smallerfriends.next();
  		if(thisfriend._id===thisuser.user_id){
  			for(var i=0;i<thisfriend.f_uid.length;i++){
  				var thisyob=findyob(thisfriend.f_uid[i]);
				
  				if(thisyob<oldest){
  			oldest=thisyob;
  			oldest_id=thisfriend.f_uid[i];
  		}
  		else if(thisyob===oldest){
  			if(oldest_id>thisfriend.f_uid[i]){
  				oldest_id=thisfriend.f_uid[i];
  			}
  			}		
  	}
  	}
  }
  	obj[thisuser.user_id]=oldest_id;
  }
  return obj;

}
