angular.module('FundFinder')

.service('OrganizationalUnitsService', function($http) {
	this.saveEntity = function(resource) {
		return $http.post('/api/v1/organizationalunits', resource);
	};
	this.deleteEntity = function(id) {
		return $http.delete('/api/v1/organizationalunits/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/organizationalunits/' + id);
	};
	this.getEntities = function() {
		return $http.get('/api/v1/organizationalunits');
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/organizationalunits/page', resource);
	};
	this.exportData = function() {
		return $http.get('/api/v1/organizationalunits/export');
	};
});