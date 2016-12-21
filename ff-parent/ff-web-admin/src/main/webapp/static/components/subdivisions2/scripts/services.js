angular.module('FundFinder')

.service('Subdivisions2Service', function($http) {
	this.saveEntity = function(resource) {
		return $http.post('/api/v1/subdivisions2', resource);
	};
	this.deleteEntity = function(id) {
		return $http.delete('/api/v1/subdivisions2/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/subdivisions2/' + id);
	};
	this.getEntities = function() {
		return $http.get('/api/v1/subdivisions2');
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/subdivisions2/page', resource);
	};
});