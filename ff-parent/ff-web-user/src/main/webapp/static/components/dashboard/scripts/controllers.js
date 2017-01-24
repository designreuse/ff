angular.module('FundFinder')
	.controller('DashboardController', DashboardController);

// ========================================================================
//	OVERVIEW
// ========================================================================
function DashboardController($rootScope, $scope, $state, $log, $timeout, $filter, DashboardService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.chartOptions = {
		scales: {
		    xAxes: [{
		    	display: true,
		    	type: "category",
		    	fontSize: 10,
		    	scaleLabel: {
		    		display: false
		    	}, 
		    	gridLines: {
		    		display: true
		    	},
		    	ticks: {
					fontSize: 10
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
	
	DashboardService.getData()
		.success(function(data, status) {
			if (status == 200) {
				$scope.chartLabels = data.chartLabels;
				$scope.chartData = new Array();
				$scope.chartData.push(data.chartData);
				$scope.tenders = data.tenders;
				$scope.cntTenders = data.cntTenders;
				$scope.cntProjects = data.cntProjects;
			} else {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			}
		})
		.error(function(data, status) {
			toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
		});
	
	$scope.showTender = function(tender) {
		$state.go('tenders.details', { 'id' : tender.id });
	}
	
	$scope.showTenders = function() {
		$state.go('tenders.overview');
	}
	
	$scope.showProjects = function() {
		$state.go('projects.overview');
	}
	
	$scope.showCompany = function() {
		$state.go('company.edit');
	}
	
};
