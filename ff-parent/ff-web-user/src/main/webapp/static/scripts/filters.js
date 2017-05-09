angular.module('FundFinder')
	
	.filter('getSimpleClassName', function() { 
		return function(input) {
			if (input) {
				return input.substring(input.lastIndexOf(".") + 1);
			}
			return input;
		} 
	})

	.filter('htmlToPlaintext', function() {
		return function(text) {
			return text ? String(text).replace(/<[^>]+>/gm, '') : '';
		};
	})
	
	.filter('nl2br', function() {
		return function(text) {
			if (!text) return text;
			return text.replace(/\n\r?/g, '<br />');
		};
	})
	
	.filter("customCurrency", function (numberFilter) {
		function isNumeric(value) {
			return (!isNaN(parseFloat(value)) && isFinite(value));
		}

		return function (inputNumber, currencySymbol, decimalSeparator, thousandsSeparator, decimalDigits, prefixWithSymbol) {
			if (isNumeric(inputNumber)) {
				// Default values for the optional arguments
				currencySymbol = (typeof currencySymbol === "undefined") ? "$" : currencySymbol;
				decimalSeparator = (typeof decimalSeparator === "undefined") ? "." : decimalSeparator;
				thousandsSeparator = (typeof thousandsSeparator === "undefined") ? "," : thousandsSeparator;
				decimalDigits = (typeof decimalDigits === "undefined" || !isNumeric(decimalDigits)) ? 2 : decimalDigits;
				prefixWithSymbol = (typeof prefixWithSymbol === "undefined") ? true : prefixWithSymbol;

				if (decimalDigits < 0) decimalDigits = 0;

				// Format the input number through the number filter
				// The resulting number will have "," as a thousands separator
				// and "." as a decimal separator.
				var formattedNumber = numberFilter(inputNumber, decimalDigits);

				// Extract the integral and the decimal parts
				var numberParts = formattedNumber.split(".");

				// Replace the "," symbol in the integral part
				// with the specified thousands separator.
				numberParts[0] = numberParts[0].split(",").join(thousandsSeparator);

				// Compose the final result
				var result = numberParts[0];

				if (numberParts.length == 2) {
					result += decimalSeparator + numberParts[1];
				}

				return (prefixWithSymbol ? currencySymbol + " " : "") + result + (prefixWithSymbol ? "" : " " + currencySymbol);
			} else {
				return inputNumber;
			}
		};
	});