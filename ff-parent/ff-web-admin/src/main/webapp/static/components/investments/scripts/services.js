angular.module('FundFinder')

.service('InvestmentsService', function($http) {
	this.activateEntity = function(id) {
		return $http.get('/api/v1/investments/' + id + "/activate");
	};
	this.deactivateEntity = function(id) {
		return $http.get('/api/v1/investments/' + id + "/deactivate");
	};
	this.saveEntity = function(resource) {
		return $http.post('/api/v1/investments', resource);
	};
	this.deleteEntity = function(id) {
		return $http.delete('/api/v1/investments/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/investments/' + id);
	};
	this.getEntities = function() {
		return $http.get('/api/v1/investments');
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/investments/page', resource);
	};
	this.getRevisions = function(id) {
		return $http.get('/api/v1/investments/' + id + "/revisions");
	};
});