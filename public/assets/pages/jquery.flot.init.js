/**
 * Theme: Adminto Admin Template
 * Author: Coderthemes
 * Module/App: Flot-Chart
 */

! function($) {
	"use strict";

	var FlotChart = function() {
		this.$body = $("body")
		this.$realData = []
	};

	//creates plot graph
	FlotChart.prototype.createPlotGraph = function(selector, data1, data2, labels, colors, borderColor, bgColor) {
		//shows tooltip
		function showTooltip(x, y, contents) {
			$('<div id="tooltip" class="tooltipflot">' + contents + '</div>').css({
				position : 'absolute',
				top : y + 5,
				left : x + 5
			}).appendTo("body").fadeIn(200);
		}


		$.plot($(selector), [{
			data : data1,
			label : labels[0],
			color : colors[0]
		}, {
			data : data2,
			label : labels[1],
			color : colors[1]
		}], {
			series : {
				lines : {
					show : true,
					fill : true,
					lineWidth : 2,
					fillColor : {
						colors : [{
							opacity : 0.4
						}, {
							opacity : 0.4
						}]
					}
				},
				points : {
					show : false
				},
				shadowSize : 0
			},

			grid : {
				hoverable : true,
				clickable : true,
				borderColor : borderColor,
				tickColor : "#f9f9f9",
				borderWidth : 1,
				labelMargin : 10,
				backgroundColor : bgColor
			},
			legend : {
				position : "ne",
				margin : [0, -24],
				noColumns : 0,
				labelBoxBorderColor : null,
				labelFormatter : function(label, series) {
					// just add some space to labes
					return '' + label + '&nbsp;&nbsp;';
				},
				width : 30,
				height : 2
			},
			yaxis : {
				tickColor : 'transparent',
				tickLength: 0,
				font : {
					color : '#bdbdbd'
				}
			},
			xaxis : {
				tickColor : '#transparent',
				tickLength: 0,
				font : {
					color : '#bdbdbd'
				}
			},
			tooltip : true,
			tooltipOpts : {
				content : '%s: Value of %x is %y',
				shifts : {
					x : -60,
					y : 25
				},
				defaultTheme : false
			}
		});
	},
	//end plot graph

	//creates Pie Chart
	FlotChart.prototype.createPieGraph = function(selector, labels, datas, colors) {
		var data = [{
			label : labels[0],
			data : datas[0]
		}, {
			label : labels[1],
			data : datas[1]
		}, {
			label : labels[2],
			data : datas[2]
		}];
		var options = {
			series : {
				pie : {
					show : true,
					stroke: {
						width: 0,
						color: '#2f3e47'
					}
				}
			},
			legend : {
				show : true
			},
			grid : {
				hoverable : true,
				clickable : true
			},
			colors : colors,
			tooltip : true,
			tooltipOpts : {
				content : "%s, %p.0%"
			}
		};

		$.plot($(selector), data, options);
	},

	//returns some random data
	FlotChart.prototype.randomData = function() {
		var totalPoints = 300;
		if (this.$realData.length > 0)
			this.$realData = this.$realData.slice(1);

		// Do a random walk
		while (this.$realData.length < totalPoints) {
			var y = 0;
            jQuery.ajax({
                type    : 'get',
                url     : '/api/realmongo',
                dataType: 'json',
                success : function(data) {
                    y = jQuery.parseJSON(JSON.stringify(data));
                    console.log(y);

                    this.$realData.push(y);
                },
                async: false
            });


		}

		// Zip the generated y values with the x values
		var res = [];
		for (var i = 0; i < this.$realData.length; ++i) {
			res.push([i, this.$realData[i]])
		}

		return res;
	}, FlotChart.prototype.createRealTimeGraph = function(selector, data, colors) {
		var plot = $.plot(selector, [data], {
			colors : colors,
			series : {
				grow : {
					active : false
				}, //disable auto grow
				shadowSize : 0, // drawing is faster without shadows
				lines : {
					show : true,
					fill : true,
					lineWidth : 2,
					steps : false
				}
			},
			grid : {
				show : true,
				aboveData : false,
				color : '#36404a',
				labelMargin : 15,
				axisMargin : 0,
				borderWidth : 0,
				borderColor : null,
				minBorderMargin : 5,
				clickable : true,
				hoverable : true,
				autoHighlight : false,
				mouseActiveRadius : 20
			},
			tooltip : true, //activate tooltip
			tooltipOpts : {
				content : "Value is : %y.0" + "%",
				shifts : {
					x : -30,
					y : -50
				}
			},
			yaxis : {
				min : 0,
				max : 100,
				tickColor : '#transparent',
				tickLength: 0
			},
			xaxis : {
				show : false
			}
		});

		return plot;
	},

	//initializing various charts and components
	FlotChart.prototype.init = function() {


		//real time data representation
		var plot = this.createRealTimeGraph('#flotRealTime', this.randomData(), ['#71b6f9']);
		plot.draw();
		var $this = this;
		function updatePlot() {
			plot.setData([$this.randomData()]);
			// Since the axes don't change, we don't need to call plot.setupGrid()
			plot.draw();
			setTimeout(updatePlot, $('html').hasClass('mobile-device') ? 1000 : 1000);
		}

		updatePlot();

	},

	//init flotchart
	$.FlotChart = new FlotChart, $.FlotChart.Constructor =
	FlotChart

}(window.jQuery),

//initializing flotchart
function($) {
	"use strict";
	$.FlotChart.init()
}(window.jQuery);


