
var Page = klass({
	views: [],
	pageOpen: false,
	backgroundColor: null,
	initialize: function(views) {
		if (typeof views === 'Array') {
			this.views = views;
		} else if (views !== null && typeof views === 'object') {
			this.views = [];
			this.views.push(views);
		} else {
			this.views = [];
		}
	},
	addView: function(view) {
		this.views.push(view);
	},
	getViews: function() {
		return this.views;
	},
	setBackgroundColor: function(color) {
		this.backgroundColor = color;
	},
	show: function() {
		this.pageOpen = true;
	},
	hide: function() {
		this.pageOpen = false;
	}
});
