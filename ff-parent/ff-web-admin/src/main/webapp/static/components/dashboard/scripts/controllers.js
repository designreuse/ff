angular.module('FundFinder')
	.controller('DashboardController', DashboardController);

// ========================================================================
//	OVERVIEW CONTROLLER
// ========================================================================
function DashboardController($rootScope, $scope, $state, $log, $timeout, $filter, uiGridConstants, DashboardService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	function gd(year, month, day) {
        return new Date(year, month - 1, day).getTime();
    }
	
	function gd2(year, month, day, hours) {
        return new Date(year, month - 1, day, hours, 0, 0, 0).getTime();
    }
	
	function getRandomInt(min, max) {
	    return Math.floor(Math.random() * (max - min + 1)) + min;
	}
	
	$scope.getRegisterUsers = function(period) {
		if (period == 'today') {
			return [
	             [gd2(2016, 12, 9, 1), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 2), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 3), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 4), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 5), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 6), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 7), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 8), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 9), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 10), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 11), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 12), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 13), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 14), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 15), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 16), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 17), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 18), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 19), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 20), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 21), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 22), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 23), getRandomInt(1, 30)],
	             [gd2(2016, 12, 9, 24), getRandomInt(1, 30)],
	             [gd2(2016, 12, 10, 1), getRandomInt(1, 30)]
	        ];
		} else if (period == 'weekly') {
			return [
	             [gd(2016, 12, 1), getRandomInt(1, 30)],
	             [gd(2016, 12, 2), getRandomInt(1, 30)],
	             [gd(2016, 12, 3), getRandomInt(1, 30)],
	             [gd(2016, 12, 4), getRandomInt(1, 30)],
	             [gd(2016, 12, 5), getRandomInt(1, 30)],
	             [gd(2016, 12, 6), getRandomInt(1, 30)],
	             [gd(2016, 12, 7), getRandomInt(1, 30)]
	        ];
		} else {
			return [
	             [gd(2012, 1, 1), getRandomInt(1, 30)],
	             [gd(2012, 1, 2), getRandomInt(1, 30)],
	             [gd(2012, 1, 3), getRandomInt(1, 30)],
	             [gd(2012, 1, 4), getRandomInt(1, 30)],
	             [gd(2012, 1, 5), getRandomInt(1, 30)],
	             [gd(2012, 1, 6), getRandomInt(1, 30)],
	             [gd(2012, 1, 7), getRandomInt(1, 30)],
	             [gd(2012, 1, 8), getRandomInt(1, 30)],
	             [gd(2012, 1, 9), getRandomInt(1, 30)],
	             [gd(2012, 1, 10), getRandomInt(1, 30)],
	             [gd(2012, 1, 11), getRandomInt(1, 30)],
	             [gd(2012, 1, 12), getRandomInt(1, 30)],
	             [gd(2012, 1, 13), getRandomInt(1, 30)],
	             [gd(2012, 1, 14), getRandomInt(1, 30)],
	             [gd(2012, 1, 15), getRandomInt(1, 30)],
	             [gd(2012, 1, 16), getRandomInt(1, 30)],
	             [gd(2012, 1, 17), getRandomInt(1, 30)],
	             [gd(2012, 1, 18), getRandomInt(1, 30)],
	             [gd(2012, 1, 19), getRandomInt(1, 30)],
	             [gd(2012, 1, 20), getRandomInt(1, 30)],
	             [gd(2012, 1, 21), getRandomInt(1, 30)],
	             [gd(2012, 1, 22), getRandomInt(1, 30)],
	             [gd(2012, 1, 23), getRandomInt(1, 30)],
	             [gd(2012, 1, 24), getRandomInt(1, 30)],
	             [gd(2012, 1, 25), getRandomInt(1, 30)],
	             [gd(2012, 1, 26), getRandomInt(1, 30)],
	             [gd(2012, 1, 27), getRandomInt(1, 30)],
	             [gd(2012, 1, 28), getRandomInt(1, 30)],
	             [gd(2012, 1, 29), getRandomInt(1, 30)],
	             [gd(2012, 1, 30), getRandomInt(1, 30)],
	             [gd(2012, 1, 31), getRandomInt(1, 30)]
	        ];
		}
	}

	var data2 = [
         [gd(2012, 1, 1), getRandomInt(100, 1000)],
         [gd(2012, 1, 2), getRandomInt(100, 1000)],
         [gd(2012, 1, 3), getRandomInt(100, 1000)],
         [gd(2012, 1, 4), getRandomInt(100, 1000)],
         [gd(2012, 1, 5), getRandomInt(100, 1000)],
         [gd(2012, 1, 6), getRandomInt(100, 1000)],
         [gd(2012, 1, 7), getRandomInt(100, 1000)],
         [gd(2012, 1, 8), getRandomInt(100, 1000)],
         [gd(2012, 1, 9), getRandomInt(100, 1000)],
         [gd(2012, 1, 10), getRandomInt(100, 1000)],
         [gd(2012, 1, 11), getRandomInt(100, 1000)],
         [gd(2012, 1, 12), getRandomInt(100, 1000)],
         [gd(2012, 1, 13), getRandomInt(100, 1000)],
         [gd(2012, 1, 14), getRandomInt(100, 1000)],
         [gd(2012, 1, 15), getRandomInt(100, 1000)],
         [gd(2012, 1, 16), getRandomInt(100, 1000)],
         [gd(2012, 1, 17), getRandomInt(100, 1000)],
         [gd(2012, 1, 18), getRandomInt(100, 1000)],
         [gd(2012, 1, 19), getRandomInt(100, 1000)],
         [gd(2012, 1, 20), getRandomInt(100, 1000)],
         [gd(2012, 1, 21), getRandomInt(100, 1000)],
         [gd(2012, 1, 22), getRandomInt(100, 1000)],
         [gd(2012, 1, 23), getRandomInt(100, 1000)],
         [gd(2012, 1, 24), getRandomInt(100, 1000)],
         [gd(2012, 1, 25), getRandomInt(100, 1000)],
         [gd(2012, 1, 26), getRandomInt(100, 1000)],
         [gd(2012, 1, 27), getRandomInt(100, 1000)],
         [gd(2012, 1, 28), getRandomInt(100, 1000)],
         [gd(2012, 1, 29), getRandomInt(100, 1000)],
         [gd(2012, 1, 30), getRandomInt(100, 1000)],
         [gd(2012, 1, 31), getRandomInt(100, 1000)]
    ];
	
	var data3 = [
         [gd(2012, 1, 1), 500],
         [gd(2012, 1, 2), 200],
         [gd(2012, 1, 3), 300],
         [gd(2012, 1, 4), 400],
         [gd(2012, 1, 5), 200],
         [gd(2012, 1, 6), 156],
         [gd(2012, 1, 7), 500],
         [gd(2012, 1, 8), 389],
         [gd(2012, 1, 9), 167],
         [gd(2012, 1, 10), 576],
         [gd(2012, 1, 11), 389],
         [gd(2012, 1, 12), 400],
         [gd(2012, 1, 13), 200],
         [gd(2012, 1, 14), 300],
         [gd(2012, 1, 15), 400],
         [gd(2012, 1, 16), 386],
         [gd(2012, 1, 17), 45],
         [gd(2012, 1, 18), 588],
         [gd(2012, 1, 19), 588],
         [gd(2012, 1, 20), 588],
         [gd(2012, 1, 21), 687],
         [gd(2012, 1, 22), 144],
         [gd(2012, 1, 23), 699],
         [gd(2012, 1, 24), 267],
         [gd(2012, 1, 25), 486],
         [gd(2012, 1, 26), 366],
         [gd(2012, 1, 27), 588],
         [gd(2012, 1, 28), 600],
         [gd(2012, 1, 29), 23],
         [gd(2012, 1, 30), 255],
         [gd(2012, 1, 31), 693]
    ];
	
	function getDataSet(period) {
		return dataset = [
           {
        	   label: "Registered users",
        	   grow: { stepMode: "linear" },
        	   data: $scope.getRegisterUsers(period),
        	   yaxis: 2,
        	   color: "#1c84c6",
        	   lines: { lineWidth: 1, show: true, fill: true, fillColor: { colors: [{ opacity: 0.1 }, { opacity: 0.1 }] } },
        	   points: { show: false }
           },
//		               {
//		            	   label: "Visits",
//		            	   grow: { stepMode: "linear" },
//		            	   data: data2,
//		            	   color: "#3dc7ab",
//		            	   bars: { show: true, align: "center", barWidth: 24 * 60 * 60 * 600, lineWidth: 0 }
//		               },
//		               {
//		            	   label: "Unique visits",
//		            	   grow: { stepMode: "linear" },
//		            	   data: data3,
//		            	   color: "#3dc7ab",
//		            	   bars: { show: true, align: "center", barWidth: 24 * 60 * 60 * 600, lineWidth: 0 }
//		               }
        ];
	}
	
	var options = {
        grid: { hoverable: true, clickable: true, tickColor: "#d5d5d5", borderWidth: 0, color: '#d5d5d5' },
        colors: ["#1ab394", "#464f88"],
        tooltip: true,
        xaxis: {
            mode: "time",
            tickSize: [2, "hour"],
            timeformat: "%H:%M"
        },
//        xaxis: {
//            mode: "time",
//            tickSize: [3, "day"],
//            tickLength: 0,
//            axisLabel: "Date",
//            axisLabelUseCanvas: true,
//            axisLabelFontSizePixels: 12,
//            axisLabelFontFamily: 'Arial',
//            axisLabelPadding: 10,
//            color: "#d5d5d5"
//        },
        yaxes: [
            {
                position: "left",
                max: 1070,
                color: "#d5d5d5",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Arial',
                axisLabelPadding: 3
            },
            {
                position: "right",
                color: "#d5d5d5",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: ' Arial',
                axisLabelPadding: 67
            }
        ],
        legend: {
        	show: true,
        	noColumns: 1,
            labelBoxBorderColor: "#d5d5d5",
            position: "nw"
        }
    };
	
	$scope.periodChanged = function() {
		$scope.flotData = getDataSet($scope.period);
	};

	$scope.period = 'today';
	$scope.flotData = getDataSet($scope.period);
	$scope.flotOptions = options;
};