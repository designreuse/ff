angular.module('FundFinder')

.service('EmailsService', function($http) {
	this.getEntity = function(id) {
		return $http.get('/api/v1/emails/' + id);
	};
});