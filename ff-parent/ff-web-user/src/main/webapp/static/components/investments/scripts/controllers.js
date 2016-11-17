angular.module('FundFinder')
	.controller('InvestmentsController', InvestmentsController);

function InvestmentsController($rootScope, $scope, $state, $log, $timeout, $filter, SessionStorage, InvestmentsService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.findAll = function() {
		if ($rootScope.principal.demoUser && SessionStorage.getSession("investments")) {
			// ================
			// 	DEMO MODE
			// ================
			$scope.investments = SessionStorage.getSession("investments");
		} else {
			InvestmentsService.findAll()
				.success(function(data, status) {
					if (status == 200) {
						$scope.investments = data;
					} else {
						toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
					}
				})
				.error(function(data, status) {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				});
		}
	};
	
	$scope.save = function() {
		if ($rootScope.principal.demoUser) {
			// ================
			// 	DEMO MODE
			// ================
			SessionStorage.setSession("investments", $scope.investments);
			toastr.success($translate('ACTION_SAVE_SUCCESS_MESSAGE'));
		} else {
			InvestmentsService.save($scope.investments)
				.success(function(data, status, headers, config) {
					if (status == 200) {
						toastr.success($translate('ACTION_SAVE_SUCCESS_MESSAGE'));
					} else {
						toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
					}
				})
				.error(function(data, status, headers, config) {
					toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
				});	
		}
	};
	
	// initial load
	$scope.findAll();
};
