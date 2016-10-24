angular.module('FundFinder')

.service('ItemsService', function($http) {
	this.activateEntity = function(id) {
		return $http.get('/api/v1/items/' + id + "/activate");
	};
	this.deactivateEntity = function(id) {
		return $http.get('/api/v1/items/' + id + "/deactivate");
	};
	this.saveEntity = function(resource) {
		return $http.post('/api/v1/items', resource);
	};
	this.deleteEntity = function(id) {
		return $http.delete('/api/v1/items/' + id);
	};
	this.getEntity = function(entityType, id) {
		return $http.get('/api/v1/items/' + entityType + "/" + id);
	};
	this.getEntities = function(entityType) {
		return $http.get('/api/v1/items/' + entityType);
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/items/page', resource);
	};
	this.getMetaTags = function(entityType) {
		return $http.get('/api/v1/items/' + entityType + '/metatags');
	};
});