angular.module('FundFinder')

// ====================================================================================
//	TopnavbarController
// ====================================================================================
.controller('TopnavbarController', function($scope, $http, $window, $translate, $state, $log) {
	
	// change language function
	$scope.changeLanguage = function(langKey) {
		$translate.use(langKey);
		$state.go($state.current, {}, { reload: true });
	};
	
})

// ====================================================================================
//	NavigationController
// ====================================================================================
.controller('NavigationController', function($rootScope, $scope, $http, $window, $log) {
	
})

// ====================================================================================
//	AccessDeniedController
// ====================================================================================
.controller('DeniedController', function($rootScope, $scope) {
	
});