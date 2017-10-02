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
        xaxis: { mode: "time", tickSize: [1, "day"], timeformat: "%d.%m.%Y" },
        yaxes: [{ position: "left" }, { position: "right" }],
        legend: { show: true, noColumns: 1, labelBoxBorderColor: "#d5d5d5", position: "nw" }	
	};
	
	var monthlyOptions = {
		grid: { hoverable: true, clickable: true, tickColor: "#d5d5d5", borderWidth: 0, color: '#d5d5d5' },
        tooltip: true,
        xaxis: { mode: "time", tickSize: [1, "month"], timeformat: "%m.%Y" },
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
	
	// styles for PDF make
	var pdfMakeStyles = {
		header: {
			fontSize: 14,
			bold: true,
			margin: [0, 0, 0, 3]
		},
		subheader: {
			fontSize: 10,
			bold: false,
			margin: [0, 0, 0, 15]
		},
		tableContent: {
			margin: [0, 15, 0, 15]
		},
		tableHeader: {
			bold: true,
			fontSize: 8
		},
		tableRow: {
			fontSize: 8
		}	
	};
	
	/**
	 * Function returns version of IE or false, if browser is not Internet Explorer.
	 */
	function detectIE() {
		var ua = window.navigator.userAgent;

		var msie = ua.indexOf('MSIE ');
		if (msie > 0) {
			// IE 10 or older => return version number
			return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
		}

		var trident = ua.indexOf('Trident/');
		if (trident > 0) {
			// IE 11 => return version number
			var rv = ua.indexOf('rv:');
			return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
		}

		var edge = ua.indexOf('Edge/');
		if (edge > 0) {
			// Edge (IE 12+) => return version number
			return parseInt(ua.substring(edge + 5, ua.indexOf('.', edge)), 10);
		}

		// other browser
		return false;
	};
	
	$scope.exportPDF = function() {
		var subheader = '';
		
		if ($scope.type == 'users') {
			subheader = $translate('DASHBOARD_USERS');
		} else if ($scope.type == 'visits') {
			subheader = $translate('DASHBOARD_VISITS');
		} else if ($scope.type == 'tenders') {
			subheader = $translate('DASHBOARD_TENDERS');
		}
		
		if ($scope.period == 'daily') {
			subheader = subheader + ' - ' + $translate('STATISTICS_PERIOD_LAST_7_DAYS');
		} else if ($scope.period == 'monthly') {
			subheader = subheader + ' - ' + $translate('STATISTICS_PERIOD_LAST_6_MONTHS');
		}
		
		html2canvas($('#trendsChart')).then(function(srcCanvas) {
			// create a dummy canvas
			destCanvas = document.createElement("canvas");
			destCanvas.width = srcCanvas.width;
			destCanvas.height = srcCanvas.height;

			destCtx = destCanvas.getContext('2d');

			// create a rectangle with the desired color
			destCtx.fillStyle = "#FFFFFF";
			destCtx.fillRect(0, 0, srcCanvas.width, srcCanvas.height);

			// draw the original canvas onto the destination canvas
			destCtx.drawImage(srcCanvas, 0, 0);
			
			var docDefinition = {
				pageSize: 'A4',
				pageOrientation: 'landscape',
				content: [
				    { text: 'Trendovi', style: 'header' },
				    { text: subheader, style: 'subheader' },
				    {
				        image: destCanvas.toDataURL('image/jpeg'),
				        width: 800
					}
				],
				styles: pdfMakeStyles
			};
			
			if (detectIE()) {
				pdfMake.createPdf(docDefinition).download("trendovi.pdf");
			} else {
				pdfMake.createPdf(docDefinition).download("trendovi.pdf");
			}
		});
	};
	
	$scope.period = 'daily';	
	$scope.type = 'users';
	$scope.getChartData($scope.period, $scope.type);
	$scope.getData();
};