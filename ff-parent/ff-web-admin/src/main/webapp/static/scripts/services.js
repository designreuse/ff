angular.module('FundFinder')

.service('RevisionDetailsService', function($http) {
	this.getRevisionDetails = function(resource) {
		return $http.post('/api/v1/revisions/details', resource);
	};
	this.getPreviousRevision = function(resource) {
		return $http.post('/api/v1/revisions/previous', resource);
	};
});