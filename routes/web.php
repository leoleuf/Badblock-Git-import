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
    Route::get('/api/mongo', 'ajax\HomeController@mongoStat');
    Route::get('/api/online', 'ajax\HomeController@online');



});
