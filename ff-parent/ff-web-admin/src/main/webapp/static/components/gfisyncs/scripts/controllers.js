angular.module('FundFinder')
	.controller('GfiSyncsOverviewController', GfiSyncsOverviewController)
	.controller('GfiSyncsDetailsController', GfiSyncsDetailsController);

// ========================================================================
//	OVERVIEW CONTROLLER
// ========================================================================
function GfiSyncsOverviewController($rootScope, $scope, $state, $log, $timeout, $filter, uiGridConstants, GfiSyncsService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.gridOptions = {
			rowHeight: $rootScope.rowHeight,
			paginationPageSize: $rootScope.paginationPageSize,
			paginationPageSizes: $rootScope.paginationPageSizes,
			enableFiltering: true,
			useExternalFiltering: true,
			useExternalSorting: true,
			useExternalPagination: true,
			enableColumnMenus: false,
			enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
			enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
			enableGridMenu: false,
			columnDefs: [
				{
					displayName: $translate('COLUMN_ID'),
					field: 'id',
					type: 'number',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: false,
					enableHiding: false,
					visible: true,
					width: 60
				}, 
				{
					displayName: $translate('COLUMN_GFI_SYNC_TOTAL'),
					field: 'cntTotal',
					type: 'number',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: false,
					enableHiding: false,
					visible: true
				},  
				{
					displayName: $translate('COLUMN_GFI_SYNC_OK'),
					field: 'cntOk',
					type: 'number',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: false,
					enableHiding: false,
					visible: true
				},  
				{
					displayName: $translate('COLUMN_GFI_SYNC_NOK'),
					field: 'cntNok',
					type: 'number',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: false,
					enableHiding: false,
					visible: true
				},  
				{
					displayName: $translate('COLUMN_START_TIME'),
					field: 'startTime',
					type: 'date',
					cellFilter: 'date:grid.appScope.dateTimeFormat',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: false,
					enableHiding: false,
					visible: true,
					width: 175
				},
				{
					displayName: $translate('COLUMN_END_TIME'),
					field: 'endTime',
					type: 'date',
					cellFilter: 'date:grid.appScope.dateTimeFormat',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: false,
					enableHiding: false,
					visible: true,
					width: 175
				},
				{
					name: ' ',
					type: 'string',
					cellTooltip: false, 
					enableSorting: false,
					enableFiltering: false,
					enableHiding: false,
					width: 62,
					cellTemplate:
						'<div style="padding-top: 1px">' +
							'<button uib-tooltip="{{\'ACTION_TOOLTIP_DETAILS\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.showEntity(row.entity)" class="ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-search-plus"></i></button>' +	
							'<button uib-tooltip="{{\'ACTION_TOOLTIP_DELETE\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.deleteEntity(row.entity)" class="ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-times"></i></button>' + 
						'</div>'
				}
			],
			onRegisterApi: function(gridApi) {
				$scope.gridApi = gridApi;
				
				// register pagination changed handler
				$scope.gridApi.pagination.on.paginationChanged($scope, function(currentPage, pageSize) {
					$scope.getPage(currentPage, pageSize);
				});
				
				// register sort changed handler 
				$scope.gridApi.core.on.sortChanged($scope, $scope.sortChanged);
				
				// register filter changed handler
				$scope.gridApi.core.on.filterChanged($scope, function() {
					var grid = this.grid;
					
					var filterArray = new Array();
					for (var i=0; i<grid.columns.length; i++) {
						var name = grid.columns[i].field;
						var term = grid.columns[i].filters[0].term;
						if (name && term) {
							filterArray.push({
								"name" : name,
								"term" : term
							});
						}
					}
					
					// date filters (e.g. creationDate, lastModifiedDate)
					$rootScope.processDateFilters($('#filterCreationDate'), $('#filterLastModifiedDate'), filterArray);
					
					$scope.filterArray = filterArray;
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				});
				
				$scope.gridApi.core.on.rowsRendered($scope, function(b, f, i) {
					var newHeight = ($scope.gridApi.core.getVisibleRows($scope.gridApi.grid).length * $rootScope.rowHeight) + (($scope.gridOptions.totalItems == 0) ? $rootScope.heightNoData : $rootScope.heightCorrectionFactorRevisions);
					angular.element(document.getElementsByClassName('grid')[0]).css('height', newHeight + 'px');
				});
				
				$scope.gridApi.core.on.columnVisibilityChanged($scope, function() {
					$rootScope.saveGridState($state.current.name, $scope.gridApi, $('#filterCreationDate'), $('#filterLastModifiedDate'));
				});
				
				$scope.gridApi.grid.clearAllFilters = function() {
					this.columns.forEach(function(column) {
						column.filters.forEach(function(filter) {
							filter.term = undefined;
						});
					});
					$scope.clearDateFilter('filterCreationDate');
					$scope.clearDateFilter('filterLastModifiedDate');
				};
			}
		};
	
	$scope.getPage = function(page, size) {
		// save current state
		$rootScope.saveGridState($state.current.name, $scope.gridApi, $('#filterCreationDate'), $('#filterLastModifiedDate'));
		
		var uiGridResource = {
			"pagination" : {
				"page" : page - 1,
				"size" : size
			},
			"sort" : $scope.sortArray,
			"filter" : $scope.filterArray
		};
		
		GfiSyncsService.getPage(uiGridResource)
			.success(function(data, status, headers, config) {
				$scope.loading = false;
				if (status == 200) {
					$scope.gridOptions.data = data.data;
					$scope.gridOptions.totalItems = data.total;
				} else {
					$log.error(data);
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status, headers, config) {
				$scope.loading = false;
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.sortChanged = function (grid, sortColumns) {
		var sortArray = new Array();
		for (var i=0; i<sortColumns.length; i++) {
			sortArray.push({
				"name" : sortColumns[i].field,
				"priority" : sortColumns[i].sort.priority,
				"direction" : sortColumns[i].sort.direction
			});
		}
		$scope.sortArray = sortArray;
		$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
	};
	
	$scope.showEntity = function (entity) {
		$state.go('gfisyncs.details', { 'id' : entity.id });
	}
	
	$scope.deleteEntity = function (entity) {
		BootstrapDialog.show({
			type: BootstrapDialog.TYPE_DEFAULT,
            title: $translate('DIALOG_DELETE_HEADER'),
            message: $translate('DIALOG_DELETE_MESSAGE'),
            buttons: [
				{
					label: $translate('BUTTON_NO'),
				    cssClass: 'btn-white',
				    action: function(dialog) {
				        dialog.close();
				    }
				},
            	{
            		label: $translate('BUTTON_YES'),
	                cssClass: 'btn-primary',
	                action: function(dialog) {
	                	GfiSyncsService.deleteEntity(entity.id)
		    				.success(function(data, status) {
		    					if (status == 200) {
		    						toastr.success($translate('ACTION_DELETE_SUCCESS_MESSAGE'));
		    						$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
		    					} else {
		    						toastr.error($translate('ACTION_DELETE_FAILURE_MESSAGE'));
		    					}
		    				})
		    				.error(function(data, status) {
		    					toastr.error($translate('ACTION_DELETE_FAILURE_MESSAGE'));
		    				});
	        			dialog.close();
	                }	                
            	}
            ]
        });
	}
	
	// date picker options
	var rangesJSON = {};
	rangesJSON[$translate('DATETIMEPICKER_TODAY')] = [moment(), moment()];
	rangesJSON[$translate('DATETIMEPICKER_YESTERDAY')] = [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
	rangesJSON[$translate('DATETIMEPICKER_LAST_7_DAYS')] = [moment().subtract(6, 'days'), moment()],
	rangesJSON[$translate('DATETIMEPICKER_LAST_30_DAYS')] = [moment().subtract(29, 'days'), moment()],
	rangesJSON[$translate('DATETIMEPICKER_THIS_MONTH')] = [moment().startOf('month'), moment().endOf('month')],
	rangesJSON[$translate('DATETIMEPICKER_LAST_MONTH')] = [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
	
	$scope.dpOptions = {
		opens: 'left',
		format: $rootScope.dateFormat.toUpperCase(),
		ranges: rangesJSON,
		locale: { 
			daysOfWeek: $rootScope.dpDaysOfWeek,
			monthNames: $rootScope.dpMonthNames,
			applyLabel: $translate('DATETIMEPICKER_APPLY'),
	        cancelLabel: $translate('DATETIMEPICKER_CANCEL'),
	        fromLabel: $translate('DATETIMEPICKER_FROM'),
	        toLabel: $translate('DATETIMEPICKER_TO'),
	        customRangeLabel: $translate('DATETIMEPICKER_CUSTOM'),
			firstDay: 1
		}, 
		showDropdowns: true,
		eventHandlers: {
			'apply.daterangepicker': function(ev, picker) {
				$scope.applyDateFilter();
			}
		}
	};
	
	/**
	 * Function clears filter of the given date range picker field.
	 */
	$scope.clearDateFilter = function(field) {
		$('#' + field).val(null);
		$scope.applyDateFilter();	
	};
	
	/**
	 * Function applies date filters.
	 */
	$scope.applyDateFilter = function() {
		setTimeout(function () {
			$scope.$apply(function() {
				$scope.gridApi.core.raise.filterChanged();
			});
		}, 100);
	};
	
	/**
	 * Function sets initial sorting.
	 */
	$scope.setInitialSorting = function() {
		if (!$scope.sortArray) {
			var sortArray = new Array();
			sortArray.push({ name: "id", priority: 0, direction: uiGridConstants.DESC });
			$scope.sortArray = sortArray;			
		}
	}
	
	$scope.back = function() {
		$state.go('users.overview');	
	};
	
	// initial load
	$scope.loading = true;
		
	$timeout(function() {
		// restore saved grid state
		$rootScope.restoreGridState($state.current.name, $scope.gridApi, $('#filterCreationDate'), $('#filterLastModifiedDate'));
		$scope.applyDateFilter();
		
		// initial sorting
		$scope.setInitialSorting();
	}, 1000);
};

// ========================================================================
//	DETAILS CONTROLLER
// ========================================================================
function GfiSyncsDetailsController($rootScope, $scope, $state, $stateParams, $log, $timeout, $filter, $sce, uiGridConstants, GfiSyncsService, GfiSyncErrorsService) {
	var $translate = $filter('translate');
	
	$scope.toTrusted = function(html) {
	    return $sce.trustAsHtml(html);
	}
	
	$scope.errorDetails = function(id) {
		GfiSyncErrorsService.getEntity(id)
			.success(function(data, status) {
				BootstrapDialog.show({
					type: BootstrapDialog.TYPE_DEFAULT,
					size: BootstrapDialog.SIZE_WIDE,
		            title: $translate('ACTION_TOOLTIP_DETAILS'),
		            buttons: [{
		                label: $translate('BUTTON_CLOSE'),
		                action: function(dialogRef) {
		                    dialogRef.close();
		                }
		            }],
		            message: 
		            	'<div style="font-size: 10px; word-wrap: break-word; height: 500px; overflow: scroll;">' +
		            	'<h4>' + $translate('LABEL_GFI_STACK_TRACE') + '</h4>' +
		            		data.error +
		            		'<p>' +
		            		'<h4>' + $translate('LABEL_GFI_COMPANY_DATA') + '</h4>' +
		            		data.companyData + 
		            	'</div>'
		        });
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});	
	};
	
	$scope.back = function() {
		if ($rootScope.previousState) {
			$state.go($rootScope.previousState, $rootScope.previousStateParams);
		} else {
			window.history.back();
		}
	};
	
	// initial load
	GfiSyncsService.getEntity($stateParams.id)
		.success(function(data, status) {
			if (status == 200) {
				$scope.entity = data;
			} else {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			}
		})
		.error(function(data, status) {
			toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
		});	
};