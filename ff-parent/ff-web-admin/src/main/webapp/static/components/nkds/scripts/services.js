angular.module('FundFinder')

.service('NkdsService', function($http) {
	this.saveEntity = function(resource) {
		return $http.post('/api/v1/nkds', resource);
	};
	this.deleteEntity = function(id) {
		return $http.delete('/api/v1/nkds/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/nkds/' + id);
	};
	this.getEntities = function(id) {
		return $http.get('/api/v1/nkds');
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/nkds/page', resource);
	};
});