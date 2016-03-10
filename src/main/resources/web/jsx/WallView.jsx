(function(window) {

    var handleClick = function(props) {
        props.engine.select(props.wall.id, "walls");
    };

    window.WallView = React.createClass({

        render: function() {
            return (
                <span onClick={handleClick.bind(this, this.props)} className="w2-10 left center">
                    <img src={this.props.wall.thumbnail}></img>
                    <h1>{this.props.wall.account.username}</h1>
                    <h1>{moment(this.props.wall.date).fromNow()}</h1>
                </span>
            );
        }

    });

}(window));