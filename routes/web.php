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

Route::get('csrf', function() {
    return Session::token();
});

Route::get('index.html', function () {
    return redirect('/');
});

Route::get('index.php', function () {
    return redirect('/');
});

Route::get('/', 'HomeController@index')->name('accueil');

Route::get('/contact', 'ContactController@contact')->name('contact');
Route::post('/contact', 'ContactController@contactPost')->name('contactposts');

Route::get('/page/{id}', 'HomeController@page')->name('page');

Route::get('/tos', 'HomeController@tos')->name('tos');
Route::get('/partenaires', 'HomeController@partenaires')->name('partenaires');
Route::get('/partenaires2', 'HomeController@partenaires2')->name('partenaires2');
Route::get('/partenaires/{name}', 'RedirectController@partenaires')->name('paredirect');

Route::get('/faq', 'HomeController@faq')->name('faq');
Route::get('/reglement', 'HomeController@rules')->name('reglement');
Route::get('/mise-en-avant', 'HomeController@pub')->name('pub');

Route::get('/api', 'HomeController@api')->name('api');
Route::get('/add-server/true', 'HomeController@installtrue')->name('installtrue');
Route::get('/add-server/json', 'HomeController@installjson')->name('installjson');
Route::get('/add-server/callback', 'HomeController@installcallback')->name('installcallback');
Route::get('/add-server/votifier', 'HomeController@installvotifier')->name('installvotifier');
Route::get('/add-server', 'HomeController@addserver')->name('add-server');
Route::get('/logout/{key}', 'HomeController@logout')->name('logout');

Route::get('/api/{id}/{key}', 'ApiController@api')->name('api');
Route::get('/api/{id}/', 'ApiController@api')->name('api');
Route::get('/api/{id}/stats/votes', 'ApiController@votes')->name('api-votes');

Auth::routes();


//Cache
Route::get('/cache', 'CacheController@index');

Route::group([
    'prefix'     => "dashboard",
    'middleware' => ['auth'],
], function () {
    //Dashboard gestion des serveurs & abo
    Route::get('/', 'PanelController@index')->name('dashboard');


    Route::get('/add-server', 'PanelController@addServer')->name('addserver');
    Route::post('/add-server', 'PanelController@addServerSave')->name('addserverPost');

    //Delete server
    //Route::get('/del-server/{id}', 'PanelController@delServer')->name('delserver');
    Route::get('/votes/{id}', 'PanelController@votes')->name('votes');
    Route::get('/verify/{id}', 'VerifyController@pleaseverify')->name('verify');
    Route::post('/verify/{id}', 'VerifyController@verify')->name('verifyp');

    Route::get('/edit-server/{id}', 'PanelController@editServer')->name('editserver');
    Route::Post('/edit-server/{id}', 'PanelController@editServerSave')->name('editserverPost');

    //Stats
    Route::get('/stats', 'StatsController@index')->name('addserver');
    Route::get('/stats/{id}', 'StatsController@stats')->name('addserver');

    Route::get('/admin', 'PanelController@adming')->name('admin');
    Route::get('/admin/validate/{id}', 'PanelController@adminvalidate')->name('admin-validate');

    //Api
    Route::get('/api/{id}', 'ApiController@index')->name('api');

    //Pub
    Route::get('/mise-en-avant', 'PubController@index')->name('pub-index');
    Route::get('/mise-en-avant2', 'PubController@index2')->name('pub-index2');
    Route::post('/mise-en-avant', 'PubController@push')->name('pub-push');
    Route::get('/recharge', 'PubController@recharge')->name('pub-recharge');
    Route::post('/recharge-validate', 'PubController@rechargevalidate')->name('pub-recharge-validate');


    //Login log
    Route::get('/logs', 'LoginController@index')->name('logs');



});

//Route::get('/recherche', 'HomeController@searchmethod')->name('search');
//Route::post('/recherche', 'HomeController@searchmethod')->name('search');
//Route::get('/recherche/{term}', 'HomeController@search')->name('search');

Route::post('/pm', 'VoteController@pm')->name('pm');
Route::get('/lien', 'HomeController@lien')->name('lien');
Route::get('/assurance', 'HomeController@assurance')->name('assurance');

Route::get('/{cat}', 'HomeController@category')->name('category');

Route::get('/{cat}/tag/{id}', 'HomeController@tag')->name('page-tag')->where('id', '(.*)');;
Route::get('/{cat}/tag/{id}/{page}', 'HomeController@tag')->name('page-tag-p')->where('id', '(.*)');;

Route::get('/{cat}/page/{id}', 'HomeController@page')->name('page-page');
Route::get('/{cat}/page/{id}', 'HomeController@page')->name('page-page-p');


// Votes
Route::get('/{cat}/{id}/vote', 'VoteController@vote')->name('vote');
Route::post('/{cat}/{id}/vote', 'VoteController@post')->name('vote-post');

// Info
Route::get('/{cat}/{id}', 'InfoController@info')->name('info');
Route::get('/{cat}/{id}/ip', 'InfoController@ip')->name('ip');

//Commentaire
Route::post('/{cat}/{id}', 'CommentController@save')->name('post-comment');

// Redirect
Route::get('/{cat}/{id}/go', 'RedirectController@redirect')->name('redirect');