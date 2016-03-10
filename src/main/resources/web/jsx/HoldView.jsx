window.HoldView = React.createClass({
    render: function() {
        return (
            <div>

                <h1>by {this.props.hold.account.username} {moment(this.props.hold.date).fromNow()}</h1>
            </div>
        );
    }
});
