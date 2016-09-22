angular.module('FundFinder')
	.controller('ArticlesController', ArticlesController);

// ========================================================================
//	OVERVIEW CONTROLLER
// ========================================================================
function ArticlesController($rootScope, $scope, $state, $log, $timeout, $filter, $sce, ArticlesService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.findAll = function() {
		ArticlesService.findAll()
			.success(function(data, status) {
				if (status == 200) {
					$scope.articles = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.toTrusted = function(html) {
	    return $sce.trustAsHtml(html);
	};
	
	$scope.showDetails = function(article) {
		alert('details for ' + article.name);
	};
	
	// initial load
	$scope.findAll();
};
