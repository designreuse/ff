angular.module('FundFinder')

.service('CommonService', function($http) {
	this.getProfileCompleteness = function() {
		return $http.get('/api/v1/company/profileCompleteness');
	};
	
	this.ping = function() {
		return $http.get('/api/v1/company/ping');
	};
})

.factory('SessionStorage',['$window', function($window) {
	return {
		setSession: function(key, value) {
			try {
				if ($window.Storage) {
					$window.sessionStorage.setItem(key, $window.JSON.stringify(value));
				}
			} catch (error) {
				console.error(error, error.message);
			}
		},
		getSession: function(key) {
			try {
				if ($window.Storage && $window.sessionStorage.getItem(key) != 'undefined') {
					return $window.JSON.parse($window.sessionStorage.getItem(key))
				} else {
					return "";
				}
			} catch (error) {
				console.error(error, error.message);
			}
		}
	}
}]);