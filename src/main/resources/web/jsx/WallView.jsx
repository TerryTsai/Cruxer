(function(window) {

    var handleClick = function(props) {
        if (props.engine) {
            props.engine._id = props.wall.id;
            props.engine._type = "walls";
            props.onSelect(props.wall.thumbnail);
        }
    };

    window.WallView = React.createClass({

        render: function() {
            return (
                <div className='w2-10 border-box left wall' onClick={handleClick.bind(this, this.props)}>
                    <div className='wall__link__thumb'>
                        <img src={this.props.wall.thumbnail}></img>
                    </div>
                </div>
            );
        }

    });

}(window));