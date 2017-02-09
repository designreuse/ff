angular.module('FundFinder')

.service('ArticlesService', function($http) {
	this.find = function(id) {
		return $http.get('/api/v1/articles/' + id);
	};
	this.findAll = function() {
		return $http.get('/api/v1/articles');
	};
	this.findLatest = function() {
		return $http.get('/api/v1/articles/latest');
	};
});