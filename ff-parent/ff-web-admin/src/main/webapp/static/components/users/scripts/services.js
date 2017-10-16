angular.module('FundFinder')

.service('UsersService', function($http) {
	this.activateEntity = function(id) {
		return $http.get('/api/v1/users/' + id + "/activate");
	};
	this.deactivateEntity = function(id) {
		return $http.get('/api/v1/users/' + id + "/deactivate");
	};
	this.deleteEntity = function(id) {
		return $http.get('/api/v1/users/delete/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/users/' + id);
	};
	this.getEntities = function() {
		return $http.get('/api/v1/users');
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/users/page', resource);
	};
	this.sendEmail = function(resource) {
		return $http.post('/api/v1/users/email', resource);
	};
	this.setBusinessRelationshipManager = function(id, resource) {
		return $http.post('/api/v1/users/' + id + '/brm', resource);
	};
	this.gfiSync = function(resource) {
		return $http.post('/api/v1/users/gfi-sync', resource);
	};
});