angular.module('FundFinder')
	.controller('TendersOverviewController', TendersOverviewController)
	.controller('TendersDetailsController', TendersDetailsController)
	.controller('TendersEditController', TendersEditController);

// ========================================================================
//	OVERVIEW CONTROLLER
// ========================================================================
function TendersOverviewController($rootScope, $scope, $state, $log, $sce, $timeout, $interval, $filter, uiGridConstants, Upload, TendersService, ImagesService, FileSaver, Blob) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	var calculateWidth = function() {
		var width = 0;
		var padding = 4;
		var cnt = 0;
		
		if ($rootScope.hasPermission(['tenders.read'])) {
			width = width + 30;
			cnt++;
		}
		if ($rootScope.hasPermission(['tenders.update'])) {
			width = width + 68;
			cnt++;
		}
		if ($rootScope.hasPermission(['tenders.delete'])) {
			width = width + 26;
			cnt++;
		}
		if ($rootScope.hasPermission(['tenders.export'])) {
			width = width + 30;
			cnt++;
		}

		if (cnt == 1) {
			width = width + padding;
		} else if (cnt > 1) {
			width = width + padding + (cnt * 2) ;
		}
		
		return width;
	};
	
	var statuses = [
		  { value: $translate('LABEL_ALL_TENDERS'), id: '' },
		  { value: $translate('STATUS_ACTIVE'), id: 'ACTIVE' },
		  { value: $translate('STATUS_INACTIVE'), id: 'INACTIVE' }
	];
	
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
					cellTemplate: 
						'<div class="ui-grid-cell-contents">' + 
							'<i ng-if="row.entity.incomplete" class="fa fa-lg fa-exclamation-triangle m-r-sm" style="color: red" uib-tooltip="{{\'STATUS_INCOMPLETE\' | translate}}" tooltip-append-to-body="true" tooltip-placement="top-left"></i>' +
							'<a class="ff-a" ng-click="grid.appScope.showEntity(row.entity)">{{row.entity.name}}</a>' +
						'</div>',
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_STATUS'),
					field: 'status',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><div filter-select></div></div>', 
				    filter: {  term: '', options: statuses },
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
					width: calculateWidth(),
					cellTemplate:
						'<div style="padding-top: 1px">' +
							'<button ng-if="grid.appScope.hasPermission([\'tenders.read\'])" uib-tooltip="{{\'ACTION_TOOLTIP_DETAILS\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.showEntity(row.entity)" class="ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-search-plus"></i></button>' +
							'<button ng-if="grid.appScope.hasPermission([\'tenders.export\'])" uib-tooltip="{{\'ACTION_TOOLTIP_EXPORT\' | translate}} {{\'TO_JSON\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.exportTender(row.entity)" class="ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-download"></i></button>' +
							'<button ng-if="grid.appScope.hasPermission([\'tenders.update\'])" uib-tooltip="{{\'ACTION_TOOLTIP_ACTIVATE\' | translate}}" tooltip-append-to-body="true" ng-show="row.entity.status == \'INACTIVE\'" ng-click="grid.appScope.activateEntity(row.entity)" class=" ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-toggle-off"></i></button>' + 
							'<button ng-if="grid.appScope.hasPermission([\'tenders.update\'])" uib-tooltip="{{\'ACTION_TOOLTIP_DEACTIVATE\' | translate}}" tooltip-append-to-body="true" ng-show="row.entity.status == \'ACTIVE\'" ng-click="grid.appScope.deactivateEntity(row.entity)" class="ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-toggle-on"></i></button>' +
							'<button ng-if="grid.appScope.hasPermission([\'tenders.update\'])" uib-tooltip="{{\'ACTION_TOOLTIP_EDIT\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.editEntity(row.entity)" class="ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-edit"></i></button>' +
							'<button ng-if="grid.appScope.hasPermission([\'tenders.delete\'])" uib-tooltip="{{\'ACTION_TOOLTIP_DELETE\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.deleteEntity(row.entity)" class="ff-grid-button btn-xs btn-white"><i class="fa fa-2x fa-times"></i></button>' + 
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
	
	$scope.importTenders = function(files) {
		if (files && files.length) {
			Upload.upload({
		        url: '/api/v1/tenders/import',
		        method: 'POST',
		        file: files[0]
		    }).success(function (data, status, headers, config) {
		    	toastr.success($translate('ACTION_IMPORT_SUCCESS_MESSAGE'));
		    	$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
		    }).error(function(data, status, headers, config) {
		    	toastr.error($translate('ACTION_IMPORT_FAILURE_MESSAGE'));
		    });
		}
	};
	
	$scope.exportTenders = function() {
		TendersService.exportTenders()
			.success(function(data, status) {
				var blob = new Blob([angular.toJson(data)], { type : "application/json;charset=utf-8;" });	
				FileSaver.saveAs(blob, "ff_tenders.json");
				toastr.success($translate('ACTION_EXPORT_SUCCESS_MESSAGE'));
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_EXPORT_FAILURE_MESSAGE'));
			});
	};
	
	$scope.exportTender = function(entity) {
		TendersService.exportTender(entity.id)
			.success(function(data, status) {
				var blob = new Blob([angular.toJson(data)], { type : "application/json;charset=utf-8;" });	
				FileSaver.saveAs(blob, "ff_tender.json");
				toastr.success($translate('ACTION_EXPORT_SUCCESS_MESSAGE'));
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_EXPORT_FAILURE_MESSAGE'));
			});
	};
	
	$scope.activateEntity = function(entity) {
		TendersService.activateEntity(entity.id)
			.success(function(data, status) {
				if (status == 200) {
					toastr.success($translate('ACTION_ACTIVATE_SUCCESS_MESSAGE'));
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				} else {
					toastr.error($translate('ACTION_ACTIVATE_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_ACTIVATE_FAILURE_MESSAGE'));
			});
	}
	
	$scope.deactivateEntity = function(entity) {
		TendersService.deactivateEntity(entity.id)
			.success(function(data, status) {
				if (status == 200) {
					toastr.success($translate('ACTION_DEACTIVATE_SUCCESS_MESSAGE'));
					$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
				} else {
					toastr.error($translate('ACTION_DEACTIVATE_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_DEACTIVATE_FAILURE_MESSAGE'));
			});
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
	                	TendersService.deleteEntity(entity.id)
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
			sortArray.push({ name: "name", priority: 0, direction: uiGridConstants.ASC });
			$scope.sortArray = sortArray;			
		}
	};
	
	$scope.getRandom = function (min, max) {
	    return Math.floor(Math.random() * (max - min + 1)) + min;
	};
	
	$scope.getTenders = function() {
		TendersService.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.tenders = data;
					$.each($scope.tenders, function(index, tender) {
						if (tender.imageId) {
							tender.image = { "base64" : "/9j/4AAQSkZJRgABAQAAAQABAAD//gA7Q1JFQVRPUjogZ2QtanBlZyB2MS4wICh1c2luZyBJSkcgSlBFRyB2NjIpLCBxdWFsaXR5ID0gOTAK/9sAQwADAgIDAgIDAwMDBAMDBAUIBQUEBAUKBwcGCAwKDAwLCgsLDQ4SEA0OEQ4LCxAWEBETFBUVFQwPFxgWFBgSFBUU/9sAQwEDBAQFBAUJBQUJFA0LDRQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQU/8AAEQgBLAGQAwEiAAIRAQMRAf/EAB8AAAEFAQEBAQEBAAAAAAAAAAABAgMEBQYHCAkKC//EALUQAAIBAwMCBAMFBQQEAAABfQECAwAEEQUSITFBBhNRYQcicRQygZGhCCNCscEVUtHwJDNicoIJChYXGBkaJSYnKCkqNDU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6g4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2drh4uPk5ebn6Onq8fLz9PX29/j5+v/EAB8BAAMBAQEBAQEBAQEAAAAAAAABAgMEBQYHCAkKC//EALURAAIBAgQEAwQHBQQEAAECdwABAgMRBAUhMQYSQVEHYXETIjKBCBRCkaGxwQkjM1LwFWJy0QoWJDThJfEXGBkaJicoKSo1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoKDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uLj5OXm5+jp6vLz9PX29/j5+v/aAAwDAQACEQMRAD8A/QTp9aKKOlAB0ooFLQAUlLRQAUlLRQAUUUUAIaWkpTQAlFLRQAmKWiigBMUYpaBQAUYoooASilpKACiiigAoFFFABRRiloASgUd6KAClpKDQAUUtGKAEooooAO1FFFABRRRQAUGiigBfxooooAKKKKAEopaKAE/GloooASilooASlpKKAClzSUZ9qADNGfyo/CjNABS0hooAM0uaSigAzRmjNGaAFozSUd6AFzikzRRQAZooooAM0UUUAFGKWigBKKWigBKKWjrQAlFLRQAlFLQaAEoxS0UAJRS0UAJRS0UAJiilooASilooAKSlooAKKBRQAUUUlAC0lLRQAlFLRQAlFB4ooAKKKWgBM0ZpcUlABQaXFJQAd6BRijpQAUUUdKACilpMUAGaKODRQAUUdKKAClpKKAFpKO1FAC0lFFABS0lFAC0lFFAC0lFHSgBaKKSgBaM0goFAC0lFFAC9KKSloAKSiloAKSiigA60UUtAB0pKKKAFopKKAF70UGjFACGiiigAooooAKKBRQAdaKXikoAKWkooAKKKKACg0UUAHSiiigAooo9KACjFFLQAlLSUtACfSilooASloooASilpKAFoxSdKWgApKWigBKXFFFACUtFFACUtBpKAFxSd6WigBOlLSdqOlAB0o7UdqKACjFLSUAFLSUUALR3opKAA0UtJQAUUfrR3oAKKKM0AFFFGaACijvS5oASiigmgApaTNGaAFpKKKACjvRRQAdqWkpRQAUUlFABS9aSigBaTpR+FFAC0UmKMUAFLRSUALRSdKKAFoopKAFopKKACiiigBRxRSUUAFFFFABQaM0UAFAoozQAUdaKKACloooADSUGjFABRRRQAUopKKAFopKKAFoJpKKAAmlxSCigBaKSjFABRRijigBaSg0UALSUUCgAoxRR0oAKM0UtACUUUUALSUtFACUUtJQAUUd6XrQAUlFLQAlFFLQAlFLSdaACilpMUAGaKODRQAGiiigAoo/GjvQAdaKKKACloooAKSlPFJQAZoHNHeloASig0ZoAKKO9FABRRRnBoAKKDxRQAtJS0hIBoAKWko60AFFFBOOtABSiko70ALSUUtACCil5pKAFoopKAFpKWjtQAUlL2zjijtmgAzSUUUAFFLSZoAWkoyPUUZHrQAuKTpR1ooACKM0tJQAUUUd6ACiiigANFGaKACijrRmgAopaKAEo5paSgA5paSigA5oo6UUAHNFFFABzXmPxq1S802LS/sl3NbbzJu8pyufu9cV6dXlHx4/1Wkf70n/stADfgpq99qepamt3dzXKpChUSyFsHcema9Z61458B/wDkJ6t/1wT/ANCNex0ANlJETn0U18yTeKtZWZwNUvANx4E7f419Nzf6p/of5V8oTf66T/eP86APTvhF40un1qTTdRupbhbkZhaZy2HHbJ9R/KvZOa+ULW6m068iuIiY54HDqe4YGvpzQ9dg1rQbbVFYJFJHvfJ4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NczKMAt6D2HP510dJQBx3xB0XU9UuNEudMtUupLG589keQIDjGOT9Knsda8Vy3kKXXh+2gtmcCSRbsMVXuQO9dXRQBzfxC0a61/wnd2NlGJbmRkKqWCg4cE8n2FblhG0FjbRuMOkaq31AxViigDkPBvh++8Oa3r0TRKNLuZvtFs4YHBPUY6jqPyrr6SloASlpKWgBMUtFFAB2pKWigAIoxRRQAUYoooASjFLSUAApaTvS0AFFFJQAtFJRQAtFFFABSUtJQAtFJS0AFFFJxQAtFHWkoAKWikoADS0UUAFFFFABRRRQAUUUUAFFJ1paACkpaSgBaKKBQAlLRRQAUUdKKACiig0AHWkpaKACiiigAooooAKKKKACiiigBKKKKAAUtIKU0AFFFJQAUvSiigBKWikFABS0UlABS0UUAJRS0UAJS0UUAFGKTvS0AJ1paO1J3oAXPNFFFABR9KKSgApfWikoAWg0UlAC0UUUAFGaKKACiigUAFGaKSgBc0UUlAC0UUUAFFJS0AFFFFABRRRQAUUZooAKTtRiigApaQUooAKSlpKAFpKWkoAKWkxRQAUtFFABSGgUtACUtFFACUtFJmgBaKSloASloooAKKSloASloooADRRRQAUUUUAFFFFAB3ooooAKKKKAEpaKBQAUUUUAFFFFABmkpaSgA5opaDQAUUUUAFFFHegBKO1FFAAKWkFLQAUlLQaACgUUlABS0lLQACikpaACkzzS0lAC0UlFAC96KKSgBaSlooAKM0UUAFFFFABRRRQAUUUlAC0UUZoAAaDRRQAlL2pKWgAzRRSUALmiiigApKWigApM0tFABRRxRQADpRRRQAUUUUAFFFFACUUZpegoASlpKOlAC0UUUAFFFFACUtJRQAtFJS0AFFJRQAuKKTFFAC0UUUAFFFJQAtFIaKAFooooAKKKKACiiigAoopKAFooooAKMUUUAIKWiigAooooAKKKKACkpaKACiiigAooooAKKKKACiiigBKD0pe9J2oAB9KWkFLQAUlLSDvQAtFGKOtACUUd6B2oAKWjFFABRRikNAC0UYo70AFFJS4oAKOlFFABRSGl70AFFFFABRSZpaACiiigAooooAKKOlJQAtFFB4oAM0UUUAFFFHpQAUUUHigAooo9aACikzS0AHSjtR2ooAKKKKACiijpQB//9k=" };
							$timeout(function() {
								$scope.getImage(tender);
							}, $scope.getRandom(1000, 3000));
						}
					});
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getImage = function(tender) {
		ImagesService.getEntity(tender.imageId)
			.success(function(data, status) {
				if (status == 200) {
					tender.image = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.changeActiveView = function(view) {
		$scope.activeView = view;
		if (view == 1) {
			$scope.tenders = null;
			$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
		} else if (view == 2) {
			$scope.getTenders();
		}
	};
	
	var trusted = {};
	$scope.toTrusted = function(html) {
		if (html) {
			html = html.replace(/\r?\n/g, '<br />');
		}
	    return trusted[html] || (trusted[html] = $sce.trustAsHtml(html)); 
	}
	
	// initial load
	$scope.activeView = 1;
	$scope.loading = true;
	
	$timeout(function() {
		// restore saved grid state
		$rootScope.restoreGridState($state.current.name, $scope.gridApi, $('#filterCreationDate'), $('#filterLastModifiedDate'));
		$scope.applyDateFilter();
		
		// initial sorting
		$scope.setInitialSorting();
		
		// watch 'cntTenders' variable, so if it changes we can refresh grid
		$scope.$watch('cntTenders', function(newValue, oldValue) {
			if (newValue != oldValue) {
				$scope.getPage($scope.gridApi.pagination.getPage(), $scope.gridOptions.paginationPageSize);
			}
		});
	}, 1000);
};

// ========================================================================
//	DETAILS CONTROLLER
// ========================================================================
function TendersDetailsController($rootScope, $scope, $state, $stateParams, $log, $timeout, $filter, $sce, uiGridConstants, TendersService, ImpressionsService, UserGroupsService, UserEmailsService, EmailsService) {
	var $translate = $filter('translate');
	
	// ========================================================================================================================
	//	E-mails grid [start]
	// ========================================================================================================================
	
	$scope.gridOptions4Emails = {
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
			expandableRowTemplate: 'email-exp.html',
	        expandableRowHeight: 125,
	        expandableRowScope: { 
	        	toTrusted: function(html) {
	        		return $sce.trustAsHtml(html);
	        	}
	        },
	        enableExpandableRowHeader: false,
			columnDefs: [
				{
					name: '  ',
					type: 'string',
					cellTooltip: false, 
					enableSorting: false,
					enableFiltering: false,
					enableHiding: false,
					exporterSuppressExport: true,
					width: 32,
					cellTemplate:
						'<div style="padding-top: 1px">' +
							'<button ng-if="!row.isExpanded" uib-tooltip="{{\'ACTION_TOOLTIP_EXPAND\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.expandRow4Emails(row)" class="btn-xs btn-white m-l-xs m-t-xxs ff-grid-button"><i class="fa fa-plus"></i></button>' +
							'<button ng-if="row.isExpanded" uib-tooltip="{{\'ACTION_TOOLTIP_COLLAPSE\' | translate}}" tooltip-append-to-body="true" ng-click="grid.appScope.collapseRow4Emails(row)" class="btn-xs btn-white m-l-xs m-t-xxs ff-grid-button"><i class="fa fa-minus"></i></button>' +
						'</div>'
				},
				{
					displayName: $translate('COLUMN_SEND_DATE'),
					field: 'creationDate',
					type: 'date',
					cellFilter: 'date:grid.appScope.dateTimeFormat',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					filterHeaderTemplate: 
						'<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters">' +
							'<div class="input-group">' +
								'<input id="filterCreationDate" date-range-picker options="grid.appScope.dpOptions4Emails" class="form-control date-picker ui-grid-filter-datepicker" type="text" readonly="readonly" style="background: white;" ng-model="grid.appScope.dpCreationDate" />' +
								'<span class="input-group-addon ui-grid-filter-datepicker-span" ng-click="grid.appScope.clearDateFilterGlobal([\'filterCreationDate\'], grid.appScope.gridApi4Emails)"><i class="ui-grid-icon-cancel ui-grid-filter-datepicker-i"></i></span>' +
							'</div>' + 
						'</div>',
					width: 175
				},
				{
					displayName: $translate('COLUMN_TO'),
					field: 'user.email',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: false,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss',
					width: 225
				},
				{
					displayName: $translate('COLUMN_SUBJECT'),
					field: 'email.subject',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: false,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				}
			],
			onRegisterApi: function(gridApi) {
				$scope.gridApi4Emails = gridApi;
				
				$scope.gridApi4Emails.expandable.on.rowExpandedStateChanged($scope, function(row) {
					if (row.isExpanded) {
						EmailsService.getEntity(row.entity.email.id)
							.success(function(data, status) {
								if (status == 200) {
									row.entity.email = data;
								} else {
									toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
								}
							})
							.error(function(data, status) {
								toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
							});
					}
					
					// refresh grid (to update the height)
					$scope.gridApi4Emails.core.queueGridRefresh();
				});
				
				// register pagination changed handler
				$scope.gridApi4Emails.pagination.on.paginationChanged($scope, function(currentPage, pageSize) {
					$scope.getPage4Emails(currentPage, pageSize);
				});
				
				// register sort changed handler 
				$scope.gridApi4Emails.core.on.sortChanged($scope, $scope.sortChanged4Emails);
				
				// register filter changed handler
				$scope.gridApi4Emails.core.on.filterChanged($scope, function() {
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
					$rootScope.processDateFilters($('#filterCreationDate'), null, filterArray);
					
					$scope.filterArray4Emails = filterArray;
					$scope.getPage4Emails($scope.gridApi4Emails.pagination.getPage(), $scope.gridOptions4Emails.paginationPageSize);
				});
				
				$scope.gridApi4Emails.core.on.rowsRendered($scope, function(b, f, i) {
					var cntExpandedRows = $scope.gridApi4Emails.expandable.getExpandedRows($scope.gridApi4Emails.grid).length;
					var newHeight = ($scope.gridApi4Emails.core.getVisibleRows($scope.gridApi4Emails.grid).length * $rootScope.rowHeight) 
						+ (cntExpandedRows * ($scope.gridApi4Emails.grid.options.expandableRowHeight + 2)) 
						+ (($scope.gridOptions4Emails.totalItems == 0) ? $rootScope.heightNoData : $rootScope.heightCorrectionFactor);
					angular.element(document.getElementsByClassName('grid4Emails')[0]).css('height', newHeight + 'px');
				});
				
				// set initial sort
				if (!$scope.sortArray4Emails) {
					var sortArray = new Array();
					sortArray.push({ name: "creationDate", priority: 0, direction: uiGridConstants.DESC });
					$scope.sortArray4Emails = sortArray;			
				}
				
				// initial load
				$scope.getPage4Emails($scope.gridApi4Emails.pagination.getPage(), $scope.gridOptions4Emails.paginationPageSize);
			}
		};
	
	$scope.expandRow4Emails = function(row) {
		$scope.gridApi4Emails.expandable.expandRow(row.entity);
	};
	
	$scope.collapseRow4Emails = function(row) {
		$scope.gridApi4Emails.expandable.collapseRow(row.entity);
	};
	
	$scope.getPage4Emails = function(page, size) {
		$scope.loading4Emails = true;
		
		if (!$scope.filterArray4Emails) {
			$scope.filterArray4Emails = new Array();
		}
		$scope.filterArray4Emails.push({ "name" : "tender.id", "term" : $stateParams.id });
		
		var uiGridResource = {
			"pagination" : { "page" : page - 1, "size" : size },
			"sort" : $scope.sortArray4Emails,
			"filter" : $scope.filterArray4Emails
		};
		
		UserEmailsService.getPage(uiGridResource)
			.success(function(data, status, headers, config) {
				$scope.loading4Emails = false;
				$scope.gridOptions4Emails.data = data.data;
				$scope.gridOptions4Emails.totalItems = data.total;
			})
			.error(function(data, status, headers, config) {
				$scope.loading4Emails = false;
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.sortChanged4Emails = function (grid, sortColumns) {
		var sortArray = new Array();
		for (var i=0; i<sortColumns.length; i++) {
			sortArray.push({
				"name" : sortColumns[i].field,
				"priority" : sortColumns[i].sort.priority,
				"direction" : sortColumns[i].sort.direction
			});
		}
		$scope.sortArray4Emails = sortArray;
		$scope.getPage4Emails($scope.gridApi4Emails.pagination.getPage(), $scope.gridOptions4Emails.paginationPageSize);
	};
	
	$scope.dpOptions4Emails = {
		opens: 'left',
		format: $rootScope.dateFormat.toUpperCase(),
		ranges: $rootScope.datePickerRanges,
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
				$rootScope.applyDateFilterGlobal($scope.gridApi4Emails);
			}
		}
	};
	
	// ========================================================================================================================
	//	E-mails grid [end]
	// ========================================================================================================================
	
	// ========================================================================================================================
	//	Users grid [start]
	// ========================================================================================================================
	
	$scope.gridOptions4Users = {
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
			columnDefs: [
				{
					displayName: $translate('COLUMN_FIRST_NAME'),
					field: 'firstName',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: true,
					width: 200,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_LAST_NAME'),
					field: 'lastName',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: true,
					width: 200,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_EMAIL'),
					field: 'email',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: true,
					width: 200,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				},
				{
					displayName: $translate('COLUMN_COMPANY'),
					field: 'company.name',
					type: 'string',
					cellTooltip: false, 
					enableSorting: true,
					enableFiltering: true,
					enableHiding: false,
					filterHeaderTemplate: 'ui-grid/ui-grid-filter-bss'
				}
			],
			onRegisterApi: function(gridApi) {
				$scope.gridApi4Users = gridApi;
				
				$scope.gridApi4Users.core.on.rowsRendered($scope, function(b, f, i) {
					var newHeight = ($scope.gridApi4Users.core.getVisibleRows($scope.gridApi4Users.grid).length * $rootScope.rowHeight) + (($scope.gridOptions4Users.totalItems == 0) ? $rootScope.heightNoDataRevisions : $rootScope.heightCorrectionFactor);
					angular.element(document.getElementsByClassName('grid4Users')[0]).css('height', newHeight + 'px');
				});
			}
		};

	$scope.findMatchingUsers = function() {
		TendersService.findMatchingUsers($stateParams.id)
			.success(function(data, status, headers, config) {
				$scope.gridOptions4Users.data = data;
				$scope.gridOptions4Users.totalItems = data.length;
			})
			.error(function(data, status, headers, config) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	// ========================================================================================================================
	//	Users grid [end]
	// ========================================================================================================================
	
	$scope.showEditButton = $stateParams.showEditButton;
	
	var trusted = {};
	$scope.toTrusted = function(html) {
		if (html) {
			html = html.replace(/\r?\n/g, '<br />');
		}
	    return trusted[html] || (trusted[html] = $sce.trustAsHtml(html)); 
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
	
	$scope.getEntity = function() {
		TendersService.getEntity($stateParams.id)
			.success(function(data, status) {
				$scope.entity = data;
				$scope.email = { "tenderId" : $scope.entity.id, "subject" : null, "userGroups" : null };
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getStatisticsPeriods = function() {
		ImpressionsService.getStatisticsPeriods()
			.success(function(data, status) {
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
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getImpressionStatistics = function(statisticsPeriod) {
		ImpressionsService.getImpressionStatistics("TENDER", $stateParams.id, statisticsPeriod.value)
			.success(function(data, status) {
				$scope.impressionStatistics = data;
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getUserGroups = function() {
		UserGroupsService.getEntities()
			.success(function(data, status) {
				$scope.userGroups = data;
				$scope.userGroups.push({ "metaTag" : "MATCHING_USERS", "name" : $translate('USER_GROUP_METATAG_MATCHING_USERS') });
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.sendEmail = function() {
		$scope.sendingEmail = true;
		
		TendersService.sendEmail($scope.email)
			.success(function(data, status) {
				$scope.sendingEmail = false;
				if (status == 200) {
					toastr.success($translate('ACTION_SEND_EMAIL_SUCCESS_MESSAGE'));
					$scope.getPage4Emails($scope.gridApi4Emails.pagination.getPage(), $scope.gridOptions4Emails.paginationPageSize);
				} else if (status == 202) {
					toastr.warning($translate('ACTION_SEND_EMAIL_HTTP_STATUS_202_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_SEND_EMAIL_FAILURE_MESSAGE'));
				$scope.sendingEmail = false;
			});
	};
	
	$scope.resetEmail = function() {
		$scope.email = { "tenderId" : $scope.entity.id, "subject" : null, "userGroups" : null };
	}
	
	// initial load
	$scope.getEntity();
	$scope.getUserGroups();	
	$scope.getStatisticsPeriods();
};

// ========================================================================
//	EDIT CONTROLLER
// ========================================================================
function TendersEditController($rootScope, $scope, $state, $stateParams, $log, $sce, $timeout, $filter, uiGridConstants, TendersService, Subdivisions1Service, Subdivisions2Service, ActivitiesService, InvestmentsService, CurrenciesService) {
	var $translate = $filter('translate');
	
	$scope.id = $stateParams.id;
	
	var trusted = {};
	$scope.toTrusted = function(html) {
		if (html) {
			html = html.replace(/\r?\n/g, '<br />');
		}
	    return trusted[html] || (trusted[html] = $sce.trustAsHtml(html)); 
	}
	
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
		Pace.restart();
		TendersService.getEntity(id)
			.success(function(data, status) {
				Pace.stop();
				
				var subdivision2Item = null;
				if (data.items) { 
					$.each(data.items, function(index, item) {
						if (item.type == 'DATE') {
							if (item.value) {
								item.value = moment.utc(item.value, $rootScope.dateFormatDB.toUpperCase()).toDate();
							}
							$scope.dictPopupDate[index] = { opened: false };
						} else if (item.metaTag == 'TENDER_SUBDIVISION1') {
							subdivision2Item = item;
						}
					});
				}
				
				$scope.getSubdivisions2(subdivision2Item);
				
				$scope.entity = data;
			})
			.error(function(data, status) {
				Pace.stop();
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.saveEntity = function() {
		$scope.saving = true;
		TendersService.saveEntity($scope.entity)
			.success(function(data, status, headers, config) {
				$scope.saving = false;
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
				$scope.saving = false;
				toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
			});		
	};
	
	$scope.getSubdivisions1 = function() {
		Subdivisions1Service.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.subdivisions1 = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getSubdivisions2 = function(item) {
		var subdivision1Ids = "";
		if (item && item.value) {
			$.each(item.value, function(index, item) {
				subdivision1Ids = subdivision1Ids + item.id + "|";
			});
		}
		$scope.getSubdivisions24Subdivisions1(subdivision1Ids);
	};
	
	$scope.getSubdivisions24Subdivisions1 = function(subdivision1Ids) {
		Subdivisions2Service.getEntities4Subdivisions1(subdivision1Ids)
			.success(function(data, status) {
				if (status == 200) {
					$scope.subdivisions2 = data;
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getActivities = function() {
		ActivitiesService.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.activities = data;
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
	
	$scope.getCurrencies = function() {
		CurrenciesService.getEntities()
			.success(function(data, status) {
				if (status == 200) {
					$scope.currencies = data;
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
				var binaryString;
				if (!readerEvt) {
			        binaryString = reader.content;
			    } else {
			    	binaryString = readerEvt.target.result;
			    }
				
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
	$scope.mandatoryItemsOnly = true;
	
	$scope.getEntity($stateParams.id);
	$scope.getCurrencies();
	$scope.getInvestments();
	$scope.getSubdivisions1();
	$scope.getActivities();
};