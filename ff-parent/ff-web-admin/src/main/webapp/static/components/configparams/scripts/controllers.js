angular.module('FundFinder')
	.controller('ConfigParamsController', ConfigParamsController);

function ConfigParamsController($rootScope, $scope, $state, $stateParams, $log, $timeout, $filter, ConfigParamsService) {
	var $translate = $filter('translate');
	
	$scope.getEntities = function() {
		ConfigParamsService.getEntities()
			.success(function(data, status) {
				$scope.entities = data;
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.saveEntities = function() {
		ConfigParamsService.saveEntities($scope.entities)
			.success(function(data, status, headers, config) {
				toastr.success($translate('ACTION_SAVE_SUCCESS_MESSAGE'));
				$state.go('settings.configparams');
				$scope.getEntities();
			})
			.error(function(data, status, headers, config) {
				toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
			});		
	};
	
	// initial load
	$scope.getEntities();
};