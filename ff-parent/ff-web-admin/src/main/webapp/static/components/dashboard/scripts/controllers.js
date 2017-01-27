angular.module('FundFinder')
	.controller('DashboardController', DashboardController);

// ========================================================================
//	OVERVIEW CONTROLLER
// ========================================================================
function DashboardController($rootScope, $scope, $state, $log, $timeout, $filter, uiGridConstants, DashboardService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	var dailyOptions = {
		grid: { hoverable: true, clickable: true, tickColor: "#d5d5d5", borderWidth: 0, color: '#d5d5d5' },
        tooltip: true,
        xaxis: { mode: "time", tickSize: [1, "day"], timeformat: "%Y-%m-%d" },
        yaxes: [{ position: "left" }, { position: "right" }],
        legend: { show: true, noColumns: 1, labelBoxBorderColor: "#d5d5d5", position: "nw" }	
	};
	
	var monthlyOptions = {
		grid: { hoverable: true, clickable: true, tickColor: "#d5d5d5", borderWidth: 0, color: '#d5d5d5' },
        tooltip: true,
        xaxis: { mode: "time", tickSize: [1, "month"], timeformat: "%Y-%m" },
        yaxes: [{ position: "left" }, { position: "right" }],
        legend: { show: true, noColumns: 1, labelBoxBorderColor: "#d5d5d5", position: "nw" }	
	};
	
	$scope.getData = function() {
		DashboardService.getData()
			.success(function(data, status) {
				if (status == 200) {
					$scope.data = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getChartData = function(period, type) {
		DashboardService.getChartData(period, type)
			.success(function(data, status) {
				var dataset;
				if (type == 'users') {
					dataset = [
		              { label: $translate('DASHBOARD_USERS'), grow: { stepMode: "linear" }, data: data.serie1, yaxis: 1, color: "#1c84c6", lines: { lineWidth: 1, show: true, fill: false, fillColor: { colors: [{ opacity: 0.1 }, { opacity: 0.1 }] } }, points: { show: true } }
		            ];
				} else if (type == 'visits') {
					dataset = [
		              { label: $translate('DASHBOARD_TOTAL') + " " + $lowercase($translate('DASHBOARD_VISITS')), grow: { stepMode: "linear" }, data: data.serie1, yaxis: 1, color: "#1c84c6", lines: { lineWidth: 1, show: true, fill: false, fillColor: { colors: [{ opacity: 0.1 }, { opacity: 0.1 }] } }, points: { show: true } },
		              { label: $translate('DASHBOARD_UNIQUE') + " " + $lowercase($translate('DASHBOARD_VISITS')), grow: { stepMode: "linear" }, data: data.serie2, yaxis: 2, color: "#f8ac59", lines: { lineWidth: 1, show: true, fill: false, fillColor: { colors: [{ opacity: 0.1 }, { opacity: 0.1 }] } }, points: { show: true } }
		            ];
				} else if (type == 'tenders') {
					dataset = [
			          { label: $translate('DASHBOARD_TENDERS'), grow: { stepMode: "linear" }, data: data.serie1, yaxis: 1, color: "#1ab394", lines: { lineWidth: 1, show: true, fill: false, fillColor: { colors: [{ opacity: 0.1 }, { opacity: 0.1 }] } }, points: { show: true } }
			        ];
				}
				
				var options;
				if (period == 'daily') {
					options = dailyOptions;
				} else if (period = 'monthly') {
					options = monthlyOptions;
				}
				
				$scope.flotData = dataset;
				$scope.flotOptions = options;
				
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});		
	};
	
	$scope.change = function() {
		$scope.getChartData($scope.period, $scope.type);
	};
	
	$scope.period = 'daily';	
	$scope.type = 'users';
	$scope.getChartData($scope.period, $scope.type);
	$scope.getData();
};