angular.module('FundFinder')
	.controller('StatisticsController', StatisticsController);

function StatisticsController($rootScope, $scope, $state, $log, $timeout, $filter, StatisticsService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	var $limitTo = $filter('limitTo');
	
	var limitToThreshold = 30;
	
	$scope.options = {
		scales: {
		    xAxes: [{
		    	display: true,
		    	type: "category",
		    	barThickness: 30,
		    	categoryPercentage: 0.3,
		    	scaleLabel: {
		    		display: false
		    	}, 
		    	gridLines: {
		    		display: true,
		    		tickMarkLength: 5
		    	}
		    }],
		    yAxes: [{
		    	display: true,
		    	ticks: {
					fontSize: 10,
					stepSize: 1
				}
		    }]
		},
		tooltips: {
			enabled: true,
			titleFontSize: 10
		}
	};
	
	$scope.companiesByCountiesView = 'CHART_VIEW';
	$scope.companiesByRevenuesView = 'CHART_VIEW';
	$scope.companiesBySizeView = 'CHART_VIEW';
	$scope.investmentsByCountiesView = 'CHART_VIEW';
	$scope.investmentsByActivitiesView = 'CHART_VIEW';
	
	$scope.changeCompaniesByCountiesView = function(view) {
		$scope.companiesByCountiesView = view;	
	};
	
	$scope.changeCompaniesByRevenuesView = function(view) {
		$scope.companiesByRevenuesView = view;	
	};
	
	$scope.changeCompaniesBySizeView = function(view) {
		$scope.companiesBySizeView = view;	
	};
	
	$scope.changeInvestmentsByCountiesView = function(view) {
		$scope.investmentsByCountiesView = view;	
	};
	
	$scope.changeInvestmentsByActivitiesView = function(view) {
		$scope.investmentsByActivitiesView = view;	
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
				
				var total = 0;
				$.each(dataArray[0], function(index, value) {
					total = total + parseInt(value);
				});
				
				$scope.percentageCompaniesByCounties4TableView = new Array();
				$.each(dataArray[0], function(index, value) {
					if (total == 0) {
						$scope.percentageCompaniesByCounties4TableView.push(0);
					} else {
						$scope.percentageCompaniesByCounties4TableView.push(parseFloat((value * 100) / total).toFixed(0));
					}
				});
				
				$scope.totalCompaniesByCounties4TableView = total;
			})
			.error(function(data, status) {
				if (status == 404) {
					toastr.warning($translate('ERR_MSG_001', { metatag: "COMPANY_LOCATION" }));
				} else if (status == 409) {
					toastr.warning($translate('ERR_MSG_002', { metatag: "COMPANY_LOCATION" }));
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
				
				var total = 0;
				$.each(dataArray[0], function(index, value) {
					total = total + parseInt(value);
				});
				
				$scope.percentageCompaniesByRevenues4TableView = new Array();
				$.each(dataArray[0], function(index, value) {
					if (total == 0) {
						$scope.percentageCompaniesByRevenues4TableView.push(0);
					} else {
						$scope.percentageCompaniesByRevenues4TableView.push(parseFloat((value * 100) / total).toFixed(0));
					}
				});
				
				$scope.totalCompaniesByRevenues4TableView = total;
			})
			.error(function(data, status) {
				if (status == 404) {
					toastr.warning($translate('ERR_MSG_001', { metatag: "COMPANY_REVENUE" }));
				} else if (status == 409) {
					toastr.warning($translate('ERR_MSG_002', { metatag: "COMPANY_REVENUE" }));
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			});	
	};
	
	$scope.getCompaniesBySize = function() {
		StatisticsService.getCompaniesBySize()
			.success(function(data, status) {
				$scope.typeCompaniesBySize = data.type;
				
				$scope.labelsCompaniesBySize4TableView = data.labels;
				$scope.labelsCompaniesBySize4ChartView = new Array();
				$.each(data.labels, function(index, value) {
					$scope.labelsCompaniesBySize4ChartView.push($limitTo(value, limitToThreshold) + ((value.length > limitToThreshold) ? "..." : ""));
				});
				
				var dataArray = new Array();
				dataArray.push(data.data);
				$scope.dataCompaniesBySize = dataArray;
				
				var total = 0;
				$.each(dataArray[0], function(index, value) {
					total = total + parseInt(value);
				});
				
				$scope.percentageCompaniesBySize4TableView = new Array();
				$.each(dataArray[0], function(index, value) {
					if (total == 0) {
						$scope.percentageCompaniesBySize4TableView.push(0);
					} else {
						$scope.percentageCompaniesBySize4TableView.push(parseFloat((value * 100) / total).toFixed(0));
					}
				});
				
				$scope.totalCompaniesBySize4TableView = total;
			})
			.error(function(data, status) {
				if (status == 404) {
					toastr.warning($translate('ERR_MSG_001', { metatag: "COMPANY_SIZE" }));
				} else if (status == 409) {
					toastr.warning($translate('ERR_MSG_002', { metatag: "COMPANY_SIZE" }));
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			});	
	};
	
	$scope.getInvestmentsByCounties = function() {
		StatisticsService.getInvestmentsByCounties()
			.success(function(data, status) {
				$scope.typeInvestmentsByCounties = data.type;
				
				$scope.labelsInvestmentsByCounties4TableView = data.labels;
				$scope.labelsInvestmentsByCounties4ChartView = new Array();
				$.each(data.labels, function(index, value) {
					$scope.labelsInvestmentsByCounties4ChartView.push($limitTo(value, limitToThreshold) + ((value.length > limitToThreshold) ? "..." : ""));
				});
				
				var dataArray = new Array();
				dataArray.push(data.data);
				$scope.dataInvestmentsByCounties = dataArray;
				
				var total = 0;
				$.each(dataArray[0], function(index, value) {
					total = total + parseInt(value);
				});
				
				$scope.percentageInvestmentsByCounties4TableView = new Array();
				$.each(dataArray[0], function(index, value) {
					if (total == 0) {
						$scope.percentageInvestmentsByCounties4TableView.push(0);
					} else {
						$scope.percentageInvestmentsByCounties4TableView.push(parseFloat((value * 100) / total).toFixed(0));
					}
				});
				
				$scope.totalInvestmentsByCounties4TableView = total;
				
				// investment amount by counties
				$scope.investmentAmountByCounties = data.data2;
				$scope.investmentAmountByCountiesTotal = 0;
				$.each($scope.investmentAmountByCounties, function(index, value) {
					$scope.investmentAmountByCountiesTotal = $scope.investmentAmountByCountiesTotal + parseFloat(value);
				});
				
				// currency
				$scope.currency = data.currency; 
			})
			.error(function(data, status) {
				if (status == 404) {
					toastr.warning($translate('ERR_MSG_001', { metatag: "COMPANY_INVESTMENT_SUBDIVISION1" }));
				} else if (status == 409) {
					toastr.warning($translate('ERR_MSG_002', { metatag: "COMPANY_INVESTMENT_SUBDIVISION1" }));
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			});	
	};
	
	$scope.getInvestmentsByActivities = function() {
		StatisticsService.getInvestmentsByActivities()
			.success(function(data, status) {
				$scope.typeInvestmentsByActivities = data.type;
				
				$scope.labelsInvestmentsByActivities4TableView = data.labels;
				$scope.labelsInvestmentsByActivities4ChartView = new Array();
				$.each(data.labels, function(index, value) {
					$scope.labelsInvestmentsByActivities4ChartView.push($limitTo(value, limitToThreshold) + ((value.length > limitToThreshold) ? "..." : ""));
				});
				
				var dataArray = new Array();
				dataArray.push(data.data);
				$scope.dataInvestmentsByActivities = dataArray;
				
				var total = 0;
				$.each(dataArray[0], function(index, value) {
					total = total + parseInt(value);
				});
				
				$scope.percentageInvestmentsByActivities4TableView = new Array();
				$.each(dataArray[0], function(index, value) {
					if (total == 0) {
						$scope.percentageInvestmentsByActivities4TableView.push(0);
					} else {
						$scope.percentageInvestmentsByActivities4TableView.push(parseFloat((value * 100) / total).toFixed(0));
					}
				});
				
				$scope.totalInvestmentsByActivities4TableView = total;
				
				// investment amount by activities
				$scope.investmentAmountByActivities = data.data2;
				$scope.investmentAmountByActivitiesTotal = 0;
				$.each($scope.investmentAmountByActivities, function(index, value) {
					$scope.investmentAmountByActivitiesTotal = $scope.investmentAmountByActivitiesTotal + parseFloat(value);
				});
				
				// currency
				$scope.currency = data.currency; 
			})
			.error(function(data, status) {
				if (status == 404) {
					toastr.warning($translate('ERR_MSG_001', { metatag: "COMPANY_INVESTMENT_ACTIVITY" }));
				} else if (status == 409) {
					toastr.warning($translate('ERR_MSG_002', { metatag: "COMPANY_INVESTMENT_ACTIVITY" }));
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			});	
	};
	
};