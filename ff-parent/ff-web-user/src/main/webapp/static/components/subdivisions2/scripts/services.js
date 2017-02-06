angular.module('FundFinder')

.service('Subdivisions2Service', function($http) {
	this.getEntities = function() {
		return $http.get('/api/v1/subdivisions2');
	};
	this.getEntities4Subdivision1 = function(subdivision1Id) {
		return $http.get('/api/v1/subdivisions2/' + subdivision1Id);
	};
});