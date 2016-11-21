angular.module('FundFinder')
	.controller('TendersOverviewController', TendersOverviewController)
	.controller('TendersDetailsController', TendersDetailsController);

// ========================================================================
//	OVERVIEW
// ========================================================================
function TendersOverviewController($rootScope, $scope, $state, $log, $timeout, $filter, uiGridConstants, SessionStorage, TendersService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.findAll = function() {
		if ($rootScope.principal.demoUser) {
			var company = SessionStorage.getSession("company")
			var investments = SessionStorage.getSession("investments");
			
			var demoResource = {
				"company" : company,
				"investments" : investments
			};
			
			console.log(demoResource);
			
			TendersService.findAllDemo(demoResource)
				.success(function(data, status, headers, config) {
					if (status == 200) {
						$scope.tenders = data;
					} else {
						toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
					}
				})
				.error(function(data, status, headers, config) {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				});	
		} else {
			TendersService.findAll()
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
		}
	};
	
	$scope.showDetails = function(tender) {
		$state.go('tenders.details', { 'id' : tender.id });
	};
	
	// initial load
	$scope.findAll();
};

//========================================================================
//	DETAILS
//========================================================================
function TendersDetailsController($rootScope, $scope, $state, $stateParams, $log, $timeout, $filter, TendersService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.back = function() {
		$state.go('tenders.overview');
	};
	
	$scope.find = function(id) {
		TendersService.find(id)
			.success(function(data, status) {
				if (status == 200) {
					$scope.tender = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	// initial load
	$scope.find($stateParams.id);
};