window.HoldListView = React.createClass({

    load: function(pg) {
        $.ajax({
            url: "holds?size=20&page=" + pg,
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
                <div>
                    {
                        this.state.holds.map(function(hold) {
                            return <HoldView key={hold.id} hold={hold} engine={this.props.engine}/>;
                        }.bind(this))
                    }
                </div>
                <div>
                    <div className='w5-10 left'>
                        <button className='hold__button border-box' onClick={this.prev} >Prev</button>
                    </div>
                    <div className='w5-10 left'>
                        <button className='hold__button border-box' onClick={this.next} >Next</button>
                    </div>
                </div>
            </div>
        );
    }

});