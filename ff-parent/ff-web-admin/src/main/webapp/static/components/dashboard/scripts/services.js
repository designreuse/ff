angular.module('FundFinder')

.service('DashboardService', function($http) {
	this.getData = function() {
		return $http.get('/api/v1/dashboard');
	};
	this.getChartData = function(period, type) {
		return $http.get('/api/v1/dashboard/chart/' + period + "/" + type);
	};
});