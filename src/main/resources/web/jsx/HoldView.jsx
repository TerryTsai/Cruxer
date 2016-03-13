(function(window) {

    var handleClick = function(props) {
        props.engine._id = props.hold.id;
        props.engine._type = "holds";
    };

    window.HoldView = React.createClass({

        render: function() {
            return (
                <span onClick={handleClick.bind(this, this.props)} className="w2-10 left center">
                    <img src={this.props.hold.thumbnail}></img>
                    <h1>{this.props.hold.account.username}</h1>
                    <h1>{moment(this.props.hold.date).fromNow()}</h1>
                </span>
            );
        }

    });

}(window));