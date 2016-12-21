angular.module('FundFinder')
	.controller('InvestmentsController', InvestmentsController);

function InvestmentsController($rootScope, $scope, $state, $log, $timeout, $filter, SessionStorage, InvestmentsService, CompanyService, ActivitiesService, CurrenciesService, Subdivisions1Service, Subdivisions2Service) {
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
			SessionStorage.setSession("company", $scope.company);
			SessionStorage.setSession("investments", $scope.investments);
			toastr.success($translate('ACTION_SAVE_SUCCESS_MESSAGE'));
		} else {
			CompanyService.save($scope.company)
				.success(function(data, status, headers, config) {
					if (status == 200) {
						$rootScope.validateProfile();
						toastr.success($translate('ACTION_SAVE_SUCCESS_MESSAGE'));
					} else {
						toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
					}
				})
				.error(function(data, status, headers, config) {
					toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
				});	
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
	
	$scope.getCompany = function() {
		if ($rootScope.principal.demoUser && SessionStorage.getSession("company")) {
			// ================
			// 	DEMO MODE
			// ================
			$scope.company = SessionStorage.getSession("company");
		} else {
			CompanyService.find()
				.success(function(data, status) {
					if (status == 200) {
						$scope.company = data;
					} else {
						toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
					}
				})
				.error(function(data, status) {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				});
		}
	};
	
	$scope.getCurrencies = function() {
		CurrenciesService.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.currencies = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getActivities = function() {
		ActivitiesService.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.activities = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getSubdivisions1 = function() {
		Subdivisions1Service.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.subdivisions1 = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getSubdivisions2 = function() {
		Subdivisions2Service.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.subdivisions2 = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	// initial load
	$scope.getCompany();
	$scope.getCurrencies();
	$scope.getActivities();
	$scope.getSubdivisions1();
	$scope.getSubdivisions2();
	$scope.findAll();
};
