angular.module('FundFinder')

.service('TendersService', function($http) {
	this.find = function(id) {
		return $http.get('/api/v1/tenders/' + id);
	};
	this.findAll = function() {
		return $http.get('/api/v1/tenders');
	};
	this.findAllDemo = function(resource) {
		return $http.post('/api/v1/tenders/demo', resource);
	};
});