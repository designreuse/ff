angular.module('FundFinder')
	.controller('TendersOverviewController', TendersOverviewController);

// ========================================================================
//	OVERVIEW CONTROLLER
// ========================================================================
function TendersOverviewController($rootScope, $scope, $state, $log, $timeout, $filter, uiGridConstants, TendersService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
};
