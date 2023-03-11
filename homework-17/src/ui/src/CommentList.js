import React, { Component } from 'react';
import {Button, ButtonGroup, Container, Table} from 'reactstrap';
import AppNavbar from './AppNavbar';

class CommentList extends Component {

    emptyComment = {
        name: '',
        author: '',
        genre: '',
        comments: []
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyComment
        };
    }

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const comment = await (await fetch(`/api/comments/${this.props.match.params.id}`)).json();
            this.setState({item: comment});
        }
    }

    async remove(id) {
        let bookId = this.state.item.id;
        await fetch(`/api/comments/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            fetch(`/api/comments/${bookId}`)
                .then(response => response.json())
                .then(data => this.setState({item: data}));
        });
    }

    render() {
        const item = this.state.item;

        let commentList;
        if (item) {
            commentList = item.comments.map(comment => {
                return <tr key={comment.id || ''}>
                    <td width="30%" style={{whiteSpace: 'nowrap'}}>{comment.id || ''}</td>
                    <td width="30%">{comment.text || ''}</td>
                    <td>
                        <ButtonGroup>
                            <Button size="sm" color="danger" onClick={() => this.remove(comment.id)}>Delete</Button>
                        </ButtonGroup>
                    </td>
                </tr>
            });
        }

        return <div>
            <AppNavbar/>
            <Container fluid>
                <h3>Comments</h3>
                <Table className="mt-4">
                    <thead>
                    <tr>
                        <th width="30%">Text</th>
                        <th width="40%">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {commentList}
                    </tbody>
                </Table>
            </Container>
        </div>
    }
}
export default CommentList;