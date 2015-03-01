
var TextView = View.extend({
	position: 'left',
	initialize: function(config) {
		this.config = config;
		this.position = config.position,
		this.text = config.text;
	},
	getJSON: function() {
		this.supr();
		return {
			position: this.position,
			text: this.text,
			type: 'text',
			id: this.id
		};
	}
});