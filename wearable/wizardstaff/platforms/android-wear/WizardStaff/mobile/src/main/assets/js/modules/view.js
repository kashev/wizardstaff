
var View = klass({
	config: null,
	onCallback: null,
	id: null,
	initialize: function(config) {
		this.config = config;
	},
	getConfig: function() {
		return this.config;
	},
	setId: function(idx) {
		if (this.id == null) {
			this.id = idx;
		}
	},
	getId: function() {
		return this.id;
	},
	getCallback: function() {
		return this.onCallback;
	},
	setCallback: function(callback) {
		this.onCallback = callback;
	},
	getJSON: function() {}
});