
/*
  item: {
    title: 'Title',
    subtitle: 'subtitle',
    icon: 'path/to/icon'
  }
*/
var ListView = View.extend({
  items: [],
  onItemClick: "",
  data: [],
  initialize: function(config) {
    console.log("initialize list view");
    this.config = config;
    this.items = config.items;
    this.onItemClick = config.onItemClick;
  },
  setItems: function(items) {
    this.items = items;
    return this.items;
  },
  getItems: function() {
    return this.items;
  },
  setOnItemClick: function(e) {
    console.log(e.toString());
    this.onItemClick = e;
  },
  getJSON: function() {
    itemClickString = null;
    if (this.onItemClick !== null) {
      itemClickString = this.onItemClick.toString();
    }
    return {
      items: this.items,
      onItemClick: itemClickString,
      data: this.data,
      type: 'listView'
    };
  }
});
