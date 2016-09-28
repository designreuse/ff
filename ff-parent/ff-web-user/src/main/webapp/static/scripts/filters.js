angular.module('FundFinder')
	
	.filter('getSimpleClassName', function() { 
		return function(input) {
			if (input) {
				return input.substring(input.lastIndexOf(".") + 1);
			}
			return input;
		} 
	})

	.filter('htmlToPlaintext', function() {
		return function(text) {
			return text ? String(text).replace(/<[^>]+>/gm, '') : '';
		};
	});