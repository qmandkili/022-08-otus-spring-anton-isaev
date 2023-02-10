const HtmlWebpackPlugin = require('html-webpack-plugin')
const path = require('path');

module.exports = {
    entry: './index.js',
    devtool: 'inline-source-map',
    mode: 'development',
    output: {
        path: path.resolve(__dirname),
        filename: 'bundle.js',
        libraryTarget: 'umd'
    },

    devServer: {
        static: path.resolve(__dirname),
        compress: true,
        port: 9000,
        host: 'localhost',
        open: true,
        proxy: {
            '*': {
              target: 'http://localhost:8080',
              secure: false,
              changeOrigin: true
            }
        }
    },

    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /(node_modules|bower_components|build)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", '@babel/preset-react']
                    }
                }
            }
        ]
    },
    plugins: [
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: 'index.html'
        })
    ]
}
