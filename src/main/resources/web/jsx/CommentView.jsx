(function(window) {

    window.CommentView = React.createClass({

        render: function() {
            return (
                <div className='col-1-1'>
                    <div className='content comment'>
                        <div className='comment__text'>
                            {this.props.comment.text}
                        </div>
                        <div className="comment__date">
                            {moment(this.props.comment.date).fromNow()}
                        </div>
                    </div>
                </div>
            );
        }

    });

}(window));