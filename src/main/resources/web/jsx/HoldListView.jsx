window.HoldListView = React.createClass({

    getInitialState: function() {
        return {holds : []};
    },

    render: function() {
        var hvs = this.state.holds.map(function(hold) {
            return (
                <HoldView key={hold.id} hold={hold}/>
            );
        });

        return (
            <div>
                <div>
                {this.state.holds.map(function(hold) {
                    return <HoldView key={hold.id} hold={hold}/>;})}
                </div>
                <button onClick={this.load}>TEST</button>
            </div>
        );
    },

    load: function() {
        $.ajax({
            url: "debug/holds",
            dataType: 'json',
            success: function(data) {
                this.setState({holds: data});
            }.bind(this)
        });
    }
});