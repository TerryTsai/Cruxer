(function(window) {

    window.RouteView = React.createClass({

        render: function() {
            return (
                <div className='col-1-4'>
                    <div className='content route'>
                        <a className="route__link" href={"design.html?id=" + this.props.route.id }>
                            <div className='route__link__thumb'>
                                <img src={this.props.route.thumbnail}></img>
                            </div>
                            <div className='route__link__info'>
                                {this.props.route.name + ' | ' + this.props.route.account.username}
                            </div>
                            <div className="route__link__date">
                                {moment(this.props.route.date).fromNow()}
                            </div>
                        </a>
                    </div>
                </div>
            );
        }
    });

}(window));