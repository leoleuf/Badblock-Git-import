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

    Route::get('/devhome', 'toenga\DevInstanceController@index');
    Route::get('/toenga', 'toenga\HomeController@index');
    Route::get('/toenga/instance/{uid}', 'toenga\InstanceController@index');
    Route::get('/toenga/websock/{uid}', 'toenga\InstanceController@tokenWS');

    Route::get('/players', 'stats\StatsController@playersStats');
    Route::resource('/players/crud', 'crud\PlayersController');

    Route::get('/api/toenga/treeroot/{name}', 'toenga\HomeController@treeroot');
    Route::get('/api/toenga/treechild/{name}', 'toenga\HomeController@treechild');

    Route::get('/api/mongo', 'ajax\HomeController@mongoStat');
    Route::get('/api/online', 'ajax\HomeController@online');
    Route::get('/api/players', 'ajax\HomeController@players');
    Route::get('/api/playersjson', 'ajax\HomeController@onlineChart');


    Route::get('/website', 'website\IndexController@index');
    Route::resource('/website/crud/server', 'website\crud\ServerController');
    Route::resource('/website/crud/category', 'website\crud\CategoryController');
    Route::resource('/website/crud/product', 'website\crud\ProductController');
    Route::resource('/website/crud/items', 'website\crud\ItemsController');





});
