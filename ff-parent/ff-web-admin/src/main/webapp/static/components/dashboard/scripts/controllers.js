angular.module('FundFinder')
	.controller('DashboardController', DashboardController);

// ========================================================================
//	OVERVIEW CONTROLLER
// ========================================================================
function DashboardController($rootScope, $scope, $state, $log, $timeout, $filter, uiGridConstants, DashboardService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
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
	
	
	
	// CHART
	
	function gd(year, month, day) {
		return moment.utc(year + "-" + month + "-" + day, "YYYY-MM-DD").toDate();
    }
	
	function gm(year, month) {
		return moment.utc(year + "-" + month + "-" + 1, "YYYY-MM-DD").toDate();
    }
	
	function getRandomInt(min, max) {
	    return Math.floor(Math.random() * (max - min + 1)) + min;
	}
	
	$scope.getData = function(period, type) {
		if (period == 'daily') {
			var data = [
	             [gd(2016, 12, 1), getRandomInt(1, 30)],
	             [gd(2016, 12, 2), getRandomInt(1, 30)],
	             [gd(2016, 12, 3), getRandomInt(1, 30)],
	             [gd(2016, 12, 4), getRandomInt(1, 30)],
	             [gd(2016, 12, 5), getRandomInt(1, 30)],
	             [gd(2016, 12, 6), getRandomInt(1, 30)],
	             [gd(2016, 12, 7), getRandomInt(1, 30)]
	        ];
			console.log(data);
			return data;
		} else if (period == 'monthly') {
			var data = [
	             [gm(2016, 7), getRandomInt(100, 300)],
	             [gm(2016, 8), getRandomInt(100, 300)],
	             [gm(2016, 9), getRandomInt(100, 300)],
	             [gm(2016, 10), getRandomInt(100, 300)],
	             [gm(2016, 11), getRandomInt(100, 300)],
	             [gm(2016, 12), getRandomInt(100, 300)]
	        ];
			console.log(data);
			return data;
		}
	}
	
	function getDataSet(period, type) {
		if (type == 'users') {
			return dataset = [
              { label: $translate('DASHBOARD_USERS'), grow: { stepMode: "linear" }, data: $scope.getData(period, type), yaxis: 1, color: "#1c84c6", lines: { lineWidth: 1, show: true, fill: false, fillColor: { colors: [{ opacity: 0.1 }, { opacity: 0.1 }] } }, points: { show: true } }
            ];
		} else if (type == 'visits') {
			return dataset = [
              { label: $translate('DASHBOARD_TOTAL') + " " + $lowercase($translate('DASHBOARD_VISITS')), grow: { stepMode: "linear" }, data: $scope.getData(period, type), yaxis: 1, color: "#1c84c6", lines: { lineWidth: 1, show: true, fill: false, fillColor: { colors: [{ opacity: 0.1 }, { opacity: 0.1 }] } }, points: { show: true } },
              { label: $translate('DASHBOARD_UNIQUE') + " " + $lowercase($translate('DASHBOARD_VISITS')), grow: { stepMode: "linear" }, data: $scope.getData(period, type), yaxis: 2, color: "#f8ac59", lines: { lineWidth: 1, show: true, fill: false, fillColor: { colors: [{ opacity: 0.1 }, { opacity: 0.1 }] } }, points: { show: true } }
            ];
		} else if (type == 'tenders') {
			return dataset = [
	          { label: $translate('DASHBOARD_TENDERS'), grow: { stepMode: "linear" }, data: $scope.getData(period, type), yaxis: 1, color: "#1ab394", lines: { lineWidth: 1, show: true, fill: false, fillColor: { colors: [{ opacity: 0.1 }, { opacity: 0.1 }] } }, points: { show: true } }
	        ];
		}
	};
	
	function getOptions(period, type) {
		if (period == 'daily') {
			return {
		        grid: { hoverable: true, clickable: true, tickColor: "#d5d5d5", borderWidth: 0, color: '#d5d5d5' },
		        tooltip: true,
		        xaxis: { mode: "time", tickSize: [1, "day"], timeformat: "%Y-%m-%d" },
		        yaxes: [{ position: "left" }, { position: "right" }],
		        legend: { show: true, noColumns: 1, labelBoxBorderColor: "#d5d5d5", position: "nw" }
		    };
		} else if (period == 'monthly') {
			return {
		        grid: { hoverable: true, clickable: true, tickColor: "#d5d5d5", borderWidth: 0, color: '#d5d5d5' },
		        tooltip: true,
		        xaxis: { mode: "time", tickSize: [1, "month"], timeformat: "%Y-%m" },
		        yaxes: [{ position: "left" }, { position: "right" }],
		        legend: { show: true, noColumns: 1, labelBoxBorderColor: "#d5d5d5", position: "nw" }
		    };
		}
	};
	
	$scope.change = function() {
		$scope.flotData = getDataSet($scope.period, $scope.type);
		$scope.flotOptions = getOptions($scope.period, $scope.type);
	};

	$scope.type = 'users';
	$scope.period = 'daily';
	$scope.flotData = getDataSet($scope.period, $scope.type);
	$scope.flotOptions = getOptions($scope.period, $scope.type);
};