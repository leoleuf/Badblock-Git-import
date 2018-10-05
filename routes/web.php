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
    'prefix'     => "api"
], function () {
    //Website
    Route::post('/upload', 'Api\ScreenController@upload');
});

Route::group([
    'middleware' => ['auth'],
], function () {
    Route::get('/', function () {return view('welcome');});
    Route::get('/home', 'HomeController@index')->name('home');

    //Notificaiton link redirect
    Route::get('/notif-link/{id}', 'NotificationController@index');


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
    Route::group([
        'prefix'     => "section",
        'middleware' => ['auth'],
    ], function () {
        //Gestion avertissement
        Route::get('/avertissement', 'section\ForumController@index');
        Route::post('/avertissement', 'section\ForumController@index');

        //Gestion section forum
        Route::get('/forum', 'section\ForumController@index');
        Route::get('/paid/{section}', 'section\PaidController@index');
        Route::post('/paid/{section}', 'section\PaidController@save');
    });

    Route::get('/paid', 'website\PaidController@index');
    Route::get('/paid/{uuid}', 'website\PaidController@view');

    Route::get('/tfacheck', 'section\TfaController@index');

    //List all staff
    Route::get('/allstaff', 'section\StaffController@index');



    //TeamSpeak
    Route::group([
        'prefix'     => "teamspeak",
        'middleware' => ['auth'],
    ], function () {
        Route::get('/banlist', 'mod\TeamspeakController@banList');
    });

    Route::group([
        'prefix'     => "website",
        'middleware' => ['auth'],
    ], function () {
        //Website
        Route::get('/', 'website\IndexController@index');

        Route::get('/achat/{uuid}', 'website\AchatController@index');

        Route::get('/vote-download', 'website\VoteController@down');
        Route::get('/vote', 'website\VoteController@index');
        Route::post('vote', 'website\VoteController@save');

        Route::get('/prefix', 'website\PrefixController@index');
        Route::post('/prefix', 'website\PrefixController@save');

        Route::get('/compta', 'website\IndexController@compta');
        Route::get('/compta/{date}', 'website\IndexController@compta');
        Route::resource('/crud/server', 'website\crud\ServerController');
        Route::resource('/crud/category', 'website\crud\CategoryController');
        Route::resource('/crud/product', 'website\crud\ProductController');
        Route::resource('/crud/items', 'website\crud\ItemsController');
    });

    Route::group([
        'prefix'     => "settings",
        'middleware' => ['auth'],
    ], function () {
        //Website
        Route::get('/sharex', 'settings\SharexController@index');
        Route::get('/sharex-reg', 'settings\SharexController@new');
        Route::get('/sharex-down', 'settings\SharexController@down');

    });



});
