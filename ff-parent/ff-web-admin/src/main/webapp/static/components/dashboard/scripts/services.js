angular.module('FundFinder')

.service('DashboardService', function($http) {
	this.getData = function() {
		return $http.get('/api/v1/dashboard');
	};
});