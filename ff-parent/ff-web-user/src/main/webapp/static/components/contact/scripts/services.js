angular.module('FundFinder')

.service('ContactService', function($http) {
	this.getTopics = function() {
		return $http.get('/api/v1/contact/topics');
	};
	this.getTypes = function() {
		return $http.get('/api/v1/contact/types');
	};
	this.getChannels = function() {
		return $http.get('/api/v1/contact/channels');
	};
	this.get = function() {
		return $http.get('/api/v1/contact');
	};
	this.set = function(resource) {
		return $http.post('/api/v1/contact', resource);
	};
});