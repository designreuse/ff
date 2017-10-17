angular.module('FundFinder')

.constant("constants", {

})

.config(function config($provide, $stateProvider, $httpProvider, $sceProvider, $urlRouterProvider, cfpLoadingBarProvider, constants) {
	// disable SCE (Strict Contextual Escaping)
	$sceProvider.enabled(false);
	
	// configure interceptor
	$httpProvider.interceptors.push(function($window, $location) {  
		var path = {
				request: function(config) {
					if (config.url.indexOf("template/datepicker") != -1 || config.url.indexOf("ui-grid") != -1 || config.url.indexOf("uib") != -1) {
						// workaround for problem with loading datepicker templates
					} else {
						config.url = $window.location.pathname.substring(0, window.location.pathname.lastIndexOf("/")) + config.url;
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
	var getPrincipal = function($rootScope, $window, $http, $log) {
		if (!$rootScope.principal) {
			$log.info('Getting principal');
			
			// get data synchronously
			var request = new XMLHttpRequest();
			request.open('GET', $window.location.pathname.substring(0, window.location.pathname.lastIndexOf("/")) + '/principal', false);
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
	
		// ==========================================
		// 	SECURITY
  		// ==========================================
		.state('security', {
	        abstract: true,
	        url: "/security",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal
	    })
	    .state('security.denied', {
	    	url: "/denied",
	        templateUrl: "/views/common/denied.html",
	        controller: 'DeniedController',
	        data: { pageTitle: 'Access denied' }
	    })
	    
		// ==========================================
		// 	DASHBOARD
		// ==========================================
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
	    
	    // ==========================================
	    // 	STATISTICS
	    // ==========================================
		.state('statistics', {
	        abstract: true,
	        url: "/statistics",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal,
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/statistics/scripts/controllers.js',
	        			        'components/statistics/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('statistics.overview', {
	        url: "/overview",
	        templateUrl: "/components/statistics/views/overview.html",
	        controller: 'StatisticsController',
	        data: { pageTitle: 'Statistics' }
	    })
	    
	    // ==========================================
		// 	USERS
	    // ==========================================
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
	        params: { 'permission' : 'users' },
	        data: { pageTitle: 'Users overview' },
	        resolve: {
	        	hasPermission: function(grant, $stateParams) {
	        		return grant.only({ test: 'hasPermission', state: 'security.denied' }, $stateParams);
	        	}
	        }
	    })
		.state('users.details', {
	        url: "/details/:id",
	        templateUrl: "/components/users/views/details.html",
	        controller: 'UsersDetailsController',
	        params: { 'id' : null, 'permission' : 'users.read' },
	        data: { pageTitle: 'User details' },
	        resolve: {
	        	hasPermission: function(grant, $stateParams) {
	        		return grant.only({ test: 'hasPermission', state: 'security.denied' }, $stateParams);
	        	},
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/useremails/scripts/services.js',
	        			        'components/emails/scripts/services.js',
	        			        'components/businessrelationshipmanagers/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
	    // ==========================================
		// 	GFI SYNCS
	    // ==========================================
		.state('gfisyncs', {
	        abstract: true,
	        url: "/gfisyncs",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal,
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/gfisyncs/scripts/controllers.js',
	        			        'components/gfisyncs/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('gfisyncs.overview', {
	        url: "/overview",
	        templateUrl: "/components/gfisyncs/views/overview.html",
	        controller: 'GfiSyncsOverviewController',
	        params: { 'permission' : 'users.update' },
	        data: { pageTitle: 'GFI syncs overview' },
	        resolve: {
	        	hasPermission: function(grant, $stateParams) {
	        		return grant.only({ test: 'hasPermission', state: 'security.denied' }, $stateParams);
	        	}
	        }
	    })
		.state('gfisyncs.details', {
	        url: "/details/:id",
	        templateUrl: "/components/gfisyncs/views/details.html",
	        controller: 'GfiSyncsDetailsController',
	        params: { 'id' : null, 'permission' : 'users.update' },
	        data: { pageTitle: 'GFI sync details' },
	        resolve: {
	        	hasPermission: function(grant, $stateParams) {
	        		return grant.only({ test: 'hasPermission', state: 'security.denied' }, $stateParams);
	        	}
	        }
	    })
	    
	    // ==========================================
		// 	TENDERS
	    // ==========================================
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
	        params: { 'permission' : 'tenders' },
	        data: { pageTitle: 'Tenders overview' },
	        resolve: {
	        	hasPermission: function(grant, $stateParams) {
	        		return grant.only({ test: 'hasPermission', state: 'security.denied' }, $stateParams);
	        	},
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/images/scripts/services.js']
	        		});
	        	}
	        }
	    })
		.state('tenders.details', {
	        url: "/details/:id",
	        templateUrl: "/components/tenders/views/details.html",
	        controller: 'TendersDetailsController',
	        params: { 'id' : null, 'showEditButton' : false, 'permission' : 'tenders.read' },
	        data: { pageTitle: 'Tender details' },
	        resolve: {
	        	hasPermission: function(grant, $stateParams) {
	        		return grant.only({ test: 'hasPermission', state: 'security.denied' }, $stateParams);
	        	},
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/impressions/scripts/services.js',
	        			        'components/users/scripts/services.js',
	        			        'components/usergroups/scripts/services.js',
	        			        'components/emails/scripts/services.js',
	        			        'components/useremails/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('tenders.edit', {
	        url: "/edit/:id",
	        templateUrl: "/components/tenders/views/edit.html",
	        controller: 'TendersEditController',
	        params: { 'id' : null, 'permission' : 'tenders.update' },
	        data: { pageTitle: 'Edit tender' },
	        resolve: {
	        	hasPermission: function(grant, $stateParams) {
	        		return grant.only({ test: 'hasPermission', state: 'security.denied' }, $stateParams);
	        	},
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/subdivisions1/scripts/services.js',
	        			        'components/subdivisions2/scripts/services.js',
	        			        'components/activities/scripts/services.js',
	        			        'components/currencies/scripts/services.js',
	        			        'components/investments/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
	    // ==========================================
		// 	INVESTMENTS
	    // ==========================================
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
	        params: { 'permission' : 'investments' },
	        data: { pageTitle: 'Investments overview' },
	        resolve: {
	        	hasPermission: function(grant, $stateParams) {
	        		return grant.only({ test: 'hasPermission', state: 'security.denied' }, $stateParams);
	        	}
	        }
	    })
		.state('investments.details', {
	        url: "/details/:id",
	        templateUrl: "/components/investments/views/details.html",
	        controller: 'InvestmentsDetailsController',
	        params: { 'id' : null, 'showEditButton' : false, 'permission' : 'investments.read' },
	        data: { pageTitle: 'Investment details' },
	        resolve: {
	        	hasPermission: function(grant, $stateParams) {
	        		return grant.only({ test: 'hasPermission', state: 'security.denied' }, $stateParams);
	        	}
	        }
	    })
	    .state('investments.edit', {
	        url: "/edit/:id",
	        templateUrl: "/components/investments/views/edit.html",
	        controller: 'InvestmentsEditController',
	        params: { 'id' : null, 'permission' : 'investments.update' },
	        data: { pageTitle: 'Edit investment' },
	        resolve: {
	        	hasPermission: function(grant, $stateParams) {
	        		return grant.only({ test: 'hasPermission', state: 'security.denied' }, $stateParams);
	        	}
	        }
	    })
	    
	    // ==========================================
	    // 	ARTICLES
	    // ==========================================
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
	        params: { 'permission' : 'articles' },
	        data: { pageTitle: 'Articles overview' },
	        resolve: {
	        	hasPermission: function(grant, $stateParams) {
	        		return grant.only({ test: 'hasPermission', state: 'security.denied' }, $stateParams);
	        	}
	        }
	    })
		.state('articles.details', {
	        url: "/details/:id",
	        templateUrl: "/components/articles/views/details.html",
	        controller: 'ArticlesDetailsController',
	        params: { 'id' : null, 'permission' : 'articles.read' },
	        data: { pageTitle: 'Article details' },
	        resolve: {
	        	hasPermission: function(grant, $stateParams) {
	        		return grant.only({ test: 'hasPermission', state: 'security.denied' }, $stateParams);
	        	}
	        }
	    })
	    .state('articles.edit', {
	        url: "/edit/:id",
	        templateUrl: "/components/articles/views/edit.html",
	        controller: 'ArticlesEditController',
	        params: { 'id' : null, 'permission' : 'articles.update' },
	        data: { pageTitle: 'Edit article' },
	        resolve: {
	        	hasPermission: function(grant, $stateParams) {
	        		return grant.only({ test: 'hasPermission', state: 'security.denied' }, $stateParams);
	        	}
	        }
	    })
	    
	    // ==========================================
		// 	CONTACTS
	    // ==========================================
		.state('contacts', {
	        abstract: true,
	        url: "/contacts",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal,
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/contacts/scripts/controllers.js',
	        			        'components/contacts/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('contacts.overview', {
	        url: "/overview",
	        templateUrl: "/components/contacts/views/overview.html",
	        controller: 'ContactsOverviewController',
	        params: { 'permission' : 'contacts' },
	        data: { pageTitle: 'Contacts overview' },
	        resolve: {
	        	hasPermission: function(grant, $stateParams) {
	        		return grant.only({ test: 'hasPermission', state: 'security.denied' }, $stateParams);
	        	}
	        }
	    })
		.state('contacts.details', {
	        url: "/details/:id",
	        templateUrl: "/components/contacts/views/details.html",
	        controller: 'ContactsDetailsController',
	        params: { 'id' : null, 'permission' : 'contacts.read' },
	        data: { pageTitle: 'Contact details' },
	        resolve: {
	        	hasPermission: function(grant, $stateParams) {
	        		return grant.only({ test: 'hasPermission', state: 'security.denied' }, $stateParams);
	        	}
	        }
	    })
	
	    // ==========================================
		// 	E-MAIL
	    // ==========================================
		.state('emails', {
	        abstract: true,
	        url: "/emails",
	        templateUrl: "/views/common/content.html",
	        onEnter: getPrincipal,
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/emails/scripts/controllers.js',
	        			        'components/emails/scripts/services.js',
	        			        'components/usergroups/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('emails.send', {
	        url: "/send",
	        templateUrl: "/components/emails/views/send.html",
	        controller: 'EmailsSendController',
	        data: { pageTitle: 'Send e-mail' }
	    })
	    
	    // ==========================================
	    // 	SETTINGS
	    // ==========================================
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
	        			        'components/subdivisions1/scripts/services.js',
	        			        'components/subdivisions2/scripts/services.js',
	        			        'components/activities/scripts/services.js',
	        			        'components/currencies/scripts/services.js',
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
	        			        'components/subdivisions1/scripts/services.js',
	        			        'components/subdivisions2/scripts/services.js',
	        			        'components/activities/scripts/services.js',
	        			        'components/currencies/scripts/services.js',
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
	    
	    .state('settings.activities_overview', {
	    	url: "/activities/overview",
	        templateUrl: "/components/activities/views/overview.html",
	        controller: 'ActivitiesOverviewController',
	        data: { pageTitle: 'Activities overview' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/activities/scripts/controllers.js',
	        			        'components/activities/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('settings.activities_edit', {
	    	url: "/activities/edit/:id",
	    	params: { 'id' : null },
	        templateUrl: "/components/activities/views/edit.html",
	        controller: 'ActivitiesEditController',
	        data: { pageTitle: 'Edit activity' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/activities/scripts/controllers.js',
	        			        'components/activities/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
	    .state('settings.subdivisions1_overview', {
	    	url: "/subdivisions1/overview",
	        templateUrl: "/components/subdivisions1/views/overview.html",
	        controller: 'Subdivisions1OverviewController',
	        data: { pageTitle: 'Counties overview' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/subdivisions1/scripts/controllers.js',
	        			        'components/subdivisions1/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('settings.subdivisions1_edit', {
	    	url: "/subdivisions1/edit/:id",
	    	params: { 'id' : null },
	        templateUrl: "/components/subdivisions1/views/edit.html",
	        controller: 'Subdivisions1EditController',
	        data: { pageTitle: 'Edit county' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/subdivisions1/scripts/controllers.js',
	        			        'components/subdivisions1/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
	    .state('settings.subdivisions2_overview', {
	    	url: "/subdivisions2/overview",
	        templateUrl: "/components/subdivisions2/views/overview.html",
	        controller: 'Subdivisions2OverviewController',
	        data: { pageTitle: 'Cities overview' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/subdivisions2/scripts/controllers.js',
	        			        'components/subdivisions2/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('settings.subdivisions2_edit', {
	    	url: "/subdivisions2/edit/:id",
	    	params: { 'id' : null },
	        templateUrl: "/components/subdivisions2/views/edit.html",
	        controller: 'Subdivisions2EditController',
	        data: { pageTitle: 'Edit city' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/subdivisions2/scripts/controllers.js',
	        			        'components/subdivisions2/scripts/services.js',
	        			        'components/subdivisions1/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
	    .state('settings.roles_overview', {
	    	url: "/roles/overview",
	        templateUrl: "/components/roles/views/overview.html",
	        controller: 'RolesOverviewController',
	        data: { pageTitle: 'Roles overview' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/roles/scripts/controllers.js',
	        			        'components/roles/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('settings.roles_edit', {
	    	url: "/roles/edit/:id",
	    	params: { 'id' : null },
	        templateUrl: "/components/roles/views/edit.html",
	        controller: 'RolesEditController',
	        data: { pageTitle: 'Edit role' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/roles/scripts/controllers.js',
	        			        'components/roles/scripts/services.js',
	        			        'components/permissions/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
	    .state('settings.usergroups_overview', {
	    	url: "/usergroups/overview",
	        templateUrl: "/components/usergroups/views/overview.html",
	        controller: 'UserGroupsOverviewController',
	        data: { pageTitle: 'User groups overview' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/usergroups/scripts/controllers.js',
	        			        'components/usergroups/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('settings.usergroups_edit', {
	    	url: "/usergroups/edit/:id",
	    	params: { 'id' : null },
	        templateUrl: "/components/usergroups/views/edit.html",
	        controller: 'UserGroupsEditController',
	        data: { pageTitle: 'Edit user group' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/usergroups/scripts/controllers.js',
	        			        'components/usergroups/scripts/services.js',
	        			        'components/users/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
	    .state('settings.businessrelationshipmanagers_overview', {
	    	url: "/businessrelationshipmanagers/overview",
	        templateUrl: "/components/businessrelationshipmanagers/views/overview.html",
	        controller: 'BusinessRelationshipManagerOverviewController',
	        data: { pageTitle: 'Business relationship managers overview' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/businessrelationshipmanagers/scripts/controllers.js',
	        			        'components/businessrelationshipmanagers/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('settings.businessrelationshipmanagers_edit', {
	    	url: "/businessrelationshipmanagers/edit/:id",
	    	params: { 'id' : null },
	        templateUrl: "/components/businessrelationshipmanagers/views/edit.html",
	        controller: 'BusinessRelationshipManagerEditController',
	        data: { pageTitle: 'Edit business relationship manager' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/businessrelationshipmanagers/scripts/controllers.js',
	        			        'components/businessrelationshipmanagers/scripts/services.js',
	        			        'components/organizationalunits/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
	    .state('settings.organizationalunits_overview', {
	    	url: "/organizationalunits/overview",
	        templateUrl: "/components/organizationalunits/views/overview.html",
	        controller: 'OrganizationalUnitsOverviewController',
	        data: { pageTitle: 'Organizational units overview' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/organizationalunits/scripts/controllers.js',
	        			        'components/organizationalunits/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    .state('settings.organizationalunits_edit', {
	    	url: "/organizationalunits/edit/:id",
	    	params: { 'id' : null },
	        templateUrl: "/components/organizationalunits/views/edit.html",
	        controller: 'OrganizationalUnitsEditController',
	        data: { pageTitle: 'Edit organizational unit' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/organizationalunits/scripts/controllers.js',
	        			        'components/organizationalunits/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
	    .state('settings.configparams', {
	    	url: "/configparams",
	        templateUrl: "/components/configparams/views/edit.html",
	        controller: 'ConfigParamsController',
	        data: { pageTitle: 'Config params' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/configparams/scripts/controllers.js',
	        			        'components/configparams/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
	    .state('settings.debugging', {
	    	url: "/debugging/overview",
	        templateUrl: "/components/debugging/views/overview.html",
	        controller: 'DebuggingController',
	        data: { pageTitle: 'Debugging' },
	        resolve: {
	        	loadPlugin: function ($ocLazyLoad) {
	        		return $ocLazyLoad.load({
	        			name: 'FundFinder',
	        			files: ['components/debugging/scripts/controllers.js',
	        			        'components/debugging/scripts/services.js',
	        			        'components/users/scripts/services.js',
	        			        'components/tenders/scripts/services.js']
	        		});
	        	}
	        }
	    })
	    
})

.run(function ($rootScope, $window, $state, $stateParams, $log, $localStorage, $timeout, $locale, $templateCache, $filter, i18nService, ModalService, grant, constants) {
	var $translate = $filter('translate');
	
	$rootScope.$state = $state;
	$rootScope.$stateParams = $stateParams;
	
	$rootScope.version = "v1.0.21";
	$rootScope.contextPath = $window.location.pathname.substring(0, window.location.pathname.lastIndexOf("/"));
	$rootScope.helpEnabled = false; // indicates if help feature is enabled or not
	
	$locale.NUMBER_FORMATS.DECIMAL_SEP= ',';
	$locale.NUMBER_FORMATS.GROUP_SEP= '.';
	
	$rootScope.dateFormat = "dd.MM.yyyy";
	$rootScope.dateTimeFormat = "dd.MM.yyyy HH:mm:ss";
	$rootScope.dateFormatDB = "yyyy-MM-dd";
	$rootScope.dateTimeFormatDB = "yyyy-MM-dd HH:mm:ss";
	
	$rootScope.dpDaysOfWeek = [ "Ned", "Pon", "Uto", "Sri", "\u010cet", "Pet", "Sub" ];
	$rootScope.dpMonthNames = [ "Sije\u010danj", "Velja\u010da", "O\u017eujak", "Travanj", "Svibanj", "Lipanj", "Srpanj", "Kolovoz", "Rujan", "Listopad", "Studeni", "Prosinac" ];
	
	$rootScope.rowHeight = 30;
	$rootScope.paginationPageSize = 10;
	$rootScope.paginationPageSizes = [10, 25, 50, 100];
	$rootScope.heightNoData = 300;
	$rootScope.heightNoDataRevisions = 88;
	$rootScope.heightCorrectionFactor = 90;
	$rootScope.heightCorrectionFactorRevisions = 60;
	
	// ui-grid export
	$rootScope.exporterPdfMaxGridWidth = 680; 
	$rootScope.exporterPdfDefaultStyle = { fontSize: 7 };
	$rootScope.exporterPdfTableStyle = { margin: [0, 0, 0, 0], alignment: 'right' };
	$rootScope.exporterPdfTableHeaderStyle = { fontSize: 8, bold: true, italics: false, color: 'black', fillColor: '#f0f0f0' };
	$rootScope.exporterPdfHeader = function (headerText) {
    	return { text: headerText, style: 'headerStyle' };
    };
	$rootScope.exporterPdfFooter = function (currentPage, pageCount) {
    	return { text: currentPage.toString() + '/' + pageCount.toString(), style: 'footerStyle' };
    };
    $rootScope.exporterPdfCustomFormatter = function (docDefinition) {
    	docDefinition.styles.headerStyle = { fontSize: 10, bold: false, margin: [ 50, 20, 0, 10 ] };
    	docDefinition.styles.footerStyle = { fontSize: 7, bold: false, margin: [ 50, 10, 0, 10 ] };
        return docDefinition;
    };
    
    // ui-grid localization
    i18nService.add('hr', {
		headerCell: {
			aria: {
				defaultFilterLabel: 'Filter for column',
				removeFilter: 'Remove Filter',
				columnMenuButtonLabel: 'Column Menu'
			},
			priority: 'Priority:',
			filterLabel: "Filter for column: "
		},
		aggregate: {
			label: 'items'
		},
		groupPanel: {
			description: 'Drag a column header here and drop it to group by that column.'
		},
		search: {
			placeholder: 'Search...',
			showingItems: 'Showing Items:',
			selectedItems: 'Selected Items:',
			totalItems: 'Total Items:',
			size: 'Page Size:',
			first: 'First Page',
			next: 'Next Page',
			previous: 'Previous Page',
			last: 'Last Page'
		},
		menu: {
			text: 'Choose Columns:'
		},
		sort: {
			ascending: 'Sort Ascending',
			descending: 'Sort Descending',
			none: 'Sort None',
			remove: 'Remove Sort'
		},
		column: {
			hide: 'Hide Column'
		},
		aggregation: {
			count: 'total rows: ',
			sum: 'total: ',
			avg: 'avg: ',
			min: 'min: ',
			max: 'max: '
		},
		pinning: {
			pinLeft: 'Pin Left',
			pinRight: 'Pin Right',
			unpin: 'Unpin'
		},
		columnMenu: {
			close: 'Close'
		},
		gridMenu: {
			aria: {
				buttonLabel: 'Grid Menu'
			},
			columns: 'Kolone:',
			importerTitle: 'Import file',
			exporterAllAsCsv: 'Export all data as csv',
			exporterVisibleAsCsv: 'Export visible data as csv',
			exporterSelectedAsCsv: 'Export selected data as csv',
			exporterAllAsPdf: 'Export all data as pdf',
			exporterVisibleAsPdf: 'Export visible data as pdf',
			exporterSelectedAsPdf: 'Export selected data as pdf',
			clearAllFilters: 'Poništi sve filtere'
		},
		importer: {
			noHeaders: 'Column names were unable to be derived, does the file have a header?',
			noObjects: 'Objects were not able to be derived, was there data in the file other than headers?',
			invalidCsv: 'File was unable to be processed, is it valid CSV?',
			invalidJson: 'File was unable to be processed, is it valid Json?',
			jsonNotArray: 'Imported json file must contain an array, aborting.'
		},
		pagination: {
			aria: {
				pageToFirst: 'Page to first',
				pageBack: 'Page back',
				pageSelected: 'Selected page',
				pageForward: 'Page forward',
				pageToLast: 'Page to last'
			},
			sizes: 'zapisa po stranici',
			totalItems: 'zapisa',
			through: 'through',
			of: 'od'
		},
		grouping: {
			group: 'Group',
			ungroup: 'Ungroup',
			aggregate_count: 'Agg: Count',
			aggregate_sum: 'Agg: Sum',
			aggregate_max: 'Agg: Max',
			aggregate_min: 'Agg: Min',
			aggregate_avg: 'Agg: Avg',
			aggregate_remove: 'Agg: Remove'
		},
		validate: {
			error: 'Error:',
			minLength: 'Value should be at least THRESHOLD characters long.',
			maxLength: 'Value should be at most THRESHOLD characters long.',
			required: 'A value is needed.'
		}
	});
	
	i18nService.setCurrentLang('hr');
	
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
	    
	    $rootScope.isUserAuthorized();
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
	
	// =======================================
	// 	Date picker ranges
	// =======================================
	var datePickerRanges = {};
	datePickerRanges[$translate('DATETIMEPICKER_TODAY')] = [moment(), moment()];
	datePickerRanges[$translate('DATETIMEPICKER_YESTERDAY')] = [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
	datePickerRanges[$translate('DATETIMEPICKER_LAST_7_DAYS')] = [moment().subtract(6, 'days'), moment()],
	datePickerRanges[$translate('DATETIMEPICKER_LAST_30_DAYS')] = [moment().subtract(29, 'days'), moment()],
	datePickerRanges[$translate('DATETIMEPICKER_THIS_MONTH')] = [moment().startOf('month'), moment().endOf('month')],
	datePickerRanges[$translate('DATETIMEPICKER_LAST_MONTH')] = [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
	
	$rootScope.datePickerRanges = datePickerRanges;
	
	// =======================================
	// 	Date filter clear/apply
	// =======================================
	$rootScope.clearDateFilterGlobal = function(fields, gridApi) {
		$.each(fields, function(index, field) {
			$('#' + field).val(null);
			$rootScope.applyDateFilterGlobal(gridApi);
		});
	};
	
	$rootScope.applyDateFilterGlobal = function(gridApi) {
		setTimeout(function() {
			gridApi.core.raise.filterChanged();
		}, 100);
	};
	
	// =======================================
	// 	Custom UI grid filter
	// =======================================
	$templateCache.put('ui-grid/ui-grid-filter-bss',
		"<div class=\"ui-grid-filter-container\" ng-repeat=\"colFilter in col.filters\" ng-class=\"{'ui-grid-filter-cancel-button-hidden' : colFilter.disableCancelFilterButton === true }\">" +
			"<div ng-if=\"colFilter.type !== 'select'\">" + 
				"<input type=\"text\" class=\"input-sm form-control ui-grid-filter-input\" ng-model=\"colFilter.term\" ng-model-options=\"{ debounce : { 'default' : 500, 'blur' : 0 }}\" ng-attr-placeholder=\"{{colFilter.placeholder || ''}}\" aria-label=\"{{colFilter.ariaLabel || aria.defaultFilterLabel}}\"><div role=\"button\" class=\"ui-grid-filter-button\" ng-click=\"removeFilter(colFilter, $index)\" ng-if=\"!colFilter.disableCancelFilterButton\" ng-disabled=\"colFilter.term === undefined || colFilter.term === null || colFilter.term === ''\" ng-show=\"colFilter.term !== undefined && colFilter.term !== null && colFilter.term !== ''\"><i class=\"ui-grid-icon-cancel\" ui-grid-one-bind-aria-label=\"aria.removeFilter\">&nbsp;</i></div>" + 
			"</div>" +
		"</div>"
	);
	
	// =======================================
	// 	permissions
	// =======================================
	$rootScope.hasPermission = function(permissions) {
		for (i=0; i<$rootScope.principal.permissions.length; i++) { 
			for (j=0; j<permissions.length; j++) {
				if (permissions[j] === $rootScope.principal.permissions[i]) {
					return true;
				}					
			}
		}
		return false;
	};
	
	grant.addTest('hasPermission', function() {
		if (!$rootScope.principal) {
			$log.info('Getting principal');
			
			// get data synchronously
			var request = new XMLHttpRequest();
			request.open('GET', $window.location.pathname.substring(0, window.location.pathname.lastIndexOf("/")) + '/principal', false);
			request.send(null);
			
			if (request.status == 200) {
				$rootScope.principal = jQuery.parseJSON(request.response);
			} else {
				$log.error('Error occurred while getting principal!', status, data);
			}
		}
		
		var permission;
		if (this.stateParams.id === 0 && this.stateParams.permission.endsWith(".update")) {
			// special handling
			permission = this.stateParams.permission.replace(".update", ".create");
		} else {
			permission = this.stateParams.permission;
		}
		
		return $rootScope.hasPermission([permission]);
	});
	
	// indicates if user is authorized to access Fund Finder
	$rootScope.userAuthorized = false;
	
	$rootScope.isUserAuthorized = function(permissions) {
		if ($rootScope.principal.role.indexOf("ERROR_") != -1) {
			$state.go('security.denied');
		} else {
			$rootScope.userAuthorized = true;		
		}	
	};
});