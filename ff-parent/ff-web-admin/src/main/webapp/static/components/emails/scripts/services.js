angular.module('FundFinder')

.service('EmailsService', function($http) {
	this.getEntity = function(id) {
		return $http.get('/api/v1/emails/' + id);
	};
	this.send = function(resource) {
		return $http.post('/api/v1/emails/send', resource);
	};
});