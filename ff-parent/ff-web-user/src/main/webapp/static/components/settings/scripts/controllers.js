angular.module('FundFinder')
	.controller('SettingsController', SettingsController);

function SettingsController($rootScope, $scope, $state, $log, $timeout, $filter, SettingsService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.find = function() {
		SettingsService.find()
			.success(function(data, status) {
				if (status == 200) {
					$scope.settings = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.save = function() {
		SettingsService.save($scope.settings)
			.success(function(data, status, headers, config) {
				if (status == 200) {
					toastr.success($translate('ACTION_SAVE_SUCCESS_MESSAGE'));
					$rootScope.principal.firstName = data.user.firstName;
					$rootScope.principal.lastName = data.user.lastName;
				} else {
					toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status, headers, config) {
				toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
			});	
	};
	
	// initial load
	$scope.find();
};