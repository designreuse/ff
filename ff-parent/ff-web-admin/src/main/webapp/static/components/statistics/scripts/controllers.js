angular.module('FundFinder')
	.controller('StatisticsController', StatisticsController);

function StatisticsController($rootScope, $scope, $state, $log, $timeout, $filter, StatisticsService, FileSaver, Blob) {
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
	
	$scope.optionsPie = {
		legend: {
			display: true,
			position: "right"
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
				
				// export data (CSV)
				var companiesByCounties4ExportCSV = new Array();
				$.each(data.data, function(index, value) {
					companiesByCounties4ExportCSV.push({
						"county" : data.labels[index],
						"number" : value,
						"percentage" : $scope.percentageCompaniesByCounties4TableView[index]
					});
				});
				$scope.companiesByCounties4ExportCSV = companiesByCounties4ExportCSV;
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
	
	$scope.exportCompaniesByCountiesCSV = function() {
		var rows = new Array();
		rows.push(['county', 'number', 'percentage']);
		
		$.each($scope.companiesByCounties4ExportCSV, function(index, value) {
			rows.push([value.county, value.number, value.percentage]);
		});
		
        var data = new Blob([rows.join('\n')], { type: 'data:text/csv;charset=utf-8' });
        FileSaver.saveAs(data, 'companies_by_county.csv');
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
				
				$scope.dataCompaniesByRevenues = data.data;
				
				var total = 0;
				$.each(data.data, function(index, value) {
					total = total + parseInt(value);
				});
				
				$scope.percentageCompaniesByRevenues4TableView = new Array();
				$.each(data.data, function(index, value) {
					if (total == 0) {
						$scope.percentageCompaniesByRevenues4TableView.push(0);
					} else {
						$scope.percentageCompaniesByRevenues4TableView.push(parseFloat((value * 100) / total).toFixed(0));
					}
				});
				
				$scope.totalCompaniesByRevenues4TableView = total;
				
				// export data (CSV)
				var companiesByRevenues4ExportCSV = new Array();
				$.each(data.data, function(index, value) {
					companiesByRevenues4ExportCSV.push({
						"revenue" : data.labels[index],
						"number" : value,
						"percentage" : $scope.percentageCompaniesByRevenues4TableView[index]
					});
				});
				$scope.companiesByRevenues4ExportCSV = companiesByRevenues4ExportCSV;
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
	
	$scope.exportCompaniesByRevenuesCSV = function() {
		var rows = new Array();
		rows.push(['revenue', 'number', 'percentage']);
		
		$.each($scope.companiesByRevenues4ExportCSV, function(index, value) {
			rows.push([value.revenue, value.number, value.percentage]);
		});
		
		var data = new Blob([rows.join('\n')], { type: 'data:text/csv;charset=utf-8' });
        FileSaver.saveAs(data, 'companies_by_revenue.csv');
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
				
				$scope.dataCompaniesBySize = data.data;
				
				var total = 0;
				$.each(data.data, function(index, value) {
					total = total + parseInt(value);
				});
				
				$scope.percentageCompaniesBySize4TableView = new Array();
				$.each(data.data, function(index, value) {
					if (total == 0) {
						$scope.percentageCompaniesBySize4TableView.push(0);
					} else {
						$scope.percentageCompaniesBySize4TableView.push(parseFloat((value * 100) / total).toFixed(0));
					}
				});
				
				$scope.totalCompaniesBySize4TableView = total;
				
				// export data (CSV)
				var companiesBySize4ExportCSV = new Array();
				$.each(data.data, function(index, value) {
					companiesBySize4ExportCSV.push({
						"size" : data.labels[index],
						"number" : value,
						"percentage" : $scope.percentageCompaniesBySize4TableView[index]
					});
				});
				$scope.companiesBySize4ExportCSV = companiesBySize4ExportCSV;
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
	
	$scope.exportCompaniesBySizeCSV = function() {
		var rows = new Array();
		rows.push(['size', 'number', 'percentage']);
		
		$.each($scope.companiesBySize4ExportCSV, function(index, value) {
			rows.push([value.size, value.number, value.percentage]);
		});
		
		var data = new Blob([rows.join('\n')], { type: 'data:text/csv;charset=utf-8' });
        FileSaver.saveAs(data, 'companies_by_size.csv');
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
				
				// export data (CSV)
				var investmentsByCounties4ExportCSV = new Array();
				$.each(data.data, function(index, value) {
					investmentsByCounties4ExportCSV.push({
						"county" : data.labels[index],
						"number" : value,
						"percentage" : $scope.percentageInvestmentsByCounties4TableView[index],
						"value" : $scope.investmentAmountByCounties[index]
					});
				});
				$scope.investmentsByCounties4ExportCSV = investmentsByCounties4ExportCSV;
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
	
	$scope.exportInvestmentsByCountiesCSV = function() {
		var rows = new Array();
		rows.push(['county', 'number', 'percentage', 'value']);
		
		$.each($scope.investmentsByCounties4ExportCSV, function(index, value) {
			rows.push([value.county, value.number, value.percentage, value.value]);
		});
		
		var data = new Blob([rows.join('\n')], { type: 'data:text/csv;charset=utf-8' });
        FileSaver.saveAs(data, 'investments_by_county.csv');
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
				
				// export data (CSV)
				var investmentsByActivities4ExportCSV = new Array();
				$.each(data.data, function(index, value) {
					investmentsByActivities4ExportCSV.push({
						"activity" : data.labels[index],
						"number" : value,
						"percentage" : $scope.percentageInvestmentsByActivities4TableView[index],
						"value" : $scope.investmentAmountByActivities[index]
					});
				});
				$scope.investmentsByActivities4ExportCSV = investmentsByActivities4ExportCSV;
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
	
	$scope.exportInvestmentsByActivitiesCSV = function() {
		var rows = new Array();
		rows.push(['activity', 'number', 'percentage', 'value']);
		
		$.each($scope.investmentsByActivities4ExportCSV, function(index, value) {
			rows.push([value.activity, value.number, value.percentage, value.value]);
		});
		
		var data = new Blob([rows.join('\n')], { type: 'data:text/csv;charset=utf-8' });
        FileSaver.saveAs(data, 'investments_by_activity.csv');
	};
	
};