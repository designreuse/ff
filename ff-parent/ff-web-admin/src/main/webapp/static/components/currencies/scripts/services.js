angular.module('FundFinder')

.service('CurrenciesService', function($http) {
	this.getEntities = function() {
		return $http.get('/api/v1/currencies');
	};
});