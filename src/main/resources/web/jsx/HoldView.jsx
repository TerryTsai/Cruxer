(function(window) {

    var handleClick = function(props) {
        if (props.engine) {
            props.engine._id = props.hold.id;
            props.engine._type = "holds";
        }
    };

    window.HoldView = React.createClass({

        render: function() {
            return (
                <div className='col-1-4'>
                    <div className='content hold' onClick={handleClick.bind(this, this.props)}>
                        <div className='hold__link__thumb'>
                            <img src={this.props.hold.thumbnail}></img>
                        </div>
                        <div className='hold__link__info'>
                            {this.props.hold.account.username}
                        </div>
                        <div className="hold__link__date">
                            {moment(this.props.hold.date).fromNow()}
                        </div>
                    </div>
                </div>
            );
        }

    });

}(window));