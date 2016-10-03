angular.module('FundFinder')

.service('CountersService', function($http) {
	this.findAll = function() {
		return $http.get('/api/v1/counters');
	};
});