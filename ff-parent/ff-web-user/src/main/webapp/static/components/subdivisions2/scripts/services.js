angular.module('FundFinder')

.service('Subdivisions2Service', function($http) {
	this.getEntities = function() {
		return $http.get('/api/v1/subdivisions2');
	};
});