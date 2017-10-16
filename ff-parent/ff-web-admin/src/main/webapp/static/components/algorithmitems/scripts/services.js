angular.module('FundFinder')

.service('AlgorithmItemsService', function($http) {
	this.activateEntity = function(id) {
		return $http.get('/api/v1/algorithmitems/' + id + "/activate");
	};
	this.deactivateEntity = function(id) {
		return $http.get('/api/v1/algorithmitems/' + id + "/deactivate");
	};
	this.saveEntity = function(resource) {
		return $http.post('/api/v1/algorithmitems', resource);
	};
	this.deleteEntity = function(id) {
		return $http.get('/api/v1/algorithmitems/delete/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/algorithmitems/' + id);
	};
	this.getEntities = function(id) {
		return $http.get('/api/v1/algorithmitems');
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/algorithmitems/page', resource);
	};
	this.exportData = function() {
		return $http.get('/api/v1/algorithmitems/export');
	};
});