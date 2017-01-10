angular.module('FundFinder')
	.controller('ContactController', ContactController);

// ========================================================================
//	OVERVIEW
// ========================================================================
function ContactController($rootScope, $scope, $state, $log, $timeout, $filter, ContactService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.getTopics = function() {
		ContactService.getTopics()
			.success(function(data, status) {
				$scope.topics = data;
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getTypes = function() {
		ContactService.getTypes()
			.success(function(data, status) {
				$scope.types = data;
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getChannels = function() {
		ContactService.getChannels()
			.success(function(data, status) {
				$scope.channels = data;
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.get = function() {
		ContactService.get()
			.success(function(data, status) {
				$scope.entity = data;
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.send = function() {
		$scope.sendingEmail = true;
		ContactService.set($scope.entity)
			.success(function(data, status) {
				toastr.success($translate('ACTION_SEND_SUCCESS_MESSAGE'));
				$scope.sendingEmail = false;
				$scope.get();
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_SEND_FAILURE_MESSAGE'));
				$scope.sendingEmail = false;
			});
	};
	
	// initial load
	$scope.getTopics();
	$scope.getTypes();
	$scope.getChannels();
	$scope.get();
};
