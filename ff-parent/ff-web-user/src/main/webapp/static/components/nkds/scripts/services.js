angular.module('FundFinder')

.service('NkdsService', function($http) {
	this.getEntities = function(id) {
		return $http.get('/api/v1/nkds');
	};
});