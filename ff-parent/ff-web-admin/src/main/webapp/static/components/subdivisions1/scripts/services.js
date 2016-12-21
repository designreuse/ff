angular.module('FundFinder')

.service('Subdivisions1Service', function($http) {
	this.saveEntity = function(resource) {
		return $http.post('/api/v1/subdivisions1', resource);
	};
	this.deleteEntity = function(id) {
		return $http.delete('/api/v1/subdivisions1/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/subdivisions1/' + id);
	};
	this.getEntities = function() {
		return $http.get('/api/v1/subdivisions1');
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/subdivisions1/page', resource);
	};
});