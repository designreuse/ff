angular.module('FundFinder')
	.controller('DebuggingController', DebuggingController);

function DebuggingController($rootScope, $scope, $state, $log, $timeout, $filter, DebuggingService, UsersService, TendersService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.getUsers = function() {
		UsersService.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.users = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});	
	};
	
	$scope.getTenders = function() {
		TendersService.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.tenders = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});	
	};
	
	$scope.debug = function() {
		DebuggingService.debug($scope.user.id, $scope.tender.id)
			.success(function(data, status, headers, config) {
				$scope.data = data;
			})
			.error(function(data, status, headers, config) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	// initial load
	$scope.getUsers();
	$scope.getTenders();
	
	$scope.user = null;
	$scope.tender = null;
};