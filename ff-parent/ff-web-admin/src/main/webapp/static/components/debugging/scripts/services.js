angular.module('FundFinder')

.service('DebuggingService', function($http) {
	this.debug = function(userId, tenderId) {
		return $http.get('/api/v1/debugging/' + userId + '/' + tenderId);
	};
});