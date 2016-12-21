angular.module('FundFinder')

.service('ActivitiesService', function($http) {
	this.getEntities = function() {
		return $http.get('/api/v1/activities');
	};
});