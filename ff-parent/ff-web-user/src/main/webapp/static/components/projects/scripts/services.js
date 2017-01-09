angular.module('FundFinder')

.service('ProjectsService', function($http) {
	this.findAll = function() {
		return $http.get('/api/v1/projects');
	};
	this.find = function(id) {
		return $http.get('/api/v1/projects/' + id);
	};
	this.save = function(resource) {
		return $http.post('/api/v1/projects', resource);
	};
	this.delete = function(id) {
		return $http.delete('/api/v1/projects/' + id);
	};
})

.service('InvestmentsService', function($http) {
	this.findAll = function() {
		return $http.get('/api/v1/investments');
	};
});