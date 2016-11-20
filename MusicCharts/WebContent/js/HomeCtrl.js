function fillGauges(elementName, data) {
	var svg = d3.select(elementName);
	var g = svg.append("g").attr("transform", "translate(100,50)");
	var domain = [ 0, 100 ];
	var gg = viz.gg().domain(domain).outerRadius(50).innerRadius(10)
			.value(data);
	gg.defs(svg);
	g.call(gg);
	d3.select(self.frameElement).style("height", "300px");
}

var app = angular.module('app', []);
app.controller('HomeCtrl', function($scope, $http) {
	var gauge;
	$scope.schools = [];
	var pie = new d3pie("pieChart", {
		"header" : {
			"title" : {
				"text" : "Genres",
				"fontSize" : 24,
				"font" : "open sans"
			},
			"titleSubtitlePadding" : 9
		},
		"footer" : {
			"color" : "#999999",
			"fontSize" : 10,
			"font" : "open sans",
			"location" : "bottom-left"
		},
		"size" : {
			"canvasWidth" : 500,
			"pieOuterRadius" : "75%"
		},
		"labels" : {
			"outer" : {
				"pieDistance" : 20
			},
			"inner" : {
				"hideWhenLessThanPercentage" : 3
			},
			"mainLabel" : {
				"fontSize" : 11
			},
			"percentage" : {
				"color" : "#ffffff",
				"decimalPlaces" : 0
			},
			"value" : {
				"color" : "#adadad",
				"fontSize" : 11
			},
			"lines" : {
				"enabled" : true
			},
			"truncation" : {
				"enabled" : true
			}
		},
		"effects" : {
			"pullOutSegmentOnClick" : {
				"effect" : "linear",
				"speed" : 400,
				"size" : 8
			}
		},
		"misc" : {
			"gradient" : {
				"enabled" : true,
				"percentage" : 100
			}
		}
	});
	$http({
		method : 'GET',
		url : 'SchoolServlet'
	}).then(
			function successCallback(response) {
				console.log(response);
				var data = response.data;
				for (var int = 0; int < response.data.length; int++) {
					var s = response.data[int];

					var school = {
						value : "",
						text : ""
					};
					school.value = s;
					school.text = toTitleCase(s);
					$scope.schools.push(school);
				}
				console.log($scope.schools);
				function toTitleCase(str) {
					return str.replace(/\w\S*/g, function(txt) {
						return txt.charAt(0).toUpperCase()
								+ txt.substr(1).toLowerCase();
					});
				}
				// this callback will be called
				// asynchronously
				// when the response is available
			}, function errorCallback(response) {
				// called asynchronously if an error
				// occurs
				// or server returns response with an
				// error status.
			});
	$scope.createReports = function() {
		$scope.topArtists = [];
		$scope.topTracks = [];
		$scope.nationalTopArtists = [];
		$http({
			method : 'GET',
			url : 'TopArtistsServlet',
			params : {
				schoolName : $scope.selectedSchool.value
			}
		}).then(function successCallback(response) {
			console.log(response.data);
			$scope.topArtists = response.data;
		}, function errorCallback(response) {
		});

		$http({
			method : 'GET',
			url : 'TopTracksServlet',
			params : {
				schoolName : $scope.selectedSchool.value
			}
		}).then(function successCallback(response) {
			console.log(response.data);
			$scope.topTracks = response.data;
		}, function errorCallback(response) {
		});

		$http({
			method : 'GET',
			url : 'AudioFeaturesServlet',
			params : {
				schoolName : $scope.selectedSchool.value
			}
		}).then(function successCallback(response) {
			console.log(response.data);
			fillGauges("#valence", response.data.valence);
			fillGauges("#danceability", response.data.danceability);
			fillGauges("#energy", response.data.energy);

			// $scope.topTracks = response.data;
		}, function errorCallback(response) {
		});

		$http({
			method : 'GET',
			url : 'GenresServlet',
			params : {
				schoolName : $scope.selectedSchool.value
			}
		}).then(function successCallback(response) {
			console.log(response.data);
			pie.updateProp("data.content", response.data);
			// $scope.topTracks = response.data;
		}, function errorCallback(response) {
		});

		$http({
			method : 'GET',
			url : 'LanguageRatioServlet',
			params : {
				schoolName : $scope.selectedSchool.value
			}
		}).then(function successCallback(response) {
			console.log("language ratio: " + response.data);
			if (gauge == null) {
				gauge = loadLiquidFillGauge("fillgauge", response.data.value);
			}
			gauge.update(response.data.value);
			var config1 = liquidFillGaugeDefaultSettings();
			config1.circleColor = "#FF7777";
			config1.textColor = "#FF4444";
			config1.waveTextColor = "#FFAAAA";
			config1.waveColor = "#FFDDDD";
			config1.circleThickness = 0.2;
			config1.textVertPosition = 0.2;
			config1.waveAnimateTime = 1000;

		}, function errorCallback(response) {
		});

		$http({
			method : 'GET',
			url : 'TopNationalArtists',
			params : {
				schoolName : $scope.selectedSchool.value
			}
		}).then(function successCallback(response) {
			console.log(response.data);
			$scope.nationalTopArtists = response.data;
		}, function errorCallback(response) {
		});

	}
});
