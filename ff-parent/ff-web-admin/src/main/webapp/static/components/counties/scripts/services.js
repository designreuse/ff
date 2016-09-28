angular.module('FundFinder')

.service('CountiesService', function($http) {
	this.saveEntity = function(resource) {
		return $http.post('/api/v1/counties', resource);
	};
	this.deleteEntity = function(id) {
		return $http.delete('/api/v1/counties/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/counties/' + id);
	};
	this.getEntities = function(id) {
		return $http.get('/api/v1/counties');
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/counties/page', resource);
	};
});