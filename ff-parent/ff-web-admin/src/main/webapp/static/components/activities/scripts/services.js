angular.module('FundFinder')

.service('ActivitiesService', function($http) {
	this.saveEntity = function(resource) {
		return $http.post('/api/v1/activities', resource);
	};
	this.deleteEntity = function(id) {
		return $http.delete('/api/v1/activities/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/activities/' + id);
	};
	this.getEntities = function() {
		return $http.get('/api/v1/activities');
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/activities/page', resource);
	};
	this.exportData = function() {
		return $http.get('/api/v1/activities/export');
	};
});