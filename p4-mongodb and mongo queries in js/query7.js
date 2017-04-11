// query 7: For each city, find the average friend count per user in that city using
// MapReduce. Using the same terminology as in query6, we are asking
// you to write the mapper, reducer and finalizer to find the average
// friend count for each city.


var city_average_friendcount_mapper = function() {
  // implement the Map function of average friend count
  
  	 var key = this.hometown.city;
  		var values={count:1,qty:this.friends.length};
  

  emit(key,values);
};

var city_average_friendcount_reducer = function(key, values) {
  // implement the reduce function of average friend count
reduceVal={count:0,qty:0};
for(var idx=0;idx<values.length;idx++){
	reduceVal.count+=values[idx].count;
	reduceVal.qty+=values[idx].qty;
}
  
  return reduceVal;
};

var city_average_friendcount_finalizer = function(key, reduceVal) {
  // We've implemented a simple forwarding finalize function. This implementation 
  // is naive: it just forwards the reduceVal to the output collection.
  // Feel free to change it if needed. However, please keep this unchanged:
  // the var ret should be the average friend count per user of each city.

  var ret=reduceVal.qty/reduceVal.count;
  return ret;
}
