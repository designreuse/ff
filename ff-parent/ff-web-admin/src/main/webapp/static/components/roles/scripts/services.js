angular.module('FundFinder')

.service('RolesService', function($http) {
	this.saveEntity = function(resource) {
		return $http.post('/api/v1/roles', resource);
	};
	this.deleteEntity = function(id) {
		return $http.get('/api/v1/roles/delete/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/roles/' + id);
	};
	this.getEntities = function() {
		return $http.get('/api/v1/roles');
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/roles/page', resource);
	};
});