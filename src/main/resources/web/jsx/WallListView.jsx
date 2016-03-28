window.WallListView = React.createClass({

    load: function(pg) {
        $.ajax({
            url: "walls?size=20&page=" + pg,
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
                <div>
                    {
                        this.state.walls.map(function(wall) {
                            return <WallView
                                key={wall.id}
                                wall={wall}
                                engine={this.props.engine}
                                onSelect={this.props.onSelect}
                            />;
                        }.bind(this))
                    }
                </div>
                <div>
                    <div className='w5-10 left'>
                        <button className='wall__button border-box' onClick={this.prev} >Prev</button>
                    </div>
                    <div className='w5-10 left'>
                        <button className='wall__button border-box' onClick={this.next} >Next</button>
                    </div>
                </div>
            </div>
        );
    }

});