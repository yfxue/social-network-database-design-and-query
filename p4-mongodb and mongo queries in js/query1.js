//query1 : find users who live in the specified city. 
// Returns an array of user_ids.

function find_user(city, dbname){
    db = db.getSiblingDB(dbname);
    //implementation goes here
     var result=new Array();
     var mycursor=db.users.find({"hometown.city":city},{user_id:1,_id:0});
     while (mycursor.hasNext()){
     	var w=mycursor.next();
     	result.push(w.user_id);
     }
return result;

    // returns a Javascript array. See test.js for a partial correctness check.  
    // This will be  an array of integers. The order does not matter.                                                               

}
