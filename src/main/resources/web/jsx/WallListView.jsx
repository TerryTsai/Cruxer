window.WallListView = React.createClass({

    load: function(pg) {
        $.ajax({
            url: "walls?size=8&page=" + pg,
            dataType: 'json',
            success: function(data) {
                if (data && data.length > 0 && pg >= 0) {
                   this.setState({walls: data, page: pg});
                }
            }.bind(this)
        });
    },

    next: function() {
        this.load(this.state.page + 1);
    },

    prev: function() {
        this.load(this.state.page - 1);
    },

    getInitialState: function() {
        return {
            walls : [],
            pg : 0
        };
    },

    componentDidMount: function() {
        this.load(0);
    },

    render: function() {
        return (
            <div>
                <div className='grid grid-pad'>
                    {
                        this.state.walls.map(function(wall) {
                            return <WallView key={wall.id} wall={wall} engine={this.props.engine}/>;
                        }.bind(this))
                    }
                </div>
                <div className='grid grid-pad'>
                    <div className='col-1-4 push-1-4 mobile-col-1-2'>
                        <button className='content wall__button' onClick={this.prev} >Prev</button>
                    </div>
                    <div className='col-1-4 mobile-col-1-2'>
                        <button className='content wall__button' onClick={this.next} >Next</button>
                    </div>
                </div>
            </div>
        );
    }

});