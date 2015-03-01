/*
 * Strap App - for Pebble and Android Wear.
 * Wizard Staff
 */


/*
 * REQUIRES
 */

var StrapKit = require('strapkit');
require('firebase');

/*
 * STRAP METRICS
 */

var app_id = "t9kakrw84oQa37c9u"; // wizardstaff
StrapKit.Metrics.Init(app_id); // Metrics will start logging sensor data


/*
 * Show Splash Screen
 */
// Show splash screen while waiting for data
var splashPage = StrapKit.UI.Page();
// Text element to inform user
var card = StrapKit.UI.TextView({
    position: 'center',
    text: 'Loading data now...'
});
// Add to splashPage and show
splashPage.addView(card);
splashPage.show();


/*
 * Load Firebase Database
 */
var myFirebaseRef = new Firebase("https://wizardstaff.firebaseio.com/");


myFirebaseRef.child("Sparks").on("value", function(snapshot) {
    StrapKit.Metrics.logEvent("/firebase/", snapshot.val());
});
var data = "unchanged";
myFirebaseRef.child("Sparks/BlackMage/NumDrinks").once("value", function(snapshot){
    data = snapshot.val()
});


/*
 * Show the data the first time
 */
myFirebaseRef.child("Sparks").once("value", function(snapshot){
    var resultsPage = StrapKit.UI.Page();
    var menuItems = [];
    snapshot.forEach(function(child_snapshot){
        var player_data = child_snapshot.val();
        var list_item = {
            title : player_data.Owner,
            subtitle : player_data.NumDrinks,
            data : {glass_name: child_snapshot.key()}
        }
        menuItems.push(list_item);
    });
    var resultsMenu = StrapKit.UI.ListView({
        items: menuItems
    });

    resultsMenu.setOnItemClick(function(e) {
        console.log(JSON.stringify(e.item));
        var player_name = e.item.title;
        var glass_name = e.item.data.glass_name;
        var drinks = e.item.subtitle;
        var content = "Glass Name: " + glass_name + "\nDrinks : " + drinks;

        var detailPage = StrapKit.UI.Page();
        var detailCard = StrapKit.UI.Card({
            title : player_name,
            body : content
        });

        detailPage.addView(detailCard);
        detailPage.show();
    });

    // Show the Menu, hide the splash
    resultsPage.addView(resultsMenu);
    resultsPage.show();
});

