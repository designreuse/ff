angular.module('FundFinder')

.service('Subdivisions1Service', function($http) {
	this.getEntities = function() {
		return $http.get('/api/v1/subdivisions1');
	};
});