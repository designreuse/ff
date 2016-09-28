angular.module('FundFinder')
	
	.filter('getSimpleClassName', function() { 
		return function(input) {
			if (input) {
				return input.substring(input.lastIndexOf(".") + 1);
			}
			return input;
		} 
	});