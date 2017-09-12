angular.module('FundFinder')

.service('GfiSyncsService', function($http) {
	this.deleteEntity = function(id) {
		return $http.delete('/api/v1/gfisyncs/' + id);
	};
	this.getEntity = function(id) {
		return $http.get('/api/v1/gfisyncs/' + id);
	};
	this.getPage = function(resource) {
		return $http.post('/api/v1/gfisyncs/page', resource);
	};
})

.service('GfiSyncErrorsService', function($http) {
	this.getEntity = function(id) {
		return $http.get('/api/v1/gfisyncerrors/' + id);
	};
});