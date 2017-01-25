angular.module('FundFinder')

.service('StatisticsService', function($http) {
	this.getCompaniesByCounties = function() {
		return $http.get('/api/v1/statistics/companiesByCounties');
	};
	this.getCompaniesByRevenues = function() {
		return $http.get('/api/v1/statistics/companiesByRevenues');
	};
	this.getCompaniesBySize = function() {
		return $http.get('/api/v1/statistics/companiesBySize');
	};
	this.getInvestmentsByCounties = function() {
		return $http.get('/api/v1/statistics/investmentsByCounties');
	};
	this.getInvestmentsByActivities = function() {
		return $http.get('/api/v1/statistics/investmentsByActivities');
	};
});