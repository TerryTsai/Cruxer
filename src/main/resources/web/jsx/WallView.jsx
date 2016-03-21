(function(window) {

    var handleClick = function(props) {
        if (props.engine) {
            props.engine._id = props.wall.id;
            props.engine._type = "walls";
        }
    };

    window.WallView = React.createClass({

        render: function() {
            return (
                <div className='col-1-4'>
                    <div className='content wall' onClick={handleClick.bind(this, this.props)}>
                        <div className='wall__link__thumb'>
                            <img src={this.props.wall.thumbnail}></img>
                        </div>
                        <div className='wall__link__info'>
                            {this.props.wall.account.username}
                        </div>
                        <div className="wall__link__date">
                            {moment(this.props.wall.date).fromNow()}
                        </div>
                    </div>
                </div>
            );
        }

    });

}(window));