angular.module('FundFinder')
	.controller('InvestmentsController', InvestmentsController);

function InvestmentsController($rootScope, $scope, $state, $log, $timeout, $filter, InvestmentsService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.findAll = function() {
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
	};
	
	$scope.save = function() {
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
	};
	
	// initial load
	$scope.findAll();
};
