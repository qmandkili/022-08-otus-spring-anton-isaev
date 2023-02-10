import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link, withRouter } from 'react-router-dom';

class GenreList extends Component {

    constructor(props) {
        super(props);
        this.state = {genres: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        fetch('/api/genres')
            .then(response => response.json())
            .then(data => this.setState({genres: data}));
    }

    async remove(id) {
        await fetch(`/api/genres/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            fetch('/api/genres')
                .then(response => response.json())
                .then(data => this.setState({genres: data}));
            /*let updatedGenres = [...this.state.genres].filter(i => i.id !== id);
            this.setState({genres: updatedGenres});*/
        });
    }

    render() {
        const {genres, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const genreList = genres.map(genre => {
            return <tr key={genre.id}>
                <td style={{whiteSpace: 'nowrap'}}>{genre.id}</td>
                <td>{genre.name}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/api/genres/" + genre.id}>Edit</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(genre.id)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/api/genres/new">Add Genre</Button>
                    </div>
                    <h3>Genres</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="30%">Name</th>
                            <th width="40%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {genreList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}
export default withRouter(GenreList);