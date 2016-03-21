window.HoldListView = React.createClass({

    load: function(pg) {
        $.ajax({
            url: "holds?size=8&page=" + pg,
            dataType: 'json',
            success: function(data) {
                if (data && data.length > 0 && pg >= 0) {
                   this.setState({holds: data, page: pg});
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
            holds : [],
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
                        this.state.holds.map(function(hold) {
                            return <HoldView key={hold.id} hold={hold} engine={this.props.engine}/>;
                        }.bind(this))
                    }
                </div>
                <div className='grid grid-pad'>
                    <div className='col-1-4 push-1-4 mobile-col-1-2'>
                        <button className='content hold__button' onClick={this.prev} >Prev</button>
                    </div>
                    <div className='col-1-4 mobile-col-1-2'>
                        <button className='content hold__button' onClick={this.next} >Next</button>
                    </div>
                </div>
            </div>
        );
    }

});