<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

//A ne pas delete rout d'Auth AP
Auth::routes();



Route::group([
    'middleware' => ['auth'],
], function () {
    Route::get('/', function () {return view('welcome');});
    Route::get('/home', 'HomeController@index')->name('home');


    Route::get('/players', 'profile\IndexController@index');
    Route::get('/profile/{uuid}', 'profile\IndexController@profile');
    Route::post('/profile/{uuid}', 'profile\IndexController@save');

    Route::post('/api/stats/search', 'profile\IndexController@search');
    Route::post('/api/stats/searchip', 'profile\IndexController@searchip');


    Route::get('/players/search', 'stats\StatsController@playersStats');
    Route::get('/players/search/json/{text}', 'stats\StatsController@search');
    Route::get('/players/edit/{id}', 'stats\StatsController@editPlayer');
    Route::resource('/players/crud', 'crud\PlayersController');


    //Gestion section
    Route::get('/section/forum', 'section\ForumController@index');
    Route::get('/section/paid/{section}', 'section\PaidController@index');
    Route::post('/section/paid/{section}', 'section\PaidController@save');
    Route::get('/paid', 'website\PaidController@index');
    Route::get('/paid/{uuid}', 'website\PaidController@view');

    Route::get('/tfacheck', 'section\TfaController@index');


    //Website
    Route::get('/website', 'website\IndexController@index');

    Route::get('/website/achat/{uuid}', 'website\AchatController@index');

    Route::get('/website/vote', 'website\VoteController@index');
    Route::post('/website/vote', 'website\VoteController@save');

    Route::get('/website/compta', 'website\IndexController@compta');
    Route::get('/website/compta/{date}', 'website\IndexController@compta');
    Route::resource('/website/crud/server', 'website\crud\ServerController');
    Route::resource('/website/crud/category', 'website\crud\CategoryController');
    Route::resource('/website/crud/product', 'website\crud\ProductController');
    Route::resource('/website/crud/items', 'website\crud\ItemsController');


});
