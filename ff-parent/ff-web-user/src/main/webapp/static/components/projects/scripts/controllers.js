angular.module('FundFinder')
	.controller('ProjectsController', ProjectsController)
	.controller('ProjectsEditController', ProjectsEditController);

function ProjectsController($rootScope, $scope, $state, $log, $timeout, $filter, SessionStorage, ImagesService, ProjectsService) {
	var $translate = $filter('translate');
	var $lowercase = $filter('lowercase');
	
	$scope.getRandom = function (min, max) {
	    return Math.floor(Math.random() * (max - min + 1)) + min;
	};
	
	$scope.getImage = function(project) {
		// image of the first investment will be the image of the project
		ImagesService.getEntity(project.investments[0].image.id)
			.success(function(data, status) {
				if (status == 200) {
					if (data.base64) {
						project.image.base64 = data.base64;
					}
				} else {
					toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
				}
			})
			.error(function(data, status) {
				toastr.error($translate('ACTION_LOAD_FAILURE_MESSAGE'));
			});
	};
	
	$scope.getProjects = function() {
		if ($rootScope.principal.demoUser && SessionStorage.getSession("projects")) {
			// DEMO MODE
			$scope.projects = SessionStorage.getSession("projects");
		} else {
			ProjectsService.findAll()
				.success(function(data, status) {
					if (status == 200) {
						$scope.projects = data;
						$.each($scope.projects, function(index, project) {
							project.image.base64 = "/9j/4AAQSkZJRgABAQAAAQABAAD//gA7Q1JFQVRPUjogZ2QtanBlZyB2MS4wICh1c2luZyBJSkcgSlBFRyB2NjIpLCBxdWFsaXR5ID0gOTAK/9sAQwADAgIDAgIDAwMDBAMDBAUIBQUEBAUKBwcGCAwKDAwLCgsLDQ4SEA0OEQ4LCxAWEBETFBUVFQwPFxgWFBgSFBUU/9sAQwEDBAQFBAUJBQUJFA0LDRQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQU/8AAEQgBLAGQAwEiAAIRAQMRAf/EAB8AAAEFAQEBAQEBAAAAAAAAAAABAgMEBQYHCAkKC//EALUQAAIBAwMCBAMFBQQEAAABfQECAwAEEQUSITFBBhNRYQcicRQygZGhCCNCscEVUtHwJDNicoIJChYXGBkaJSYnKCkqNDU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6g4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2drh4uPk5ebn6Onq8fLz9PX29/j5+v/EAB8BAAMBAQEBAQEBAQEAAAAAAAABAgMEBQYHCAkKC//EALURAAIBAgQEAwQHBQQEAAECdwABAgMRBAUhMQYSQVEHYXETIjKBCBRCkaGxwQkjM1LwFWJy0QoWJDThJfEXGBkaJicoKSo1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoKDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uLj5OXm5+jp6vLz9PX29/j5+v/aAAwDAQACEQMRAD8A/QTp9aKKOlAB0ooFLQAUlLRQAUlLRQAUUUUAIaWkpTQAlFLRQAmKWiigBMUYpaBQAUYoooASilpKACiiigAoFFFABRRiloASgUd6KAClpKDQAUUtGKAEooooAO1FFFABRRRQAUGiigBfxooooAKKKKAEopaKAE/GloooASilooASlpKKAClzSUZ9qADNGfyo/CjNABS0hooAM0uaSigAzRmjNGaAFozSUd6AFzikzRRQAZooooAM0UUUAFGKWigBKKWigBKKWjrQAlFLRQAlFLQaAEoxS0UAJRS0UAJRS0UAJiilooASilooAKSlooAKKBRQAUUUlAC0lLRQAlFLRQAlFB4ooAKKKWgBM0ZpcUlABQaXFJQAd6BRijpQAUUUdKACilpMUAGaKODRQAUUdKKAClpKKAFpKO1FAC0lFFABS0lFAC0lFFAC0lFHSgBaKKSgBaM0goFAC0lFFAC9KKSloAKSiloAKSiigA60UUtAB0pKKKAFopKKAF70UGjFACGiiigAooooAKKBRQAdaKXikoAKWkooAKKKKACg0UUAHSiiigAooo9KACjFFLQAlLSUtACfSilooASloooASilpKAFoxSdKWgApKWigBKXFFFACUtFFACUtBpKAFxSd6WigBOlLSdqOlAB0o7UdqKACjFLSUAFLSUUALR3opKAA0UtJQAUUfrR3oAKKKM0AFFFGaACijvS5oASiigmgApaTNGaAFpKKKACjvRRQAdqWkpRQAUUlFABS9aSigBaTpR+FFAC0UmKMUAFLRSUALRSdKKAFoopKAFopKKACiiigBRxRSUUAFFFFABQaM0UAFAoozQAUdaKKACloooADSUGjFABRRRQAUopKKAFopKKAFoJpKKAAmlxSCigBaKSjFABRRijigBaSg0UALSUUCgAoxRR0oAKM0UtACUUUUALSUtFACUUtJQAUUd6XrQAUlFLQAlFFLQAlFLSdaACilpMUAGaKODRQAGiiigAoo/GjvQAdaKKKACloooAKSlPFJQAZoHNHeloASig0ZoAKKO9FABRRRnBoAKKDxRQAtJS0hIBoAKWko60AFFFBOOtABSiko70ALSUUtACCil5pKAFoopKAFpKWjtQAUlL2zjijtmgAzSUUUAFFLSZoAWkoyPUUZHrQAuKTpR1ooACKM0tJQAUUUd6ACiiigANFGaKACijrRmgAopaKAEo5paSgA5paSigA5oo6UUAHNFFFABzXmPxq1S802LS/sl3NbbzJu8pyufu9cV6dXlHx4/1Wkf70n/stADfgpq99qepamt3dzXKpChUSyFsHcema9Z61458B/wDkJ6t/1wT/ANCNex0ANlJETn0U18yTeKtZWZwNUvANx4E7f419Nzf6p/of5V8oTf66T/eP86APTvhF40un1qTTdRupbhbkZhaZy2HHbJ9R/KvZOa+ULW6m068iuIiY54HDqe4YGvpzQ9dg1rQbbVFYJFJHvfJ4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NczKMAt6D2HP510dJQBx3xB0XU9UuNEudMtUupLG589keQIDjGOT9Knsda8Vy3kKXXh+2gtmcCSRbsMVXuQO9dXRQBzfxC0a61/wnd2NlGJbmRkKqWCg4cE8n2FblhG0FjbRuMOkaq31AxViigDkPBvh++8Oa3r0TRKNLuZvtFs4YHBPUY6jqPyrr6SloASlpKWgBMUtFFAB2pKWigAIoxRRQAUYoooASjFLSUAApaTvS0AFFFJQAtFJRQAtFFFABSUtJQAtFJS0AFFFJxQAtFHWkoAKWikoADS0UUAFFFFABRRRQAUUUUAFFJ1paACkpaSgBaKKBQAlLRRQAUUdKKACiig0AHWkpaKACiiigAooooAKKKKACiiigBKKKKAAUtIKU0AFFFJQAUvSiigBKWikFABS0UlABS0UUAJRS0UAJS0UUAFGKTvS0AJ1paO1J3oAXPNFFFABR9KKSgApfWikoAWg0UlAC0UUUAFGaKKACiigUAFGaKSgBc0UUlAC0UUUAFFJS0AFFFFABRRRQAUUZooAKTtRiigApaQUooAKSlpKAFpKWkoAKWkxRQAUtFFABSGgUtACUtFFACUtFJmgBaKSloASloooAKKSloASloooADRRRQAUUUUAFFFFAB3ooooAKKKKAEpaKBQAUUUUAFFFFABmkpaSgA5opaDQAUUUUAFFFHegBKO1FFAAKWkFLQAUlLQaACgUUlABS0lLQACikpaACkzzS0lAC0UlFAC96KKSgBaSlooAKM0UUAFFFFABRRRQAUUUlAC0UUZoAAaDRRQAlL2pKWgAzRRSUALmiiigApKWigApM0tFABRRxRQADpRRRQAUUUUAFFFFACUUZpegoASlpKOlAC0UUUAFFFFACUtJRQAtFJS0AFFJRQAuKKTFFAC0UUUAFFFJQAtFIaKAFooooAKKKKACiiigAoopKAFooooAKMUUUAIKWiigAooooAKKKKACkpaKACiiigAooooAKKKKACiiigBKD0pe9J2oAB9KWkFLQAUlLSDvQAtFGKOtACUUd6B2oAKWjFFABRRikNAC0UYo70AFFJS4oAKOlFFABRSGl70AFFFFABRSZpaACiiigAooooAKKOlJQAtFFB4oAM0UUUAFFFHpQAUUUHigAooo9aACikzS0AHSjtR2ooAKKKKACiijpQB//9k=";
							if (project.investments && project.investments.length > 0 && project.investments[0].image.id) {
								$timeout(function() {
									$scope.getImage(project);
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
		}
	};
	
	$scope.addEntity = function (entity) {
		$state.go('projects.edit', { 'id' : 0 });
	}
	
	$scope.editEntity = function (entity) {
		$state.go('projects.edit', { 'id' : entity.id });
	}
	
	$scope.deleteEntity = function (entity) {
		BootstrapDialog.show({
			type: BootstrapDialog.TYPE_DEFAULT,
            title: $translate('DLG_DELETE_HDR'),
            message: $translate('DLG_DELETE_MSG'),
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
	                	ProjectsService.delete(entity.id)
		    				.success(function(data, status) {
		    					if (status == 200) {
		    						toastr.success($translate('ACTION_DELETE_SUCCESS_MESSAGE'));
		    						$scope.getProjects();
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
	
	// initial load
	$scope.getProjects();
};

function ProjectsEditController($rootScope, $scope, $state, $stateParams, $log, $timeout, $filter, InvestmentsService, ProjectsService, ActivitiesService, CurrenciesService, Subdivisions1Service, Subdivisions2Service) {
	var $translate = $filter('translate');
	
	$scope.getInvestments = function() {
		InvestmentsService.findAll()
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
	
	$scope.getSubdivisions2 = function() {
		Subdivisions2Service.getEntities()
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
	
	$scope.getEntity = function(id) {
		ProjectsService.find(id)
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
	
	$scope.save = function() {
		if ($rootScope.principal.demoUser) {
			// DEMO MODE
		} else {
			ProjectsService.save($scope.entity)
				.success(function(data, status, headers, config) {
					if (status == 200) {
						$scope.entity = data;
						$state.go('projects.edit', { 'id' : $scope.entity.id });
						toastr.success($translate('ACTION_SAVE_SUCCESS_MESSAGE'));
					} else {
						toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
					}
				})
				.error(function(data, status, headers, config) {
					toastr.error($translate('ACTION_SAVE_FAILURE_MESSAGE'));
				});	
		}
	};
	
	$scope.back = function() {
		$state.go('projects.overview');
	};
	
	// initial load
	$scope.getInvestments();
	$scope.getCurrencies();
	$scope.getActivities();
	$scope.getSubdivisions1();
	$scope.getSubdivisions2();
	$scope.getEntity($stateParams.id);
};
