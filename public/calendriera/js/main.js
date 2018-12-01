/**
 * main.js
 * http://www.codrops.com
 *
 * Licensed under the MIT license.
 * http://www.opensource.org/licenses/mit-license.php
 * 
 * Copyright 2016, Codrops
 * http://www.codrops.com
 *
 *
 * Note: Code updated and edited by RedSpri
 */
var username = $('#username').data('username');
 ;(function(window) {

	'use strict';
	function extend( a, b ) {
		for( var key in b ) { 
			if( b.hasOwnProperty( key ) ) {
				a[key] = b[key];
			}
		}
		return a;
	} function getMousePos(e) {
		var posx = 0;
		var posy = 0;
		if (!e) var e = window.event;
		if (e.pageX || e.pageY) 	{
			posx = e.pageX;
			posy = e.pageY;
		}
		else if (e.clientX || e.clientY) 	{
			posx = e.clientX + document.body.scrollLeft
				+ document.documentElement.scrollLeft;
			posy = e.clientY + document.body.scrollTop
				+ document.documentElement.scrollTop;
		}
		return {
			x : posx,
			y : posy
		}
	}
	function mobilecheck() {
		var check = false;
		(function(a){if(/(android|ipad|playbook|silk|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4)))check = true})(navigator.userAgent||navigator.vendor||window.opera);
		return check;
	}
	function Day(options) {
		this.options = extend({}, this.options);
		extend(this.options, options);
		this.number = this.options.number;
		this.color = this.options.color;
		this.isActive = !this.options.inactive;
		this.casestatus = this.options.casestatus;
		this.dayleft = this.options.dayleft;
		this._layout();
	}

	Day.prototype.options = {
		number: 0,
		color: '#150763',
		inactive: false
	};

	Day.prototype._layout = function() {
		this.cube = document.createElement('div');
		this.cube.className = this.isActive ? 'cube ' : 'cube cube--inactive';
		if (this.isActive && this.casestatus != '') {
			this.cube.className = this.cube.className + " " + this.casestatus;
		}
		this.cube.innerHTML = '<div class="cube__side cube__side--back"></div><div class="cube__side cube__side--left"></div><div class="cube__side cube__side--right"></div><div class="cube__side cube__side--bottom"></div><div class="cube__side cube__side--top"></div><div class="cube__side cube__side--front"><div class="cube__number">' + (this.number+1) + '</div></div>';
		this.currentTransform = {translateZ: 0, rotateX: 0, rotateY: 0};
	};

	Day.prototype._rotate = function(ev) {
		anime.remove(this.cube);

		var dir = this._getDirection(ev),
			type = ev.type.toLowerCase() === 'mouseenter' ? 1 : -1,
			animationSettings = {
				targets: this.cube,
				duration: 500,
				easing: 'easeOutQuart',
				translateZ: type === 1 ? 100 : 0
			};

		switch(dir) {
			case 0:
				animationSettings.rotateX = type === 1 ? -180 : 0; 
				animationSettings.rotateY = 0;
				break; 
			case 1:
				animationSettings.rotateY = type === 1 ? -180 : 0; 
				animationSettings.rotateX = 0;
				break; 
			case 2:
				animationSettings.rotateX = type === 1 ? 180 : 0; 
				animationSettings.rotateY = 0;
				break; 
			case 3:
				animationSettings.rotateY = type === 1 ? 180 : 0; 
				animationSettings.rotateX = 0;
				break;
		};

		this.currentTransform = {
			translateZ: animationSettings.translateZ, 
			rotateX: animationSettings.rotateX, 
			rotateY: animationSettings.rotateY
		};

		anime(animationSettings);
	};

	Day.prototype._setContentTitleFx = function(contentTitleEl) {
		this.titlefx = new TextFx(contentTitleEl);
		this.titlefxSettings = {
			in: {
				duration: 200,
				delay: function(el, index) { return 900 + index*20 +  + 2000; },
				easing: 'easeOutExpo',
				opacity: {
					duration: 200,
					value: [0,1],
					easing:'linear'
				},
				rotateZ: function(el, index) { return [anime.random(-100,100), 0]; },
				translateX: function(el, index) {
					return [anime.random(-200,-100),0];
				}
			},
			out: {
				duration: 800,
				delay: 300,
				easing: 'easeInExpo',
				opacity: 0,
				translateY: -350
			}
		};
	};

	// From: https://codepen.io/noeldelgado/pen/pGwFx?editors=0110 by Noel Delgado (@noeldelgado).
	Day.prototype._getDirection = function(ev) {
		var obj = this.cube.querySelector('.cube__side--front'),
			w = obj.offsetWidth, 
			h = obj.offsetHeight,
			bcr = obj.getBoundingClientRect(),
			x = (ev.pageX - (bcr.left + window.pageXOffset) - (w / 2) * (w > h ? (h / w) : 1)),
			y = (ev.pageY - (bcr.top + window.pageYOffset) - (h / 2) * (h > w ? (w / h) : 1)),
			d = Math.round( Math.atan2(y, x) / 1.57079633 + 5 ) % 4;
		return d;
	};

	function Calendar(el) {
		this.el = el;
		this.calendarDays = [].slice.call(this.el.querySelectorAll('.cube'));
		this.cubes = document.createElement('div');
		this.cubes.className = 'cubes';
		this.el.appendChild(this.cubes);
		this.days = [];
		var self = this;
		this.calendarDays.forEach(function(d, pos) {
			var colord;
			if (d.getAttribute('data-status') != null) {
				if (d.getAttribute('data-status') == "collected") colord = '#076324';
				if (d.getAttribute('data-status') == "missed") colord = '#630707';
				if (d.getAttribute('data-status') == "collectable") colord = '#10a3a5';
			} else colord = '#150763'
			var day = new Day({
					number: pos,
					color: colord,
					inactive: d.hasAttribute('data-inactive'),
					casestatus: d.getAttribute('data-status') || '',
					dayleft: d.getAttribute('data-left') || ''
				}),
				content = contents[pos];

			if( content !== undefined ) {
				var contentTitle = contents[pos].querySelector('.content__title');
				day._setContentTitleFx(contentTitle);
			}

			self.days.push(day);
			self.cubes.appendChild(day.cube);
			self.el.removeChild(d);
			self._initDayEvents(day);
		});
		this.dayPreview = document.createElement('h2');
		this.dayPreview.className = 'title';
		this.el.appendChild(this.dayPreview);
		
		this._initEvents();
	}

	Calendar.prototype._initEvents = function() {
		var self = this;
		if( settings.tilt ) {
			this.mousemoveFn = function(ev) {
				if( self.isOpen ) {
					return false;
				};
				requestAnimationFrame(function() {
					var mousepos = getMousePos(ev);
					self._rotateCalendar(mousepos);
				});
			};

			this.handleOrientation = function() {
				if( self.isOpen ) {
					return false;
				};
				var y = event.gamma;
				y += 90;
				requestAnimationFrame(function() {
					var movement = {ry:40},
						rotY = 2*movement.ry / 180 * y - movement.ry;

					self.cubes.style.WebkitTransform = self.cubes.style.transform = 'rotate3d(0,-1,0,' + rotY + 'deg)';
				});
			};
			if( isMobile ) {
				window.addEventListener('deviceorientation', this.handleOrientation);
			}
			else {
				document.addEventListener('mousemove', this.mousemoveFn);
			}
		}

		this.backToCalendarFn = function(ev) {
			if( !self.isOpen || self.isAnimating ) return false;
			self.isAnimating = true;
			self._hidePreviewTitle();
			self._hideContent();

			anime({
				targets: self.el,
				duration: 1400,
				easing: 'easeInOutExpo',
				opacity: 1
			});

			for(var i = 0, totalDays = self.days.length; i < totalDays; ++i) {
				var day = self.days[i], isCurrent = self.currentDayIdx === i;

				if( isCurrent ) {
					day.isRotated = false;
				}
				
				anime({
					targets: day.cube,
					duration: 500,
					delay: isCurrent ? 1000 : function(el, index) {
						return 1100 + anime.random(0,300);
					},
					easing: 'easeOutBack',
					scale: [0,1],
					translateZ: 0,
					rotateX: 0,
					rotateY: 0,
					complete: isCurrent ? function() {
						self.isOpen = false;
						self.isAnimating = false;
					} : null
				});
			}
		};
		backCtrl.addEventListener('click', this.backToCalendarFn);
	};

	Calendar.prototype._initDayEvents = function(day) {
		var self = this;
		var instance = day;
		if( !isMobile ) {
			instance.mouseenterFn = function(ev) {
				if( instance.isRotated || self.isOpen ) {
					return false;
				};
				clearTimeout(colortimeout);
				var previewTitle = "Disponible dans " + day.dayleft + " jour(s)";
				if (instance.casestatus != null) {
					if (instance.casestatus == "collected") previewTitle = 'Récompense disponible !';
					if (instance.casestatus == "missed") previewTitle = 'Récompense passée';
					if (instance.casestatus == "collectable") previewTitle = 'Récompense récupérable';
				}
				instance.rotatetimeout = setTimeout(function() {
					colortimeout = setTimeout(function() { self._changeBGColor(instance.color); }, 30);
					instance._rotate(ev);
					self._showPreviewTitle(previewTitle, instance.number);
					instance.isRotated = true;
				}, 30);
			};
			instance.mouseleaveFn = function(ev) {
				if( self.isOpen ) {
					return false;
				};
				clearTimeout(instance.rotatetimeout);
				clearTimeout(colortimeout);
				if( instance.isRotated ) {
					colortimeout = setTimeout(function() { self._resetBGColor(); }, 35);
					instance._rotate(ev);
					self._hidePreviewTitle();
					instance.isRotated = false;
				}
			};
		}
		instance.clickFn = function(ev) {
			if( !instance.isActive || self.isAnimating ) {
				return false;
			}
			self.isAnimating = true;
			self.isOpen = true;
			self.currentDayIdx = instance.number;
			anime({
				targets: self.el,
				duration: 1400,
				easing: 'easeInOutExpo',
				opacity: 0,
				complete: function() {
					self.isAnimating = false;
				}
			});

			for(var i = 0, totalDays = self.days.length; i < totalDays; ++i) {
				var day = self.days[i],
					isCurrent = self.currentDayIdx === i

				if( isCurrent ) {
					self._showContent(instance);
				}

				anime({
					targets: day.cube,
					duration: 500,
					delay: isCurrent ? 600 : function() { return anime.random(0,300); },
					easing: isCurrent ? 'easeOutCubic' : 'easeInBack',
					scale: 0,
					translateZ: isCurrent ? -1000 : function() { return anime.random(-1000,-400); },
					rotateX: isCurrent ? -180 : function() { return anime.random(-180,180); },
					rotateY: isCurrent ? -180 : function() { return anime.random(-180,180); }
				});
			}
		};
		instance.cube.querySelector('.cube__side--front').addEventListener('mouseenter', instance.mouseenterFn);
		instance.cube.addEventListener('mouseleave', instance.mouseleaveFn);
		instance.cube.addEventListener('click', instance.clickFn);
		instance.cube.addEventListener('mousedown', function() {
			clearTimeout(instance.rotatetimeout );
		});
	};

	Calendar.prototype._rotateCalendar = function(mousepos) {
		var movement = {rx:3, ry:3},
			rotX = 2 * movement.rx / this.cubes.offsetHeight * mousepos.y - movement.rx,
			rotY = 2 * movement.ry / this.cubes.offsetWidth * mousepos.x - movement.ry;
		
		this.cubes.style.WebkitTransform = this.cubes.style.transform = 'rotate3d(-1,0,0,' + rotX + 'deg) rotate3d(0,1,0,' + rotY + 'deg)';
	};

	Calendar.prototype._showPreviewTitle = function(text, number) {
		this.dayPreview.innerHTML = text;
		this.dayPreview.setAttribute('data-number', parseInt(number+1));
		
		this.txtfx = new TextFx(this.dayPreview);
		this.txtfx.hide();
		this.dayPreview.style.opacity = 1;
		this.txtfx.show({
			in: {
				duration: 700,
				delay: function(el, index) { return index*20; },
				easing: 'easeOutCirc',
				opacity: 1,
				translateX: function(el, index) {
					return [(50+index*10),0]
				}
			}
		});
	};

	Calendar.prototype._hidePreviewTitle = function() {
		var self = this;
		if( this.txtfx ) {
			this.txtfx.hide();
		}
		this.dayPreview.style.opacity = 0;
	};

	Calendar.prototype._showContent = function(day) {
		var self = this;
		var content = contents[this.currentDayIdx],
			title = content.querySelector('.content__title'),
			description = content.querySelector('.content__description');

		self._changeBGColor(day.color);
		var meta = " Erreur :(";
		contentNumber.innerHTML = this.currentDayIdx + 1;
		if (day.casestatus != null) {
			if (day.casestatus == "missed") meta = 'Vous avez oublié cette récompense ou elle est dépassée !';
			if (day.casestatus == "collectable" || day.casestatus == "collected") {
				$.ajax({
					type: 'POST',
					// make sure you respect the same origin policy with this url:
					// http://en.wikipedia.org/wiki/Same_origin_policy
					url: '/calendrier-get',
					data: { 
						'username': username,
					},
				success: function(msg){
					meta = msg;
					content.classList.add('content__block--current');
					day.titlefx.hide();
					day.titlefx.show(day.titlefxSettings);
					var metaobject = document.createElement("p"); 
					metaobject.innerHTML = '<p class="content__meta">' + meta + '</p>';
					metaobject = document.getElementsByClassName("content__block--current")[0].appendChild(metaobject);
					anime({
						targets: [description],
						duration: 1100,
						delay: function(el, index) { return index === 0 ? 2500 : 2600 },
						easing: 'easeOutExpo',
						opacity: [0,1],
						translateY: [100,0]
					});
					anime({
						targets: [document.getElementsByClassName("content__meta")[0]],
						duration: 2200,
						delay: function(el, index) { return index === 0 ? 2500 : 2600 },
						easing: 'easeOutExpo',
						opacity: [0,1],
						translateY: [100,0]
					});
					anime({
						targets: backCtrl,
						duration: 1500,
						delay: 800,
						easing: 'easeOutExpo',
						opacity: [0,1],
						translateY: [50,0]
					});
					anime({
						targets: contentNumber,
						duration: 1500,
						delay: 900,
						easing: 'easeOutExpo',
						opacity: [0,1],
						translateX: [-200,0]
					});
				}
			});
			} else {
				content.classList.add('content__block--current');
				day.titlefx.hide();
				day.titlefx.show(day.titlefxSettings);
				var metaobject = document.createElement("p"); 
				metaobject.innerHTML = '<p class="content__meta">' + meta + '</p>';
				metaobject = document.getElementsByClassName("content__block--current")[0].appendChild(metaobject);
				anime({
					targets: [description],
					duration: 1100,
					delay: function(el, index) { return index === 0 ? 2500 : 2600 },
					easing: 'easeOutExpo',
					opacity: [0,1],
					translateY: [100,0]
				});
				anime({
					targets: [document.getElementsByClassName("content__meta")[0]],
					duration: 2200,
					delay: function(el, index) { return index === 0 ? 2500 : 2600 },
					easing: 'easeOutExpo',
					opacity: [0,1],
					translateY: [100,0]
				});
				anime({
					targets: backCtrl,
					duration: 1500,
					delay: 800,
					easing: 'easeOutExpo',
					opacity: [0,1],
					translateY: [50,0]
				});
				anime({
					targets: contentNumber,
					duration: 1500,
					delay: 900,
					easing: 'easeOutExpo',
					opacity: [0,1],
					translateX: [-200,0]
				});
			}
		}
	};

	Calendar.prototype._hideContent = function() {
		var self = this;
		var day = this.days[this.currentDayIdx],
			content = contents[this.currentDayIdx],
			title = content.querySelector('.content__title'),
			description = content.querySelector('.content__description'),
			meta = content.querySelector('.content__meta');
		anime({
			targets: contentNumber,
			duration: 800,
			easing: 'easeInExpo',
			opacity: 0,
			translateX: -200
		});
		anime({
			targets: backCtrl,
			duration: 1000,
			easing: 'easeInExpo',
			opacity: 0,
			translateY: 50
		});
		anime({
			targets: [description, meta],
			duration: 800,
			delay: function(el, index) { return index === 0 ? 150 : 0 },
			easing: 'easeInExpo',
			opacity: [1,0],
			translateY: [0,200]
		});
		setTimeout(function () { 
			var list = document.getElementsByClassName("content__meta");
			for (var i = 0; i < list.length; i++)list[i].remove();
		}, 900);
		if (day.casestatus == "collectable") {
			day.casestatus = "collected";
			day.color = "#076324";
			day.cube.innerHTML = '<div style="background-image: url(img/collected.png);" class="cube__side cube__side--back"></div><div style="background-image: url(img/collected.png);" class="cube__side cube__side--left"></div><div style="background-image: url(img/collected.png);" class="cube__side cube__side--right"></div><div style="background-image: url(img/collected.png);" class="cube__side cube__side--bottom"></div><div style="background-image: url(img/collected.png);" class="cube__side cube__side--top"></div><div style="background-image: url(img/collected.png);" class="cube__side cube__side--front"><div class="cube__number">' + (this.currentDayIdx + 1) + '</div></div>';
			if( !isMobile ) {
				day.mouseenterFn = function(ev) {
					if( day.isRotated || self.isOpen ) {
						return false;
					};
					clearTimeout(colortimeout);
					var previewTitle = "Disponible dans " + day.dayleft + " jour(s)";
					if (day.casestatus != null) {
						if (day.casestatus == "collected") previewTitle = 'Récompense récupérée';
						if (day.casestatus == "missed") previewTitle = 'Récompense oubliée';
						if (day.casestatus == "collectable") previewTitle = 'Récompense récupérable';
					}
					day.rotatetimeout = setTimeout(function() {
						colortimeout = setTimeout(function() { self._changeBGColor(day.color); }, 30);
						day._rotate(ev);
						self._showPreviewTitle(previewTitle, day.number);
						day.isRotated = true;
					}, 30);
				};
				day.mouseleaveFn = function(ev) {
					if( self.isOpen ) {
						return false;
					};
					clearTimeout(day.rotatetimeout);
					clearTimeout(colortimeout);
					if( day.isRotated ) {
						colortimeout = setTimeout(function() { self._resetBGColor(); }, 35);
						day._rotate(ev);
						self._hidePreviewTitle();
						day.isRotated = false;
					}
				};
			}
		day.cube.querySelector('.cube__side--front').addEventListener('mouseenter', day.mouseenterFn);
		day.cube.addEventListener('mouseleave', day.mouseleaveFn);
		day.cube.addEventListener('mousedown', function() {
			clearTimeout(day.rotatetimeout );
		});
		}
		var bcr = day.cube.getBoundingClientRect();
		day.titlefx.hide(day.titlefxSettings, function() {
			content.classList.remove('content__block--current');
		});
		setTimeout(function() { self._changeBGColor('#150763'); }, 30);
	};

	Calendar.prototype._changeBGColor = function(color) {
		bgEl.style.backgroundColor = color;
	};
	
	Calendar.prototype._getBGColor = function() {
		return bgEl.style.backgroundColor;
	};

	Calendar.prototype._resetBGColor = function() {
		bgEl.style.backgroundColor = defaultBgColor;
	};

	// Snow obj. Based on // https://gist.github.com/OmShiv/4368164.
	function Snow() {
		this.canvas = document.createElement('canvas');
		this.canvas.id = 'snow';
		this.canvas.className = 'background';
		this.canvas.style.background = defaultBgColor;
		document.body.insertBefore(this.canvas, document.body.firstElementChild);

		this.flakes = [];
		this.ctx = this.canvas.getContext('2d');
		this.flakeCount = 300;
		this.mX = -100,
		this.mY = -100

		this.width = this.canvas.width = window.innerWidth;
		this.height = this.canvas.height = window.innerHeight;

		this._init();
	}

	Snow.prototype._init = function() {
		var self = this;
		window.addEventListener('resize', function() {
			self.width = self.canvas.width = window.innerWidth;
			self.height = self.canvas.height = window.innerHeight;
		});

		for(var i = 0; i < this.flakeCount; ++i) {
			var x = Math.floor(Math.random() * this.width),
				y = Math.floor(Math.random() * this.height),
				size = (Math.random()*3.5) + .5,
				speed = size*.5,
				opacity = (Math.random() * 0.5) + 0.1;

			this.flakes.push({
				speed: speed,
				velY: speed,
				velX: 0,
				x: x,
				y: y,
				size: size,
				stepSize: (Math.random()) / 30,
				step: 0,
				opacity: opacity
			});
		}

		this._snow();
	};

	Snow.prototype._snow = function() {
		this.ctx.clearRect(0, 0, this.width, this.height);

		for(var i = 0; i < this.flakeCount; ++i) {
			var flake = this.flakes[i],
				x = this.mX,
				y = this.mY,
				minDist = 150,
				x2 = flake.x,
				y2 = flake.y,
				dist = Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y)),
				dx = x2 - x,
				dy = y2 - y;

			if( dist < minDist ) {
				var force = minDist / (dist * dist),
				xcomp = (x - x2) / dist,
				ycomp = (y - y2) / dist,
				deltaV = force / 2;
				flake.velX -= deltaV * xcomp;
				flake.velY -= deltaV * ycomp;
			}
			else {
				flake.velX *= .98;
				if( flake.velY <= flake.speed ) {
					flake.velY = flake.speed
				}
				flake.velX += Math.cos(flake.step += .05) * flake.stepSize;
			}

			this.ctx.fillStyle = "rgba(255,255,255," + flake.opacity + ")";
			flake.y += flake.velY;
			flake.x += flake.velX;

			if( flake.y >= this.height || flake.y <= 0 ) {
				this._reset(flake);
			}

			if( flake.x >= this.width || flake.x <= 0 ) {
				this._reset(flake);
			}

			this.ctx.beginPath();
			this.ctx.arc(flake.x, flake.y, flake.size, 0, Math.PI * 2);
			this.ctx.fill();
		}
		requestAnimationFrame(this._snow.bind(this));
	};

	Snow.prototype._reset = function(flake) {
		flake.x = Math.floor(Math.random() * this.width);
		flake.y = 0;
		flake.size = (Math.random() * 3.5) + .5;
		flake.speed = flake.size*.5,
		flake.velY = flake.speed;
		flake.velX = 0;
		flake.opacity = (Math.random() * 0.5) + 0.1;
	};

	var calendarEl = document.querySelector('.calendar'),
		calendarDays = [].slice.call(calendarEl.children),
		settings = {
			snow: true,
			tilt: false
		},
		bgEl = document.body,
		defaultBgColor = bgEl.style.backgroundColor,
		colortimeout,
		contentEl = document.querySelector('.content'),
		contents = contentEl.querySelectorAll('.content__block'),
		backCtrl = contentEl.querySelector('.btn-back'),
		contentNumber = contentEl.querySelector('.content__number'),
		isMobile = mobilecheck();

	function init() {
		layout();
	}

	function layout() {
		new Calendar(calendarEl);
		if( settings.snow ) {
			var snow = new Snow();
			bgEl = snow.canvas;
		}
	}

	init();

})(window);