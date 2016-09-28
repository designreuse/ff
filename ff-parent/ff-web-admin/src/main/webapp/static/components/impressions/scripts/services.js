angular.module('FundFinder')

.service('ImpressionsService', function($http) {
	this.getImpressionStatistics = function(entity, entityId, statisticsPeriod) {
		return $http.get('/api/v1/impressions/statistics/' + entity + "/" + entityId + "/" + statisticsPeriod);
	};
	this.getStatisticsPeriods = function() {
		return $http.get('/api/v1/impressions/statistics/periods');
	};
});