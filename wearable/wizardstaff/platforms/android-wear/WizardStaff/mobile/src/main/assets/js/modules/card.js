var Card = View.extend({
	title: null,
	body: null,
	initialize: function(config) {
		this.config = config;
		this.title = config.title;
		this.body = config.body;
		this.onCallback = config.onClick;
	},
	setOnClick: function(event) {
		this.onCallback = event;
	},
	getJSON: function(){
		var callbackString = null;
		if (this.onCallback != null) {
			callbackString = this.onCallback.toString();
		}
		return {
			type: 'card',
			id: this.id,
			onClick: callbackString,
			title: this.title,
			body: this.body
		};
	}
});