angular.module('FundFinder')
	.controller('DashboardController', DashboardController);

// ========================================================================
//	OVERVIEW CONTROLLER
// ========================================================================
function DashboardController($rootScope, $scope, $state, $log, $timeout, $filter, uiGridConstants, DashboardService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
};