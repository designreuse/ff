angular.module('FundFinder')

.service('UsersService', function($http) {
	this.activateEntity = function(id) {
		return $http.get('/api/v1/users/' + id + "/activate");
	};
	this.deactivateEntity = function(id) {
		return $http.get('/api/v1/users/' + id + "/deactivate");
	};
	this.deleteEntity = function(id) {
		return $http.delete('/api/v1/users/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/users/' + id);
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/users/page', resource);
	};
});