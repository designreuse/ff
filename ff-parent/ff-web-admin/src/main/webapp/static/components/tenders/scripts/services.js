angular.module('FundFinder')

.service('TendersService', function($http) {
	this.activateEntity = function(id) {
		return $http.get('/api/v1/tenders/' + id + "/activate");
	};
	this.deactivateEntity = function(id) {
		return $http.get('/api/v1/tenders/' + id + "/deactivate");
	};
	this.saveEntity = function(resource) {
		return $http.post('/api/v1/tenders', resource);
	};
	this.deleteEntity = function(id) {
		return $http.delete('/api/v1/tenders/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/tenders/' + id);
	};
	this.getEntities = function() {
		return $http.get('/api/v1/tenders');
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/tenders/page', resource);
	};
	this.sendEmail = function(resource) {
		return $http.post('/api/v1/tenders/email', resource);
	};
	this.findMatchingUsers = function(id) {
		return $http.get('/api/v1/tenders/' + id + "/users");
	};
	this.exportTenders = function() {
		return $http.get('/api/v1/tenders/export');
	};
	this.exportTender = function(id) {
		return $http.get('/api/v1/tenders/export/' + id);
	};
});