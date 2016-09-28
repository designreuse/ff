angular.module('FundFinder')

.service('CitiesService', function($http) {
	this.getEntities = function(id) {
		return $http.get('/api/v1/cities');
	};
});