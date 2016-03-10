window.HoldView = React.createClass({
    render: function() {
        return (
            <span className="w2-10 left center debug-red border-box">
                <img src={this.props.hold.thumbnail}></img>
                <h1>{this.props.hold.account.username}</h1>
                <h1>{moment(this.props.hold.date).fromNow()}</h1>
            </span>
        );
    }
});
