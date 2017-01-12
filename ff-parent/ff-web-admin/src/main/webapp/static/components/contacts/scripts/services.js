angular.module('FundFinder')

.service('ContactsService', function($http) {
	this.deleteEntity = function(id) {
		return $http.delete('/api/v1/contacts/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/contacts/' + id);
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/contacts/page', resource);
	};
});