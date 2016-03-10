window.WallListView = React.createClass({

    load: function() {
        $.ajax({
            url: "debug/walls",
            dataType: 'json',
            success: function(data) {
                this.setState({walls: data});
            }.bind(this)
        });
    },

    getInitialState: function() {
        return {walls : []};
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
                        this.state.walls.map(function(wall) {
                            return <WallView key={wall.id} wall={wall} engine={this.props.engine}/>;
                        }.bind(this))
                    }
                </div>
            </div>
        );
    }

});