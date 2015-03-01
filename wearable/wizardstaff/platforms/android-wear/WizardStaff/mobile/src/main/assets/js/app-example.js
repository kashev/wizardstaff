
var parseFeed = function(data, quantity) {
  var items = [];
  for(var i = 0; i < quantity; i++) {
    // Always upper case the description string
    var title = data.list[i].weather[0].main;
    title = title.charAt(0).toUpperCase() + title.substring(1);
 

    var date = new Date(data.list[i].dt_txt);

    var displayDate = (date.getMonth() + 1) + '/' +date.getDate()+ '/' +date.getFullYear()+ ' ' +formatDateTime(date);

    // Get date/time substring
    var time = data.list[i].dt_txt;
    time = time.substring(time.indexOf('-') + 1, time.indexOf(':') + 3);
 
    // Add to menu items array
    items.push({
      title:title,
      subtitle:displayDate,
      data: data.list[i]
    });
  }
 
  // Finally return whole array
  return items;
};

var formatDateTime = function(date) { // This is to display 12 hour format like you asked
  var hours = date.getHours();
  var minutes = date.getMinutes();
  var ampm = hours >= 12 ? 'pm' : 'am';
  hours = hours % 12;
  hours = hours ? hours : 12; // the hour '0' should be '12'
  minutes = minutes < 10 ? '0'+minutes : minutes;
  var strTime = hours + ':' + minutes + ' ' + ampm;
  return strTime;
};

var app_id = "njgrse2JSpYBpFCSa";

StrapKit.Metrics.init(app_id);

// Show splash screen while waiting for data
var splashPage = StrapKit.UI.Page();
 
// Text element to inform user
var card = StrapKit.UI.TextView({
  position: 'center',
  text:'Loading data now...'
});
 
// Add to splashPage and show
splashPage.addView(card);
splashPage.show();

StrapKit.Metrics.logEvent("/show/splashPage");

// Make request to openweathermap.org
StrapKit.HttpClient(
  {
    url:'http://api.openweathermap.org/data/2.5/forecast?q=London',
    type:'json'
  },
  function(data) {

    var menuItems = parseFeed(data, 10);

    StrapKit.Metrics.logEvent("/httpClient/success", menuItems);

    var resultsPage = StrapKit.UI.Page();
    // Construct Menu to show to user
    var resultsMenu = StrapKit.UI.ListView({
        items: menuItems
    });

    // Add an action for SELECT
    resultsMenu.setOnItemClick(function(e) {
          // Get that forecast
        var forecast = e.item.data;
        
        // Assemble body string
        var content = forecast.weather[0].description;
       
        // Capitalize first letter
        content = content.charAt(0).toUpperCase() + content.substring(1);
       
        // Add temperature, pressure etc
        content += '\nTemperature: ' + Math.round(forecast.main.temp - 273.15) + '°C' 
        + '\nPressure: ' + Math.round(forecast.main.pressure) + ' mbar' +
          '\nWind: ' + Math.round(forecast.wind.speed) + ' mph, ' + 
          Math.round(forecast.wind.deg) + '°';
       
        var detailPage = StrapKit.UI.Page();
        // Create the Card for detailed view
        var detailCard = StrapKit.UI.Card({
          title:e.item.subtitle,
          body: content
        });
        detailPage.addView(detailCard);
        detailPage.show();

        StrapKit.Metrics.logEvent("show/detailPage", e.item.data);
    });
 
    // Show the Menu, hide the splash
    resultsPage.addView(resultsMenu);
    resultsPage.show();

    StrapKit.Metrics.logEvent("show/resultsPage");

  },
  function(error) {
    console.log(error);
  }
);