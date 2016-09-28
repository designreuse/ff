angular.module('FundFinder')

// ====================================================================================
//	RevisionDetailsController
// ====================================================================================
.controller('RevisionDetailsController', function($rootScope, $scope, $state, $log, $translate, RevisionDetailsService, revision) {

	$scope.getPreviousRevision = function() {
		RevisionDetailsService.getPreviousRevision($scope.revision)
			.success(function(data, status, headers, config) {
				if (status == 200) {
					$scope.revisionPrev = data;
					if ($scope.revisionPrev) {
						RevisionDetailsService.getRevisionDetails($scope.revisionPrev)
							.success(function(data, status, headers, config) {
								if (status == 200) {
									$scope.revisionPrevDetails = JSON.stringify(data, null, 4);
								} else {
									toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
								}
							})
							.error(function(data, status, headers, config) {
								toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
							});	
					}
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status, headers, config) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});	
	};
	
	$scope.getRevisionDetails = function() {
		RevisionDetailsService.getRevisionDetails($scope.revision)
			.success(function(data, status, headers, config) {
				if (status == 200) {
					$scope.revisionDetails = JSON.stringify(data, null, 4);
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status, headers, config) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});		
	};
	
	$scope.compare = function() {
		$scope.compareVisible = true;
	};
	
	$scope.back = function() {
		$scope.compareVisible = false;
	};
	
	$scope.compareVisible = false;
	$scope.revision = revision;
	
	// initial load
	$scope.getPreviousRevision();
	$scope.getRevisionDetails();

})

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