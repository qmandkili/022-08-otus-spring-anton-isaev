import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import Select from 'react-select'
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';

class BookEdit extends Component {

    emptyBook = {
        name: '',
        author: '',
        genre: ''
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyBook
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleAuthorChange = (selectedOption) => {
        let item = {...this.state.item};
        item['author'] = selectedOption.value;
        this.setState({item});
    }

    handleGenreChange = (selectedOption) => {
        let item = {...this.state.item};
        item['genre'] = selectedOption.value;
        this.setState({item});
    }

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const book = await (await fetch(`/api/books/${this.props.match.params.id}`)).json();
            this.setState({item: book});
        }

        fetch('/api/authors')
            .then(response => response.json())
            .then(data => this.setState({authorOptions: data.map((author, index) => {
                return {
                    label: author.firstName + ' ' + author.secondName,
                    value: author,
                    key: index
                }
                })})
            );
        fetch('/api/genres')
            .then(response => response.json())
            .then(data => this.setState({genreOptions: data.map((genre, index) => {
                    return {
                        label: genre.name,
                        value: genre,
                        key: index
                    }
                })})
            );
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item[name] = value;
        this.setState({item});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item} = this.state;

        await fetch('/api/books' + (item.id ? '/' + item.id : ''), {
            method: (item.id) ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        });
        this.props.history.push('/api/books');
    }

    render() {
        const {item, authorOptions, genreOptions} = this.state;
        const title = <h2>{item.id ? 'Edit Book' : 'Add Book'}</h2>;

        return <div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Label for="name">Name</Label>
                        <Input type="text" name="name" id="name" value={item.name || ''}
                               onChange={this.handleChange} autoComplete="name"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="author">Author</Label>
                        <Select options={authorOptions} name="author" id="author" classNamePrefix="select"
                                onChange={this.handleAuthorChange}/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="genre">Genre</Label>
                        <Select options={genreOptions} name="genre" id="genre" classNamePrefix="select"
                                onChange={this.handleGenreChange}/>
                    </FormGroup>
                    <FormGroup>
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Button color="secondary" tag={Link} to="/api/books">Cancel</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }
}
export default BookEdit;