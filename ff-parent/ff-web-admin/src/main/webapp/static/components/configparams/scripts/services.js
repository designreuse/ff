angular.module('FundFinder')

.service('ConfigParamsService', function($http) {
	this.saveEntities = function(resource) {
		return $http.post('/api/v1/configparams', resource);
	};
	this.getEntities = function() {
		return $http.get('/api/v1/configparams');
	};
});