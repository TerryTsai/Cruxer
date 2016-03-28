window.CommentListView = React.createClass({

    load: function(pg) {
        $.ajax({
            url: "comments?type=route&id=" + this.props.routeId + "&size=8&page=" + pg,
            dataType: 'json',
            success: function(data) {
                if (data && data.length > 0 && pg >= 0) {
                   this.setState({comments: data, page: pg});
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
            comments : [],
            pg : 0
        };
    },

    componentDidMount: function() {
        this.load(0);
    },

    render: function() {
        return (
            <div>
                <div className=''>
                    {
                        this.state.comments.map(function(comment) {
                            return <CommentView key={comment.id} comment={comment}/>;
                        }.bind(this))
                    }
                </div>
                <div className=''>
                    <div className='col-1-4 push-1-4 mobile-col-1-2'>
                        <button className='content comment__button' onClick={this.prev} >Prev</button>
                    </div>
                    <div className='col-1-4 mobile-col-1-2'>
                        <button className='content comment__button' onClick={this.next} >Next</button>
                    </div>
                </div>
            </div>
        );
    }

});