window.HoldListView = React.createClass({

    load: function() {
        $.ajax({
            url: "debug/holds",
            dataType: 'json',
            success: function(data) {
                this.setState({holds: data});
            }.bind(this)
        });
    },

    getInitialState: function() {
        return {holds : []};
    },

    componentDidMount: function() {
        this.load();
    },

    render: function() {
        return (
            <div>
                <h1>{this.props.header}</h1>
                <div>
                    {
                        this.state.holds.map(function(hold) {
                            return <HoldView key={hold.id} hold={hold} engine={this.props.engine}/>;
                        }.bind(this))
                    }
                </div>
            </div>
        );
    }

});