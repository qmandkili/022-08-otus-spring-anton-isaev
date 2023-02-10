import React, { Component } from 'react';
//import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import GenreEdit from "./GenreEdit";
import AuthorList from "./AuthorList";
import AuthorEdit from "./AuthorEdit";
import BookList from "./BookList";
import BookEdit from "./BookEdit";
import CommentList from "./CommentList";
import GenreList from "./GenreList";

class App extends Component {
  render() {
    return (
        <Router>
          <Switch>
            <Route path='/' exact={true} component={Home}/>
            <Route path='/api/genres' exact={true} component={GenreList}/>
            <Route path='/api/genres/:id' component={GenreEdit}/>
            <Route path='/api/authors' exact={true} component={AuthorList}/>
            <Route path='/api/authors/:id' component={AuthorEdit}/>
            <Route path='/api/books' exact={true} component={BookList}/>
            <Route path='/api/books/:id' component={BookEdit}/>
            <Route path='/api/comments/:id' component={CommentList}/>
          </Switch>
        </Router>
    )
  }
}

export default App;
