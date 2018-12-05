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
    'middleware' => ["auth"],
], function () {
    Route::get('/', function () {return view('welcome');});
    Route::get('/home', 'HomeController@index')->name('home');

    //Notificaiton link redirect
    Route::get('/notif-link/{id}', 'NotificationController@index');

    Route::group([
        'prefix'     => "settings",
        'middleware' => ['auth'],
    ], function () {
        //Website
        Route::get('/sharex', 'Settings\SharexController@index');
        Route::get('/sharex-reg', 'Settings\SharexController@new');
        Route::get('/sharex-down', 'Settings\SharexController@down');

    });

    //Screenshort list
    Route::get('/screen', 'profile\ScreenController@index');
    Route::get('/screen/{id}', 'profile\ScreenController@page');

    Route::group([
        'prefix'     => "moderation",
        'middleware' => ['auth','can:mod_index'],
    ], function () {
        //Modération
        Route::get('/', 'moderation\ModerationController@index');
        Route::get('/screen', 'moderation\ModerationController@screen');
        Route::get('/sanction', 'moderation\ModerationController@sanction');
        Route::post('/union', 'moderation\ModerationController@union');
        //Modération casier
        Route::get('/casier/{player}', 'moderation\CasierController@case');
        Route::get('/mcasier/{player}', 'moderation\CasierController@minicase');
        Route::get('/preuve/{id}', 'moderation\CasierController@preuve');

        Route::get('/sanction-tx', 'moderation\SanctionController@index');
        Route::get('/tx-sanction/', 'moderation\SanctionController@tx');

    });

    Route::group([
        'prefix'     => "animation",
        'middleware' => ['auth','can:animation'],
    ], function () {
        //Aniamtion
        Route::get('/pb', 'Animation\GiveController@points');
        Route::get('/item', 'Animation\GiveController@item');

        Route::post('/pb', 'Animation\GiveController@savepoints');
        Route::post('/item', 'Animation\GiveController@saveitem');
    });



    Route::get('/players', 'profile\IndexController@index');
    Route::get('/profile/{uuid}', 'profile\IndexController@profile');

    Route::group([
        'prefix'     => "profile-api"
    ], function () {
        Route::post('/{uuid}/resetpassword', 'profile\ActionController@resetPassword')->middleware("can:profile_password");
        Route::post('/{uuid}/resettfa', 'profile\ActionController@resetTfa')->middleware("can:profile_tfa");
        Route::post('/{uuid}/resetom', 'profile\ActionController@resetOm')->middleware("can:profile_om");
        Route::post('/{uuid}/resetol', 'profile\ActionController@resetOl')->middleware("can:profile_om");

    });


    Route::post('/api/stats/search', 'profile\IndexController@search');
    Route::post('/api/stats/searchip', 'profile\IndexController@searchip');


    //Gestion section
    Route::group([
        'prefix'     => "section",
        'middleware' => ['auth'],
    ], function () {
        //Gestion avertissement
        Route::get('/avertissement', 'section\ForumController@index')->middleware('can:gestion_warn');
        Route::post('/avertissement', 'section\ForumController@index')->middleware('can:gestion_warn');

        //Gestion section forum
        Route::get('/forum', 'section\ForumController@index');
        Route::get('/paid/{section}', 'section\PaidController@index')->middleware('can:gestion_paid');
        Route::post('/paid/{section}', 'section\PaidController@save')->middleware('can:gestion_paid');

        Route::get('/paid', 'website\PaidController@index')->middleware('can:gestion_paid');
        Route::get('/paidv/{uuid}', 'website\PaidController@view')->middleware('can:gestion_paid');

        //List all staff
        Route::get('/tfacheck', 'section\TfaController@index')->middleware('can:gestion_tfalist');
        Route::get('/allstaff', 'section\StaffController@index')->middleware('can:gestion_tfalist');
    });





    //TeamSpeak
    Route::group([
        'prefix'     => "teamspeak",
        'middleware' => ["auth"],
    ], function () {
        Route::get('/banlist', 'moderation\TeamspeakController@banList');
    });

    Route::group([
        'prefix'     => "website",
        'middleware' => ["auth", "can:website"],
    ], function () {
        //Website
        Route::get('/', 'website\IndexController@index');

        Route::get('/achat/{uuid}', 'website\AchatController@index');

        Route::get('/vote-download', 'website\VoteController@down')->middleware('can:website_vote');
        Route::get('/vote', 'website\VoteController@index')->middleware('can:website_vote');
        Route::post('vote', 'website\VoteController@save')->middleware('can:website_vote');

        Route::get('/prefix', 'website\PrefixController@index')->middleware('can:website_prefix');
        Route::post('/prefix', 'website\PrefixController@save')->middleware('can:website_prefix');

        Route::get('/compta', 'website\IndexController@compta')->middleware('can:website_admin');
        Route::get('/compta/{date}', 'website\IndexController@compta')->middleware('can:website_admin');
        Route::resource('/crud/server', 'website\crud\ServerController')->middleware('can:website_admin');
        Route::resource('/crud/category', 'website\crud\CategoryController')->middleware('can:website_admin');
        Route::resource('/crud/product', 'website\crud\ProductController')->middleware('can:website_admin');
        Route::resource('/crud/items', 'website\crud\ItemsController')->middleware('can:website_admin');
    });
});
