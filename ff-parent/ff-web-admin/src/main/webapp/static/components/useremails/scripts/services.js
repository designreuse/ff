angular.module('FundFinder')

.service('UserEmailsService', function($http) {
	this.getPage = function(resource) {
		return $http.post('/api/v1/useremails/page', resource);
	};
});