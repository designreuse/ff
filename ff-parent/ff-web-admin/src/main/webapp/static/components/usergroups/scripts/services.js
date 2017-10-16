angular.module('FundFinder')

.service('UserGroupsService', function($http) {
	this.activateEntity = function(id) {
		return $http.get('/api/v1/usergroups/' + id + "/activate");
	};
	this.deactivateEntity = function(id) {
		return $http.get('/api/v1/usergroups/' + id + "/deactivate");
	};
	this.saveEntity = function(resource) {
		return $http.post('/api/v1/usergroups', resource);
	};
	this.deleteEntity = function(id) {
		return $http.get('/api/v1/usergroups/delete/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/usergroups/' + id);
	};
	this.getEntities = function() {
		return $http.get('/api/v1/usergroups');
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/usergroups/page', resource);
	};
});