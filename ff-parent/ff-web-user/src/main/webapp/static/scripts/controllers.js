angular.module('FundFinder')

// ====================================================================================
//	TopnavbarController
// ====================================================================================
.controller('TopnavbarController', function($scope, $http, $window, $translate, $state, $log) {
	
	// logout function
	$scope.logout = function() {
		$http.post('/logout', {})
			.success(function() {
				$window.location.href = '';
			})
			.error(function(data) {
				$log.error(data);
			});
	};
	
	$scope.settings = function() {
		$state.go('settings.overview');
	};
	
//	$scope.registration = function() {
//		$state.go('settings.registration');
//	};
	
	// change language function
	$scope.changeLanguage = function(langKey) {
		$translate.use(langKey);
		$state.go($state.current, {}, { reload: true });
	};
	
})

// ====================================================================================
//	NavigationController
// ====================================================================================
.controller('NavigationController', function($rootScope, $scope, $http, $window) {

});