angular.module('FundFinder')

.service('StatisticsService', function($http) {
	this.getCompaniesByCounties = function() {
		return $http.get('/api/v1/statistics/companiesByCounties');
	};
	this.getCompaniesByInvestments = function() {
		return $http.get('/api/v1/statistics/companiesByInvestments');
	};
	this.getCompaniesByRevenues = function() {
		return $http.get('/api/v1/statistics/companiesByRevenues');
	};
	this.getCompaniesBySectors = function() {
		return $http.get('/api/v1/statistics/companiesBySectors');
	};
});