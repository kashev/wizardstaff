/*
 * wizard staff
 */

$(document).ready(function() {

    console.log('loading firebase ref');
    var myFirebaseRef = new Firebase("https://wizardstaff.firebaseio.com/");

    function updateBarGraph(snapshot){
        var plot_data = [];
        var ticks_data = [];
        var idx = 0;
        snapshot.forEach(function(child_snapshot){
            var player_data = child_snapshot.val();
            var list_item = [idx, player_data.NumDrinks];
            var tick_item = [idx, player_data.Owner];
            if (player_data.Owned === 1)
            {
                idx = idx + 1;
                plot_data.push(list_item);
                ticks_data.push(tick_item);
            }
        });

        var data = [{
            data: plot_data,
            color: '#377D8C',
            label:'Drinks',
            bars: {show: true, align:'center', barWidth:0.6}
        }];

        var options = {
            xaxis: {
                ticks: ticks_data,
            },
            yaxis: {
                tickDecimals: 0
            }
        };

        $.plot($("#drink-plot"), data, options);
    }

    /*
     * Show the data the first time
     */
    myFirebaseRef.child("Sparks").once("value", updateBarGraph);
    myFirebaseRef.child("Sparks").on("value", updateBarGraph);



});
