angular.module('FundFinder')
	.controller('TendersOverviewController', TendersOverviewController)
	.controller('TendersDetailsController', TendersDetailsController)
	.controller('TendersEditController', TendersEditController);

// ========================================================================
//	OVERVIEW CONTROLLER
// ========================================================================
function TendersOverviewController($rootScope, $scope, $state, $log, $timeout, $filter, uiGridConstants, TendersService) {
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
			enableGridMenu: true,
			columnDefs: [
				{
					displayName: $translate('COLUMN_ID'),
					field: 'id',
					type: 'number',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					filterHeaderTemplate: 
						'<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters">' +
							'<input type="number" min="0" class="ui-grid-filter-input ui-grid-filter-input-0 ng-dirty ng-valid-parse ng-touched ui-grid-filter-input-port" ng-model="colFilter.term" ng-attr-placeholder="{{colFilter.placeholder || \'\'}}" aria-label="Filter for column" placeholder="">' + 
						'</div>', 
					enableHiding: true,
					visible: false,
					width: 60
				},        
				{
					displayName: $translate('COLUMN_NAME'),
					field: 'name',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: false,
					cellTemplate:'<div class="ui-grid-cell-contents"><a class="ff-a" ng-click="grid.appScope.showEntity(row.entity)">{{row.entity.name}}</a></div>'
				},
				{
					displayName: $translate('COLUMN_STATUS'),
					field: 'status',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					filter: {
						type: uiGridConstants.filter.SELECT,
						disableCancelFilterButton: true,
						selectOptions: [
							{ value: 'ACTIVE', label: $translate('STATUS_ACTIVE') },
							{ value: 'INACTIVE', label: $translate('STATUS_INACTIVE') }]
					},
					enableHiding: false,
					width: 100,
					cellTemplate:
						'<div ng-show="row.entity.status == \'ACTIVE\'" class="ui-grid-cell-contents"><span class="badge badge-primary">{{\'STATUS_ACTIVE\' | translate}}</span></div>' + 
						'<div ng-show="row.entity.status == \'INACTIVE\'" class="ui-grid-cell-contents"><span class="badge badge-danger">{{\'STATUS_INACTIVE\' | translate}}</span></div>'
				},
				{
					displayName: $translate('COLUMN_CREATION_DATE'),
					field: 'creationDate',
					type: 'date',
					cellFilter: 'date:grid.appScope.dateTimeFormat',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					filterHeaderTemplate: 
						'<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters">' +
							'<div class="input-group">' +
								'<input id="filterCreationDate" date-range-picker options="grid.appScope.dpOptions" class="form-control date-picker ui-grid-filter-datepicker" type="text" readonly="readonly" style="background: white;" ng-model="grid.appScope.dpCreationDate" />' +
								'<span class="input-group-addon ui-grid-filter-datepicker-span" ng-click="grid.appScope.clearDateFilter(\'filterCreationDate\')"><i class="ui-grid-icon-cancel ui-grid-filter-datepicker-i"></i></span>' +
							'</div>' + 
						'</div>',
					enableHiding: true,
					visible: false,
					width: 175
				},
				{
					displayName: $translate('COLUMN_LAST_MODIFIED_DATE'),
					field: 'lastModifiedDate',
					type: 'date',
					cellFilter: 'date:grid.appScope.dateTimeFormat',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					filterHeaderTemplate: 
						'<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters">' +
							'<div class="input-group">' +
								'<input id="filterLastModifiedDate" date-range-picker options="grid.appScope.dpOptions" class="form-control date-picker ui-grid-filter-datepicker" type="text" readonly="readonly" style="background: white;" ng-model="grid.appScope.dpLastModifiedDate" />' +
								'<span class="input-group-addon ui-grid-filter-datepicker-span" ng-click="grid.appScope.clearDateFilter(\'filterLastModifiedDate\')"><i class="ui-grid-icon-cancel ui-grid-filter-datepicker-i"></i></span>' +
							'</div>' + 
						'</div>',
					enableHiding: true,
					width: 175
				},
				{
					name: ' ',
					type: 'string',
					cellTooltip: false, 
					enableSorting: false,
					enableFiltering: false,
					enableHiding: false,
					width: 158,
					cellTemplate:
						'<div style="padding-top: 1px">' +
							'<button uib-tooltip="{{\'ACTION_TOOLTIP_DETAILS\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.showEntity(row.entity)" class="ff-grid-button btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-search-plus"></i></button>' +	
							'<button uib-tooltip="{{\'ACTION_TOOLTIP_ACTIVATE\' | translate}}" tooltip-append-to-body="true" ng-show="row.entity.status == \'INACTIVE\'" ng-click="grid.appScope.activateEntity(row.entity)" class=" ff-grid-button btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-toggle-off"></i></button>' + 
							'<button uib-tooltip="{{\'ACTION_TOOLTIP_DEACTIVATE\' | translate}}" tooltip-append-to-body="true" ng-show="row.entity.status == \'ACTIVE\'" ng-click="grid.appScope.deactivateEntity(row.entity)" class="ff-grid-button btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-toggle-on"></i></button>' +
							'<button uib-tooltip="{{\'ACTION_TOOLTIP_EDIT\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.editEntity(row.entity)" class="ff-grid-button btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-edit"></i></button>' +
							'<button uib-tooltip="{{\'ACTION_TOOLTIP_DELETE\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.deleteEntity(row.entity)" class="ff-grid-button btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-times"></i></button>' + 
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
					var newHeight = ($scope.gridApi.core.getVisibleRows($scope.gridApi.grid).length * $rootScope.rowHeight) + (($scope.gridOptions.totalItems == 0) ? $rootScope.heightNoData : $rootScope.heightCorrectionFactor);
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
		
		TendersService.getPage(uiGridResource)
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
		$state.go('tenders.details', { 'id' : entity.id, 'showEditButton' : true });
	}
	
	$scope.addEntity = function (entity) {
		$state.go('tenders.edit', { 'id' : 0 });
	}
	
	$scope.editEntity = function (entity) {
		$state.go('tenders.edit', { 'id' : entity.id });
	}
	
	$scope.activateEntity = function(entity) {
		TendersService.activateEntity(entity.id)
			.success(function(data, status) {
				if (status == 200) {
					toastr.success($translate('ACTION_ACTIVATE_SUCCESS_MESSAGE', { entity: $translate('ENTITY_TENDER') }));
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				} else {
					toastr.error($translate('ACTION_ACTIVATE_FAILURE_MESSAGE', { entity: $translate('ENTITY_TENDER') }));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_ACTIVATE_FAILURE_MESSAGE', { entity: $translate('ENTITY_TENDER') }));
			});
	}
	
	$scope.deactivateEntity = function(entity) {
		TendersService.deactivateEntity(entity.id)
			.success(function(data, status) {
				if (status == 200) {
					toastr.success($translate('ACTION_DEACTIVATE_SUCCESS_MESSAGE', { entity: $translate('ENTITY_TENDER') }));
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				} else {
					toastr.error($translate('ACTION_DEACTIVATE_FAILURE_MESSAGE', { entity: $translate('ENTITY_TENDER') }));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_DEACTIVATE_FAILURE_MESSAGE', { entity: $translate('ENTITY_TENDER') }));
			});
	}
	
	$scope.deleteEntity = function (entity) {
		BootstrapDialog.show({
			type: BootstrapDialog.TYPE_DEFAULT,
            title: $translate('DIALOG_DELETE_HEADER', { entity: $lowercase($translate('ENTITY_TENDER')) }),
            message: $translate('DIALOG_DELETE_MESSAGE', { entity: $lowercase($translate('ENTITY_TENDER')) }),
            buttons: [
				{
					label: $translate('BUTTON_NO'),
					icon: 'fa fa-times',
				    cssClass: 'btn-white',
				    action: function(dialog) {
				        dialog.close();
				    }
				},
            	{
            		label: $translate('BUTTON_YES'),
	            	icon: 'fa fa-check',
	                cssClass: 'btn-primary',
	                action: function(dialog) {
	                	TendersService.deleteEntity(entity.id)
		    				.success(function(data, status) {
		    					if (status == 200) {
		    						toastr.success($translate('ACTION_DELETE_SUCCESS_MESSAGE', { entity: $translate('ENTITY_TENDER') }));
		    						$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
		    					} else {
		    						toastr.error($translate('ACTION_DELETE_FAILURE_MESSAGE', { entity: $translate('ENTITY_TENDER') }));
		    					}
		    				})
		    				.error(function(data, status) {
		    					toastr.error($translate('ACTION_DELETE_FAILURE_MESSAGE', { entity: $translate('ENTITY_TENDER') }));
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
			customRangeLabel: $translate('DATETIMEPICKER_CUSTOM') 
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
			sortArray.push({ name: "name", priority: 0, direction: uiGridConstants.ASC });
			$scope.sortArray = sortArray;			
		}
	}
	
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
function TendersDetailsController($rootScope, $scope, $state, $stateParams, $log, $timeout, $filter, $sce, uiGridConstants, TendersService, ImpressionsService) {
	var $translate = $filter('translate');
	
	$scope.showEditButton = $stateParams.showEditButton;
	
	$scope.gridOptions = {
		rowHeight: $rootScope.rowHeight,
		paginationPageSize: $rootScope.paginationPageSize,
		paginationPageSizes: $rootScope.paginationPageSizes,
		enableFiltering: true,
		useExternalFiltering: false,
		useExternalSorting: false,
		useExternalPagination: false,
		enableColumnMenus: false,
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableGridMenu: false,
		columnDefs: [
			{
				displayName: $translate('COLUMN_REVISION'),
				field: 'id',
				type: 'number',
				cellTooltip: false, 
				sort: { direction: uiGridConstants.DESC },
				enableSorting: true,
				enableFiltering: false,
				filterHeaderTemplate: 
					'<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters">' +
						'<input type="number" min="0" class="ui-grid-filter-input ui-grid-filter-input-0 ng-dirty ng-valid-parse ng-touched ui-grid-filter-input-port" ng-model="colFilter.term" ng-attr-placeholder="{{colFilter.placeholder || \'\'}}" aria-label="Filter for column" placeholder="">' + 
					'</div>', 
				enableHiding: false,
				visible: false,
				width: 100
			},
			{
				displayName: $translate('COLUMN_REVISION_DATE'),
				field: 'creationDate',
				type: 'date',
				cellFilter: 'date:\'' + $rootScope.dateTimeFormat + '\'',
				cellTooltip: false, 
				enableSorting: true,
				enableFiltering: false,
				enableHiding: false,
				width: 175
			},
			{
				displayName: $translate('COLUMN_REVISION_USER'),
				field: 'createdBy',
				type: 'string',
				cellTooltip: false, 
				enableSorting: true,
				enableFiltering: false,
				enableHiding: false
			},
			{
				displayName: $translate('COLUMN_REVISION_TYPE'),
				field: 'type',
				type: 'string',
				filter: {
					type: uiGridConstants.filter.SELECT,
					disableCancelFilterButton: true,
					selectOptions: [
						{ value: 'ADD', label: $translate('REVISION_TYPE_ADD') },
						{ value: 'MOD', label: $translate('REVISION_TYPE_MOD') },
						{ value: 'DEL', label: $translate('REVISION_TYPE_DEL') }]
				},
				cellTooltip: false, 
				enableSorting: true,
				enableFiltering: false,
				enableHiding: false,
				width: 100
			},
			{
				name: ' ',
				type: 'string',
				cellTooltip: false, 
				enableSorting: false,
				enableFiltering: false,
				enableHiding: false,
				width: 42,
				cellTemplate:
					'<div style="padding-top: 1px">' +
						'<button uib-tooltip="{{\'ACTION_TOOLTIP_REVISION_DETAILS\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.showRevisionDetails(row.entity)" class="ff-grid-button btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-search-plus"></i></button>' +
					'</div>'
			}
		],
		onRegisterApi: function(gridApi) {
			$scope.gridApi = gridApi;
			
			$scope.gridApi.core.on.rowsRendered($scope, function(b, f, i) {
				var newHeight = ($scope.gridApi.core.getVisibleRows($scope.gridApi.grid).length * $rootScope.rowHeight) + (($scope.gridOptions.totalItems == 0) ? $rootScope.heightNoDataRevisions : $rootScope.heightCorrectionFactorRevisions);
				angular.element(document.getElementsByClassName('grid')[0]).css('height', newHeight + 'px');
			});
		}
	};
	
	$scope.toTrusted = function(html) {
		if (html) {
			html = html.replace(/\r?\n/g, '<br />');
		}
	    return $sce.trustAsHtml(html);
	}
	
	$scope.editEntity = function() {
		$state.go('tenders.edit', { 'id' : $stateParams.id });
	}
	
	$scope.back = function() {
		if ($rootScope.previousState) {
			if ($rootScope.currentState == 'tenders.details') {
				if ($rootScope.previousState == 'users.details') {
					$state.go($rootScope.previousState, $rootScope.previousStateParams);
				} else {
					$state.go('tenders.overview');	
				}
			} else if ($rootScope.previousState == 'tenders.edit' && $rootScope.previousStateParams.id == '0') {
				$state.go('tenders.overview');
			} else {
				$state.go($rootScope.previousState, $rootScope.previousStateParams);
			}
		} else {
			window.history.back();
		}
	};
	
	// initial load
	TendersService.getRevisions($stateParams.id)
		.success(function(data, status, headers, config) {
			if (status == 200) {
				$scope.gridOptions.data = data;
				$scope.gridOptions.totalItems = data.length;
			} else {
				$log.error(data);
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			}
		})
		.error(function(data, status, headers, config) {
			toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
		});
	
	TendersService.getEntity($stateParams.id)
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
	
	ImpressionsService.getStatisticsPeriods()
		.success(function(data, status) {
			if (status == 200) {
				$scope.statisticsPeriods = new Array();
				$.each(data, function(key, value) {
					if (value == 'LAST_7_DAYS') {
						$scope.statisticsPeriods.push({ "label" : $translate('STATISTICS_PERIOD_LAST_7_DAYS'), "value" : value });
					} else if (value == 'LAST_6_MONTHS') {
						$scope.statisticsPeriods.push({ "label" : $translate('STATISTICS_PERIOD_LAST_6_MONTHS'), "value" : value });
					} 
				});
				$scope.statisticsPeriod = $scope.statisticsPeriods[0];
				$scope.getImpressionStatistics($scope.statisticsPeriod);
			} else {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			}
		})
		.error(function(data, status) {
			toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
		});
	
	$scope.getImpressionStatistics = function(statisticsPeriod) {
		ImpressionsService.getImpressionStatistics("TENDER", $stateParams.id, statisticsPeriod.value)
			.success(function(data, status) {
				if (status == 200) {
					$scope.impressionStatistics = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	}
	
};

// ========================================================================
//	EDIT CONTROLLER
// ========================================================================
function TendersEditController($rootScope, $scope, $state, $stateParams, $log, $timeout, $filter, uiGridConstants, TendersService, CountiesService, NkdsService, InvestmentsService) {
	var $translate = $filter('translate');
	
	$scope.summernoteOptions = {
		    height: 300,
		    focus: false,
		    airMode: false,
		    toolbar: [
		            ['edit', ['undo','redo']],
		            ['style', ['bold', 'italic', 'underline']],
		            ['alignment', ['ul', 'ol', 'paragraph']],
		            ['insert', ['link','hr']]
		    ]
		};
	
	$scope.getEntity = function(id) {
		TendersService.getEntity(id)
			.success(function(data, status) {
				if (status == 200) {
					if (data.items) { 
						$.each(data.items, function(index, item) {
							if (item.type == 'DATE') {
								if (item.value) {
									item.value = moment(item.value, $rootScope.dateFormat.toUpperCase()).toDate();
								}
								$scope.dictPopupDate[index] = { opened: false };
							}
						});
					}
					$scope.entity = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.saveEntity = function() {
		$.each($scope.entity.items, function(index, item) {
			if (item.type == 'DATE') {
				if (item.value) {
					item.value = moment(item.value).format("YYYY.MM.DD");
					item.value = moment(item.value).format($rootScope.dateFormat.toUpperCase());
				}
			}
		});
		
		TendersService.saveEntity($scope.entity)
			.success(function(data, status, headers, config) {
				if (status == 200) {
					toastr.success($translate('ACTION_SAVE_SUCCESS_MESSAGE'));
					$state.go('tenders.edit', { 'id' : data.id });
					$scope.getEntity(data.id);
				} else {
					if (data.exception.indexOf("ValidationFailedException") != -1) {
						toastr.warning(data.message, $translate('VALIDATION_FAILED_HEADER'));
					} else {
						toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
					}
				}
			})
			.error(function(data, status, headers, config) {
				toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
			});		
	};
	
	$scope.getCounties = function() {
		CountiesService.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.counties = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getNkds = function() {
		NkdsService.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.nkds = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getInvestments = function() {
		InvestmentsService.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.investments = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.back = function() {
		if ($rootScope.previousState) {
			if ($rootScope.currentState == 'tenders.details') {
				if ($rootScope.previousState == 'users.details') {
					$state.go($rootScope.previousState, $rootScope.previousStateParams);
				} else {
					$state.go('tenders.overview');	
				}
			} else if ($rootScope.previousState == 'tenders.edit' && $rootScope.previousStateParams.id == '0') {
				$state.go('tenders.overview');
			} else {
				$state.go($rootScope.previousState, $rootScope.previousStateParams);
			}
		} else {
			window.history.back();
		}
	};
	
	$scope.selectImage = function(files) {
		if (files && files.length == 1) {
			var reader = new FileReader();
			reader.onload = function(readerEvt) {
	            var binaryString = readerEvt.target.result;
	            setTimeout(function () {
	                $scope.$apply(function () {
	                	$scope.entity.image.base64 = btoa(binaryString);
	                });
	            }, 100);
	        };
	        reader.readAsBinaryString(files[0]);
		}
	};
	
	$scope.deleteImage = function() {
		$scope.entity.image.base64 = null;
	};
	
	$scope.dictPopupDate = new Object();
	
	$scope.openPopupDate = function(index) {
		$scope.dictPopupDate[index].opened = true;
	};
	
	// initial load
	$scope.getInvestments();
	$scope.getCounties();
	$scope.getNkds();
	$scope.getEntity($stateParams.id);
};