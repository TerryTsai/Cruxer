window.RouteListView = React.createClass({

        load: function(pg) {
            $.ajax({
                url: "routes?size=8&page=" + pg,
                dataType: 'json',
                success: function(data) {
                    if (data && data.length > 0 && pg >= 0) {
                       this.setState({routes: data, page: pg});
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
                routes : [],
                page : 0
            };
        },

        componentDidMount: function() {
            this.load(0);
        },

        render: function() {
            return (
                <div>
                    <div className ='grid grid-pad'>
                        {this.state.routes.map(function(route) {
                                return <RouteView key={route.id} route={route}/>;
                        }.bind(this))}
                    </div>
                    <div className='grid grid-pad'>
                        <div className='col-1-4 push-1-4 mobile-col-1-2'>
                            <button className='content route__button' onClick={this.prev} >Prev</button>
                        </div>
                        <div className='col-1-4 mobile-col-1-2'>
                            <button className='content route__button' onClick={this.next} >Next</button>
                        </div>
                    </div>
                </div>
            );
        }

});