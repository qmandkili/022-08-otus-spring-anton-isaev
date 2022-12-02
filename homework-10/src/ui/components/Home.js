import React, { Component } from 'react';
//import './src/App.css';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import { Button, Container } from 'reactstrap';

class Home extends Component {
    /*state = {
        genres: []
    };

    async componentDidMount() {
        const response = await fetch('api/genres');
        const body = await response.json();
        this.setState({genres: body});
    }

    render() {
        const {genres} = this.state;

        const genreList = genres.map(genre => {
            return <tr key={genre.id}>
                <td style={{whiteSpace: 'nowrap'}}>{genre.id}</td>
                <td>{genre.name}</td>
            </tr>
        });

        return (
            <Container fluid>
                <Button color="link"><Link to="/api/genres">Genres</Link></Button>
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
        );
    }*/
    render() {
        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <Button color="link"><Link to="/api/genres">Genres</Link></Button>
                    <Button color="link"><Link to="/api/authors">Authors</Link></Button>
                    <Button color="link"><Link to="/api/books">Books</Link></Button>
                </Container>
            </div>
        );
    }
}
export default Home;