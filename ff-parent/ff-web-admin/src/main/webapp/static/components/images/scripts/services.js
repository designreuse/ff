angular.module('FundFinder')

.service('ImagesService', function($http) {
	this.getEntity = function(id) {
		return $http.get('/api/v1/images/' + id);
	};
});