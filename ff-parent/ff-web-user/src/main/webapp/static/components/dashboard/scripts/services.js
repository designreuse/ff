angular.module('FundFinder')

.service('DashboardService', function($http) {
	this.getData = function() {
		return $http.get('/api/v1/dashboard');
	};
	this.showSyncDataWarningOrNot = function() {
		return $http.get('/api/v1/company/showSyncDataWarningOrNot');
	};
	this.getHideSyncDataWarning = function() {
		return $http.get('/api/v1/company/getHideSyncDataWarning');
	};
	this.syncCompanyData = function(option, showSyncDataWarningFlag) {
		return $http.get('/api/v1/company/syncData/' + option + '/' + showSyncDataWarningFlag);
	};
	this.getChartDetails = function(period) {
		return $http.get('/api/v1/dashboard/getChartDetails?period=' + period);
	};
});