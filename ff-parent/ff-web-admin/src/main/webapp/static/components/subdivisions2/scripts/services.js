angular.module('FundFinder')

.service('Subdivisions2Service', function($http) {
	this.saveEntity = function(resource) {
		return $http.post('/api/v1/subdivisions2', resource);
	};
	this.deleteEntity = function(id) {
		return $http.get('/api/v1/subdivisions2/delete/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/subdivisions2/' + id);
	};
	this.getEntities = function() {
		return $http.get('/api/v1/subdivisions2');
	};
	this.getEntities4Subdivisions1 = function(subdivision1Ids) {
		return $http.get('/api/v1/subdivisions2?subdivision1Ids=' + subdivision1Ids);
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/subdivisions2/page', resource);
	};
});