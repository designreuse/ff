angular.module('FundFinder')

.service('ArticlesService', function($http) {
	this.activateEntity = function(id) {
		return $http.get('/api/v1/articles/' + id + "/activate");
	};
	this.deactivateEntity = function(id) {
		return $http.get('/api/v1/articles/' + id + "/deactivate");
	};
	this.saveEntity = function(resource) {
		return $http.post('/api/v1/articles', resource);
	};
	this.deleteEntity = function(id) {
		return $http.delete('/api/v1/articles/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/articles/' + id);
	};
	this.getEntities = function(id) {
		return $http.get('/api/v1/articles');
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/articles/page', resource);
	};
});