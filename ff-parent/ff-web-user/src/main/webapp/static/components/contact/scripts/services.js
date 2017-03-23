angular.module('FundFinder')

.service('ContactService', function($http) {
	this.getLocations = function() {
		return $http.get('/api/v1/contact/locations');
	};
	this.get = function() {
		return $http.get('/api/v1/contact');
	};
	this.set = function(resource) {
		return $http.post('/api/v1/contact', resource);
	};
});