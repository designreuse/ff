angular.module('FundFinder')

// ====================================================================================
//	TopnavbarController
// ====================================================================================
.controller('TopnavbarController', function($scope, $http, $window, $translate, $state, $log) {
	
	// logout function
	$scope.logout = function() {
		$http.post('/logout', {})
			.success(function() {
				location.reload();
			})
			.error(function(data) {
				$log.error(data);
			});
	};
	
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
	// TODO
	$rootScope.totalUsers = 0;
	$rootScope.totalTenders = 0;
	$rootScope.totalInvestments = 0;
	$rootScope.totalArticles = 0;
});