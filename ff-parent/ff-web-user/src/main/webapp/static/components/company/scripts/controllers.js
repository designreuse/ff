angular.module('FundFinder')
	.controller('CompanyEditController', CompanyEditController);

// ========================================================================
//	EDIT CONTROLLER
// ========================================================================
function CompanyEditController($rootScope, $scope, $state, $log, $timeout, $sce, $filter, CompanyService, NkdsService, CitiesService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	var trusted = {};
	$scope.toTrusted = function(html) {
		if (html) {
			html = html.replace(/\r?\n/g, '<br />');
		}
	    return trusted[html] || (trusted[html] = $sce.trustAsHtml(html)); 
	}
	
	$scope.getNkds = function() {
		NkdsService.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.nkds = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getCities = function() {
		CitiesService.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.cities = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.find = function() {
		CompanyService.find()
			.success(function(data, status) {
				if (status == 200) {
					if (data.items) { 
						$.each(data.items, function(index, item) {
							if (item.type == 'DATE') {
								if (item.value) {
									item.value = moment(item.value, $rootScope.dateFormat.toUpperCase()).toDate();
								}
								$scope.dictPopupDate[index] = { opened: false };
							}
						});
					}
					$scope.company = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.save = function() {
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
	};
	
	$scope.dictPopupDate = new Object();
	$scope.openPopupDate = function(index) {
		$scope.dictPopupDate[index].opened = true;
	};
	
	// initial load
	$scope.getNkds();
	$scope.getCities();
	$scope.find();
};
