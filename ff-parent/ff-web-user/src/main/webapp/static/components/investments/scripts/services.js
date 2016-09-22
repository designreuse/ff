angular.module('FundFinder')

.service('InvestmentsService', function($http) {
	this.findAll = function() {
		return $http.get('/api/v1/investments');
	};
	this.save = function(resource) {
		return $http.post('/api/v1/investments', resource);
	};
});