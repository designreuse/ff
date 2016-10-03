angular.module('FundFinder')

.constant("constants", {
    "contextPath": "/fundfinder-admin"
})

.config(function config($provide, $stateProvider, $httpProvider, $urlRouterProvider, cfpLoadingBarProvider, constants) {
	// configure interceptor
	$httpProvider.interceptors.push(function($location) {  
		var path = {
				request: function(config) {
					if (config.url.indexOf("template/datepicker") != -1 || config.url.indexOf("ui-grid") != -1 || config.url.indexOf("uib") != -1) {
						// workaround for problem with loading datepicker templates
					} else {
						config.url = constants.contextPath + config.url;
					}
					return config;
				},
				response: function(response) {
					return response;
				}
			};
			return path;
		}
	);
	
	// by default, the loading bar will only display after it has been waiting for a response for over 500ms
	cfpLoadingBarProvider.latencyThreshold = 500;
	cfpLoadingBarProvider.includeBar = false;
	cfpLoadingBarProvider.includeSpinner = false;
		
	// 'getPrincipal' function
	var getPrincipal = function($rootScope, $http, $log) {
		if (!$rootScope.principal) {
			$log.info('Getting principal');
			
			// get data synchronously
			var request = new XMLHttpRequest();
			request.open('GET', constants.contextPath + '/principal', false);
			request.send(null);
			
			if (request.status == 200) {
				$rootScope.principal = jQuery.parseJSON(request.response);
			} else {
				$log.error('Error occurred while getting principal!', status, data);
			}
		}
	};
	
	$urlRouterProvider.when('', '/dashboard/overview').otherwise('/dashboard/overview');
	
	$stateProvider
		// DASHBOARD
		.state('dashboard', {
	        abstract: true,
	        url: "/dashboard",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal,
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/dashboard/scripts/controllers.js',
	        			        'components/dashboard/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('dashboard.overview', {
	        url: "/overview",
	        templateUrl: "/components/dashboard/views/overview.html",
	        controller: 'DashboardController',
	        data: { pageTitle: 'Dashboard' }
	    })
	    
		// USERS
		.state('users', {
	        abstract: true,
	        url: "/users",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal,
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/users/scripts/controllers.js',
	        			        'components/users/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('users.overview', {
	        url: "/overview",
	        templateUrl: "/components/users/views/overview.html",
	        controller: 'UsersOverviewController',
	        data: { pageTitle: 'Users overview' }
	    })
		.state('users.details', {
	        url: "/details/:id",
	        templateUrl: "/components/users/views/details.html",
	        controller: 'UsersDetailsController',
	        params: { 'id' : null },
	        data: { pageTitle: 'User details' }
	    })
	    
		// TENDERS
		.state('tenders', {
	        abstract: true,
	        url: "/tenders",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal,
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/tenders/scripts/controllers.js',
	        			        'components/tenders/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('tenders.overview', {
	        url: "/overview",
	        templateUrl: "/components/tenders/views/overview.html",
	        controller: 'TendersOverviewController',
	        data: { pageTitle: 'Tenders overview' }
	    })
		.state('tenders.details', {
	        url: "/details/:id",
	        templateUrl: "/components/tenders/views/details.html",
	        controller: 'TendersDetailsController',
	        params: { 'id' : null, 'showEditButton' : false },
	        data: { pageTitle: 'Tender details' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/impressions/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('tenders.edit', {
	        url: "/edit/:id",
	        templateUrl: "/components/tenders/views/edit.html",
	        controller: 'TendersEditController',
	        params: { 'id' : null },
	        data: { pageTitle: 'Edit tender' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/counties/scripts/services.js',
	        			        'components/nkds/scripts/services.js',
	        			        'components/investments/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
		// INVESTMENTS
		.state('investments', {
	        abstract: true,
	        url: "/investments",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal,
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/investments/scripts/controllers.js',
	        			        'components/investments/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('investments.overview', {
	        url: "/overview",
	        templateUrl: "/components/investments/views/overview.html",
	        controller: 'InvestmentsOverviewController',
	        data: { pageTitle: 'Investments overview' }
	    })
		.state('investments.details', {
	        url: "/details/:id",
	        templateUrl: "/components/investments/views/details.html",
	        controller: 'InvestmentsDetailsController',
	        params: { 'id' : null, 'showEditButton' : false },
	        data: { pageTitle: 'Investment details' }
	    })
	    .state('investments.edit', {
	        url: "/edit/:id",
	        templateUrl: "/components/investments/views/edit.html",
	        controller: 'InvestmentsEditController',
	        params: { 'id' : null },
	        data: { pageTitle: 'Edit investment' }
	    })
	    
	    // ARTICLES
		.state('articles', {
	        abstract: true,
	        url: "/articles",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal,
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/articles/scripts/controllers.js',
	        			        'components/articles/scripts/services.js',
	        			        'components/impressions/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('articles.overview', {
	        url: "/overview",
	        templateUrl: "/components/articles/views/overview.html",
	        controller: 'ArticlesOverviewController',
	        data: { pageTitle: 'Articles overview' }
	    })
		.state('articles.details', {
	        url: "/details/:id",
	        templateUrl: "/components/articles/views/details.html",
	        controller: 'ArticlesDetailsController',
	        params: { 'id' : null },
	        data: { pageTitle: 'Article details' }
	    })
	    .state('articles.edit', {
	        url: "/edit/:id",
	        templateUrl: "/components/articles/views/edit.html",
	        controller: 'ArticlesEditController',
	        params: { 'id' : null },
	        data: { pageTitle: 'Edit article' }
	    })
	
		.state('settings', {
	        abstract: true,
	        url: "/settings",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal
	    })
	    
	    .state('settings.items_overview_company', {
	    	url: "/:entityType/companyitems/overview",
	    	params: { 'entityType' : null },
	        templateUrl: "/components/items/views/overview.html",
	        controller: 'ItemsOverviewController',
	        data: { pageTitle: 'Items overview' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/items/scripts/controllers.js',
	        			        'components/items/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('settings.items_details_company', {
	    	url: "/:entityType/companyitems/details/:id",
	    	params: { 'entityType' : null, 'id' : null },
	        templateUrl: "/components/items/views/details.html",
	        controller: 'ItemsDetailsController',
	        data: { pageTitle: 'Item details' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/items/scripts/controllers.js',
	        			        'components/items/scripts/services.js',
	        			        'components/cities/scripts/services.js',
	        			        'components/counties/scripts/services.js',
	        			        'components/nkds/scripts/services.js',
	        			        'components/investments/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('settings.items_edit_company', {
	    	url: "/:entityType/companyitems/edit/:id",
	    	params: { 'entityType' : null, 'id' : null },
	        templateUrl: "/components/items/views/edit.html",
	        controller: 'ItemsEditController',
	        data: { pageTitle: 'Edit item' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/items/scripts/controllers.js',
	        			        'components/items/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
	    .state('settings.items_overview_tender', {
	    	url: "/:entityType/tenderitems/overview",
	    	params: { 'entityType' : null },
	        templateUrl: "/components/items/views/overview.html",
	        controller: 'ItemsOverviewController',
	        data: { pageTitle: 'Items overview' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/items/scripts/controllers.js',
	        			        'components/items/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('settings.items_details_tender', {
	    	url: "/:entityType/tenderitems/details/:id",
	    	params: { 'entityType' : null, 'id' : null },
	        templateUrl: "/components/items/views/details.html",
	        controller: 'ItemsDetailsController',
	        data: { pageTitle: 'Item details' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/items/scripts/controllers.js',
	        			        'components/items/scripts/services.js',
	        			        'components/cities/scripts/services.js',
	        			        'components/counties/scripts/services.js',
	        			        'components/nkds/scripts/services.js',
	        			        'components/investments/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('settings.items_edit_tender', {
	    	url: "/:entityType/tenderitems/edit/:id",
	    	params: { 'entityType' : null, 'id' : null },
	        templateUrl: "/components/items/views/edit.html",
	        controller: 'ItemsEditController',
	        data: { pageTitle: 'Edit item' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/items/scripts/controllers.js',
	        			        'components/items/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
	    .state('settings.algorithmitems_overview', {
	    	url: "/algorithmitems/overview",
	        templateUrl: "/components/algorithmitems/views/overview.html",
	        controller: 'AlgorithmItemsOverviewController',
	        data: { pageTitle: 'Algorithm items overview' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/algorithmitems/scripts/controllers.js',
	        			        'components/algorithmitems/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('settings.algorithmitems_edit', {
	    	url: "/algorithmitems/edit/:id",
	    	params: { 'id' : null },
	        templateUrl: "/components/algorithmitems/views/edit.html",
	        controller: 'AlgorithmItemsEditController',
	        data: { pageTitle: 'Edit algorithm item' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/algorithmitems/scripts/controllers.js',
	        			        'components/algorithmitems/scripts/services.js',
	        			        'components/items/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
	    .state('settings.cities_overview', {
	    	url: "/cities/overview",
	        templateUrl: "/components/cities/views/overview.html",
	        controller: 'CitiesOverviewController',
	        data: { pageTitle: 'Cities overview' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/cities/scripts/controllers.js',
	        			        'components/cities/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('settings.cities_edit', {
	    	url: "/cities/edit/:id",
	    	params: { 'id' : null },
	        templateUrl: "/components/cities/views/edit.html",
	        controller: 'CitiesEditController',
	        data: { pageTitle: 'Edit city' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/cities/scripts/controllers.js',
	        			        'components/cities/scripts/services.js',
	        			        'components/counties/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
	    .state('settings.counties_overview', {
	    	url: "/counties/overview",
	        templateUrl: "/components/counties/views/overview.html",
	        controller: 'CountiesOverviewController',
	        data: { pageTitle: 'Counties overview' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/counties/scripts/controllers.js',
	        			        'components/counties/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('settings.counties_edit', {
	    	url: "/counties/edit/:id",
	    	params: { 'id' : null },
	        templateUrl: "/components/counties/views/edit.html",
	        controller: 'CountiesEditController',
	        data: { pageTitle: 'Edit county' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/counties/scripts/controllers.js',
	        			        'components/counties/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
	    .state('settings.nkds_overview', {
	    	url: "/nkds/overview",
	        templateUrl: "/components/nkds/views/overview.html",
	        controller: 'NkdsOverviewController',
	        data: { pageTitle: 'NKD overview' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/nkds/scripts/controllers.js',
	        			        'components/nkds/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('settings.nkds_edit', {
	    	url: "/nkds/edit/:id",
	    	params: { 'id' : null },
	        templateUrl: "/components/nkds/views/edit.html",
	        controller: 'NkdsEditController',
	        data: { pageTitle: 'Edit NKD' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/nkds/scripts/controllers.js',
	        			        'components/nkds/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
})

.run(function ($rootScope, $state, $stateParams, $log, $localStorage, ModalService, constants, CountersService) {
	$rootScope.$state = $state;
	$rootScope.$stateParams = $stateParams;
	
	$rootScope.version = "v1.0.0";
	$rootScope.helpEnabled = true; // indicates if help feature is enabled or not
	
	$rootScope.dateFormat = "yyyy-MM-dd";
	$rootScope.dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
	
	$rootScope.rowHeight = 30;
	$rootScope.paginationPageSize = 10;
	$rootScope.paginationPageSizes = [10, 25, 50, 100];
	$rootScope.heightNoData = 300;
	$rootScope.heightNoDataRevisions = 88;
	$rootScope.heightCorrectionFactor = 90;
	$rootScope.heightCorrectionFactorRevisions = 60;
	
	// toastr default settings
	toastr.options.preventDuplicates = true;
	toastr.options.closeButton = true;
	
	// keep track of previous and current state via root scope variables
	$rootScope.previousState;
	$rootScope.currentState;
	$rootScope.$on('$stateChangeSuccess', function(ev, to, toParams, from, fromParams) {
	    $rootScope.previousState = from.name;
	    $rootScope.previousStateParams = fromParams;
	    $rootScope.currentState = to.name;
	    $rootScope.currentStateParams = toParams;
	});
	
	$rootScope.getRandom = function () {
		return random = $.now() + "" + Math.floor((Math.random() * 1000) + 1);
	}
	
	$rootScope.processDateFilters = function (creationDateEl, lastModifiedDateEl, filterArray) {
		if (creationDateEl && creationDateEl.val() && creationDateEl.val().length > 0) {
			filterArray.push({ "name" : "creationDate", "term" : creationDateEl.val() });
		}

		if (lastModifiedDateEl && lastModifiedDateEl.val() && lastModifiedDateEl.val().length > 0) {
			filterArray.push({ "name" : "lastModifiedDate", "term" :  lastModifiedDateEl.val() });
		}
	}
	
	$rootScope.setDateFilters = function (creationDateEl, lastModifiedDateEl, filterArray) {
		for(var i in filterArray) {
			var filter = filterArray[i];
			
			if(filter.name == "creationDate" && creationDateEl != undefined) {
				creationDateEl.val(filter.term);
			}
			if(filter.name == "lastModifiedDate" && lastModifiedDateEl != undefined) {
				lastModifiedDateEl.val(filter.term);
			}
		}
	}

	$rootScope.saveGridState = function (view, gridApi, creationDateEl, lastModifiedDateEl) {
		if(view != undefined && gridApi != undefined && gridApi.saveState != undefined) {
			if($localStorage.gridStates == undefined) {
				$localStorage.gridStates = new Array();
			}
			var gridState = gridApi.saveState.save();
			var dateFilters = new Array();
			$rootScope.processDateFilters(creationDateEl, lastModifiedDateEl, dateFilters);
			
			$localStorage.gridStates[view] = {
				gridState: gridState,
				dateFilters: dateFilters
			};
		}
	}
	
	$rootScope.restoreGridState = function (view, gridApi, creationDateEl, lastModifiedDateEl) {
		if(view != undefined && $localStorage.gridStates != undefined && $localStorage.gridStates[view] != undefined
				&& gridApi != undefined && gridApi.saveState != undefined) {
			var state = $localStorage.gridStates[view];
			
			gridApi.saveState.restore(null, state.gridState);
			$rootScope.setDateFilters(creationDateEl, lastModifiedDateEl, state.dateFilters);
		}
	}
	
	$log.info("Registering event listener for SSE");
	var source = new EventSource(constants.contextPath + '/api/v1/sse/emitter');
	source.addEventListener('FundFinder', function(event) {
		var data = JSON.parse(event.data);
		if (data.type == 'COUNTERS_UPDATE') {
			$rootScope.totalUsers = data.counters.cntUsers;
			$rootScope.totalTenders = data.counters.cntTenders;
			$rootScope.totalInvestments = data.counters.cntInvestments;
			$rootScope.totalArticles = data.counters.cntArticles;
		}
	});
	
	CountersService.findAll()
		.success(function(data, status, headers, config) {
			if (status == 200) {
				$rootScope.totalUsers = data.cntUsers;
				$rootScope.totalTenders = data.cntTenders;
				$rootScope.totalInvestments = data.cntInvestments;
				$rootScope.totalArticles = data.cntArticles;	
			} else {
				$log.error(data);
				$rootScope.totalUsers = 'n/a';
				$rootScope.totalTenders = 'n/a';
				$rootScope.totalInvestments = 'n/a';
				$rootScope.totalArticles = 'n/a';	
			}
		})
		.error(function(data, status, headers, config) {
			$log.error(data);
			$rootScope.totalUsers = 'n/a';
			$rootScope.totalTenders = 'n/a';
			$rootScope.totalInvestments = 'n/a';
			$rootScope.totalArticles = 'n/a';
		});
});