angular.module('FundFinder')

.service('SettingsService', function($http) {
	this.find = function() {
		return $http.get('/api/v1/settings');
	};
	this.save = function(resource) {
		return $http.post('/api/v1/settings', resource);
	};
});