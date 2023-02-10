import axios from "axios"

export const Api = {
    getGenres() {
        return axios.get('/api/genres');
    },

    deleteGenre(id) {
        return axios.delete('/api/genres/' + id);
    },

    addGenre(genreData) {
        return axios.post('/api/genres', genreData);
    },

    editGenre(id, genreData) {
        return axios.put('/api/genres/' + id, genreData);
    },

    getGenre(id) {
        return axios.get('/api/genres/' + id);
    }

    /*getGenres() {
        return fetch('/api/genres');
    },

    addGenre(genreData) {
        return fetch('/api/genres', {
            method: 'POST',
            body: genreData
        });
    },

    editGenre(id, genreData) {
        return fetch('/api/genres/' + id, {
            method: 'PUT',
            body: genreData
        });
    },

    deleteGenre(id) {
        return fetch('/api/genres/' + id, {
            method: 'DELETE'
        });
    }*/
};