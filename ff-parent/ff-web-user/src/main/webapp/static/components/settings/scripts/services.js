angular.module('FundFinder')

.service('SettingsService', function($http) {
	this.find = function() {
		return $http.get('/api/v1/settings');
	};
	this.save = function(resource) {
		return $http.post('/api/v1/settings', resource);
	};
	this.deactivate = function() {
		return $http.get('/api/v1/settings/deactivate');
	};
	this.registerDemo = function(resource) {
		return $http.post('/api/v1/users/registerDemo', resource);
	};
});