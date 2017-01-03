angular.module('FundFinder')

.service('InvestmentsService', function($http) {
	this.findAll = function() {
		return $http.get('/api/v1/investments');
	};
	this.save = function(resource) {
		return $http.post('/api/v1/investments', resource);
	};
})

.service('CompanyInvestmentsService', function($http) {
	this.findAll = function() {
		return $http.get('/api/v1/companyinvestments');
	};
	this.find = function(id) {
		return $http.get('/api/v1/companyinvestments/' + id);
	};
	this.save = function(resource) {
		return $http.post('/api/v1/companyinvestments', resource);
	};
	this.delete = function(id) {
		return $http.delete('/api/v1/companyinvestments/' + id);
	};
});