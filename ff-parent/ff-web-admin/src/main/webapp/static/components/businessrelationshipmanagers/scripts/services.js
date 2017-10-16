angular.module('FundFinder')

.service('BusinessRelationshipManagerService', function($http) {
	this.saveEntity = function(resource) {
		return $http.post('/api/v1/businessrelationshipmanagers', resource);
	};
	this.deleteEntity = function(id) {
		return $http.get('/api/v1/businessrelationshipmanagers/delete/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/businessrelationshipmanagers/' + id);
	};
	this.getEntities = function() {
		return $http.get('/api/v1/businessrelationshipmanagers');
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/businessrelationshipmanagers/page', resource);
	};
});