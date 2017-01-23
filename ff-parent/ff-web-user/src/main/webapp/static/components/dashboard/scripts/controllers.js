angular.module('FundFinder')
	.controller('DashboardController', DashboardController);

// ========================================================================
//	OVERVIEW
// ========================================================================
function DashboardController($rootScope, $scope, $state, $log, $timeout, $filter, DashboardService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.chartOptions = {
		tooltips: {
			enabled: true,
			titleFontSize: 10
		}
	};
	
	DashboardService.getData()
		.success(function(data, status) {
			if (status == 200) {
				$scope.chartLabels = data.chartLabels;
				$scope.chartData = new Array();
				$scope.chartData.push(data.chartData);
				$scope.tenders = data.tenders;
			} else {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			}
		})
		.error(function(data, status) {
			toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
		});
};
