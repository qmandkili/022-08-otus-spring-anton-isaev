import React, { Component } from 'react';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import { Button, Container } from 'reactstrap';

class Home extends Component {

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