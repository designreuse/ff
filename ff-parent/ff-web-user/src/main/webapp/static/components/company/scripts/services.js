angular.module('FundFinder')

.service('CompanyService', function($http) {
	this.find = function() {
		return $http.get('/api/v1/company');
	};
	this.save = function(resource) {
		return $http.post('/api/v1/company', resource);
	};
});