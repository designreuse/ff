angular.module('FundFinder')

.service('CommonService', function($http) {
	this.validateProfile = function() {
		return $http.get('/api/v1/company/validate');
	};
});