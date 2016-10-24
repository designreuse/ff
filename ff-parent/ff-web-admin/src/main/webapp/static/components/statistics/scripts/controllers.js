angular.module('FundFinder')
	.controller('StatisticsController', StatisticsController);

function StatisticsController($rootScope, $scope, $state, $log, $timeout, $filter, StatisticsService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	var $limitTo = $filter('limitTo');
	
	var limitToThreshold = 30;
	
	$scope.companiesByCountiesView = 'CHART_VIEW';
	$scope.companiesByInvestmentsView = 'CHART_VIEW';
	$scope.companiesByRevenuesView = 'CHART_VIEW';
	$scope.companiesBySectorsView = 'CHART_VIEW';
	
	$scope.changeCompaniesByCountiesView = function(view) {
		$scope.companiesByCountiesView = view;	
	};
	
	$scope.changeCompaniesByInvestmentsView = function(view) {
		$scope.companiesByInvestmentsView = view;	
	};
	
	$scope.changeCompaniesByRevenuesView = function(view) {
		$scope.companiesByRevenuesView = view;	
	};
	
	$scope.changeCompaniesBySectorsView = function(view) {
		$scope.companiesBySectorsView = view;	
	};
	
	$scope.getCompaniesByCounties = function() {
		StatisticsService.getCompaniesByCounties()
			.success(function(data, status) {
				$scope.typeCompaniesByCounties = data.type;
				
				$scope.labelsCompaniesByCounties4TableView = data.labels;
				$scope.labelsCompaniesByCounties4ChartView = new Array();
				$.each(data.labels, function(index, value) {
					$scope.labelsCompaniesByCounties4ChartView.push($limitTo(value, limitToThreshold) + ((value.length > limitToThreshold) ? "..." : ""));
				});
				
				var dataArray = new Array();
				dataArray.push(data.data);
				$scope.dataCompaniesByCounties = dataArray;
			})
			.error(function(data, status) {
				if (status == 404) {
					toastr.warning($translate('ERR_MSG_001'));
				} else if (status == 409) {
					toastr.warning($translate('ERR_MSG_002'));
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			});	
	};
	
	$scope.getCompaniesByInvestments = function() {
		StatisticsService.getCompaniesByInvestments()
			.success(function(data, status) {
				$scope.typeCompaniesByInvestments = data.type;
				
				$scope.labelsCompaniesByInvestments4TableView = data.labels;
				$scope.labelsCompaniesByInvestments4ChartView = new Array();
				$.each(data.labels, function(index, value) {
					$scope.labelsCompaniesByInvestments4ChartView.push($limitTo(value, limitToThreshold) + ((value.length > limitToThreshold) ? "..." : ""));
				});
				
				var dataArray = new Array();
				dataArray.push(data.data);
				$scope.dataCompaniesByInvestments = dataArray;
			})
			.error(function(data, status) {
				if (status == 404) {
					toastr.warning($translate('ERR_MSG_001'));
				} else if (status == 409) {
					toastr.warning($translate('ERR_MSG_002'));
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			});	
	};
	
	$scope.getCompaniesByRevenues = function() {
		StatisticsService.getCompaniesByRevenues()
			.success(function(data, status) {
				$scope.typeCompaniesByRevenues = data.type;
				
				$scope.labelsCompaniesByRevenues4TableView = data.labels;
				$scope.labelsCompaniesByRevenues4ChartView = new Array();
				$.each(data.labels, function(index, value) {
					$scope.labelsCompaniesByRevenues4ChartView.push($limitTo(value, limitToThreshold) + ((value.length > limitToThreshold) ? "..." : ""));
				});
				
				var dataArray = new Array();
				dataArray.push(data.data);
				$scope.dataCompaniesByRevenues = dataArray;
			})
			.error(function(data, status) {
				if (status == 404) {
					toastr.warning($translate('ERR_MSG_001'));
				} else if (status == 409) {
					toastr.warning($translate('ERR_MSG_002'));
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			});	
	};
	
	$scope.getCompaniesBySectors = function() {
		StatisticsService.getCompaniesBySectors()
			.success(function(data, status) {
				$scope.typeCompaniesBySectors = data.type;
				
				$scope.labelsCompaniesBySectors4TableView = data.labels;
				$scope.labelsCompaniesBySectors4ChartView = new Array();
				$.each(data.labels, function(index, value) {
					$scope.labelsCompaniesBySectors4ChartView.push($limitTo(value, limitToThreshold) + ((value.length > limitToThreshold) ? "..." : ""));
				});
				
				var dataArray = new Array();
				dataArray.push(data.data);
				$scope.dataCompaniesBySectors = dataArray;
			})
			.error(function(data, status) {
				if (status == 404) {
					toastr.warning($translate('ERR_MSG_001'));
				} else if (status == 409) {
					toastr.warning($translate('ERR_MSG_002'));
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			});	
	};
};