angular.module('FundFinder')

.service('CitiesService', function($http) {
	this.saveEntity = function(resource) {
		return $http.post('/api/v1/cities', resource);
	};
	this.deleteEntity = function(id) {
		return $http.delete('/api/v1/cities/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/cities/' + id);
	};
	this.getEntities = function(id) {
		return $http.get('/api/v1/cities');
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/cities/page', resource);
	};
});