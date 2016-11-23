angular.module('FundFinder')

.service('PermissionsService', function($http) {
	this.getEntities = function() {
		return $http.get('/api/v1/permissions');
	};
});