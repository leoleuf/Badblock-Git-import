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

Route::get('/2fa','PasswordSecurityController@show2faForm');
Route::post('/generate2faSecret','PasswordSecurityController@generate2faSecret')->name('generate2faSecret');
Route::post('/2fa','PasswordSecurityController@enable2fa')->name('enable2fa');
Route::post('/disable2fa','PasswordSecurityController@disable2fa')->name('disable2fa');

Route::post('/2faVerify', function () {

    Cookie::queue("TFA", true, 60 * 24 * 7);
    return redirect('/');

})->name('2faVerify')->middleware('2fa');

Route::group([
    'prefix'     => "api"
], function () {
    //Website
    Route::post('/upload', 'Api\ScreenController@upload');
    Route::post('/vrack', 'Infra\VrackController@api');
    Route::get('/minecraft', 'Infra\McController@players');
    Route::get('/ban', 'Infra\McController@ban');
    Route::post('/theme', 'HomeController@setTheme');

    Route::get('/msg-guardianner/{uuid}', 'moderation\GuardianController@sanction');
    Route::get('/msg-del-guardianner/{uuid}', 'moderation\GuardianController@setMessageOk');
    Route::get('/get-msg-guardianner', 'moderation\GuardianController@getAllMsg');

    Route::get('/cloudflare/purge/all', 'Infra\CloudFlareController@purge_all');

});

Route::group([
    'middleware' => ["auth", "2fa"],
], function () {

    Route::get('/', 'HomeController@index')->name('home');


    //Notificaiton link redirect
    Route::get('/notif-link/{id}', 'NotificationController@index');

    Route::get('/my-warns', 'section\WarningController@mylist');
    Route::get('/my-notifs', 'section\NotificationsController@mylist');

    Route::get('/gradeperso-clear', 'others\ClearGradePersoController@index');
    Route::get('/setGradesOnNewServer/{row}', 'others\SetGradeOnNewServerController@index');
    Route::get('/starPerms', 'others\BordelController@starPerm');

    Route::group([
        'middleware' => ['auth'],
    ], function () {
        //Website
        Route::get('/hookix', 'upload\HookiXController@index');
        Route::post('/hookix/upload', 'upload\HookiXController@upload');

    });


Route::group([
    'prefix' => "profil",
    'middleware' => ["auth"]
], function(){

    Route::get('/', 'ProfilController@index');
    Route::post('/', 'ProfilController@reset');

    Route::get('/todolists', 'profile\TodolistsController@index')->middleware("can:staff_todolist");
    Route::post('/todolists', 'profile\TodolistsController@done')->middleware("can:staff_todolist");

    Route::get('/file-uploader', 'profile\BuilderFileUploaderController@index')->middleware("can:build_upload_file");
    Route::post('/file-uploader', 'profile\BuilderFileUploaderController@upload')->middleware("can:build_upload_file");

});


    //Screenshort list
    Route::get('/screen', 'profile\ScreenController@index');
    Route::get('/screen/{id}', 'profile\ScreenController@page');

    Route::get('/gallery', 'upload\HookiXController@list_gallery');

    Route::get('/moderation/casier/{player}', 'moderation\CasierController@case')->middleware("can:mod_user_record");;


    Route::group([
        'prefix'     => "moderation",
        'middleware' => ['auth','can:mod_index'],
    ], function () {
        //Modération
        Route::get('/', 'moderation\ModerationController@index');
        Route::get('/screen', 'moderation\ModerationController@screen');
        Route::get('/sanction', 'moderation\ModerationController@sanction');
        Route::post('/union', 'moderation\ModerationController@union');
        Route::post('/share', 'moderation\ModerationController@share');
        //Modération casier
        Route::get('/mcasier/{player}', 'moderation\CasierController@minicase')->middleware("can:mod_user_record");
        Route::get('/preuve/{id}', 'moderation\CasierController@preuve')->middleware("can:mod_proof");
        Route::get('/guardian/{id}', 'moderation\GuardianController@view')->middleware("can:mod_guardianer");
        //Search moderator
        Route::get('/sanction/search', 'moderation\ModerationController@search_index')->middleware("can:mod_search_mod_sanction");
        Route::get('/sanction/{username}', 'moderation\ModerationController@search')->middleware("can:mod_search_mod_sanction");

        //TX Sanction
        Route::get('/sanction-tx', 'moderation\SanctionController@index');
        Route::post('/sanction-tx', 'moderation\SanctionController@postSanction');
        Route::get('/tx-sanction/', 'moderation\SanctionController@tx');

        //Search double account
        Route::get('/seenaccount/', 'moderation\SeenController@index')->middleware("can:mod_account_seen");
        Route::post('/seenaccount/speed', 'moderation\SeenController@speedsearch')->middleware("can:mod_account_seen");
        Route::post('/seenaccount/long', 'moderation\SeenController@longsearch')->middleware("can:mod_account_seen");

        Route::get('/guardian', 'moderation\GuardianController@index')->middleware("can:mod_guardianer");

        /* Ajax routes */
        Route::get('/guardian/ajax/unprocessed-messages', 'moderation\GuardianController@getUnprocessedMessages');
        Route::get('/guardian/ajax/sanc-message/{messageId}', 'moderation\GuardianController@determineSanction');
        Route::get('/guardian/ajax/jsonsanc-message/{messageId}', 'moderation\GuardianController@jsonSanction');

        Route::get('/guardian/ajax/sancsend-message/{messageId}', 'moderation\GuardianController@sanction');


        Route::get('/guardian/ajax/set-message-ok/{messageId}', 'moderation\GuardianController@setMessageOk');

    });

    Route::group([
        'prefix'     => "animation",
        'middleware' => ['auth','can:animation'],
    ], function () {
        //Animation
        Route::get('/pb', 'Animation\GiveController@points')->middleware("can:anim_give_pb");
        Route::get('/item', 'Animation\GiveController@item')->middleware("can:anim_give_item");

        Route::post('/pb', 'Animation\GiveController@savepoints')->middleware("can:anim_give_pb");
        Route::post('/item', 'Animation\GiveController@saveitem')->middleware("can:anim_give_item");

        Route::get('/msg-anim', 'Animation\AnimationController@index')->middleware("can:anim_send_automessages");
        Route::post('/msg-anim', 'Animation\AnimationController@setIgMsg')->middleware("can:anim_send_automessages");

        Route::post('/msg-anim/changeMessage', 'Animation\AnimationController@changeMessage')->middleware("can:anim_send_automessages");
        Route::post('/msg-anim/deleteMessage', 'Animation\AnimationController@deleteMessage')->middleware("can:anim_send_automessages");

    });



    Route::get('/players', 'profile\IndexController@index')->middleware("can:mod_find_user");
    Route::get('/profile/{uuid}', 'profile\IndexController@profile')->middleware("can:mod_find_user");

    Route::group([
        'prefix'     => "profile-api"
    ], function () {
        Route::post('/{uuid}/resetpassword', 'profile\ActionController@resetPassword')->middleware("can:mod_profil_user");
        Route::post('/{uuid}/resettfa', 'profile\ActionController@resetTfa')->middleware("can:mod_profil_user");
        Route::post('/{uuid}/resetom', 'profile\ActionController@resetOm')->middleware("can:mod_profil_user");
        Route::post('/{uuid}/resetol', 'profile\ActionController@resetOl')->middleware("can:mod_profil_user");
        Route::post('/{uuid}/addgroup', 'profile\ActionController@addGroup')->middleware("can:mod_profil_user");
    });


    Route::post('/api/stats/search', 'profile\IndexController@search');
    Route::post('/api/stats/searchip', 'profile\IndexController@searchip');


    //Gestion section
    Route::group([
        'prefix'     => "section",
        'middleware' => ['auth'],
    ], function () {

        //Gestion section forum
        Route::get('/forum', 'section\ForumController@index')->middleware("can:admin_manage_forum");

        Route::get('/connection', 'section\StaffController@connection')->middleware('can:mod_stats_connexion');

        Route::get('/blog', 'section\RedacController@blog')->middleware('can:redac_stats_blog');
        Route::get('/correction', 'section\RedacController@correct')->middleware("can:redac_correct_view");
        Route::get('/correction-text/{id}','section\RedacController@correct_text')->middleware("can:redac_correct_text");
        Route::post('/validation-text','section\RedacController@validate_text')->middleware("can:redac_correct_text");
        Route::get('/correction/view/{id}','section\RedacController@view_corrected_text')->middleware("can:redac_correct_view");
        Route::post('/add-text','section\RedacController@add_text')->middleware("can:redac_correct_view");
        Route::get('/suppr-text/{id}','section\RedacController@suppr_text')->middleware("can:redac_correct_view");

        Route::get('/build', 'section\BuildController@index')->middleware('can:build_stats_connexion');

        Route::get('/paid/{section}', 'section\PaidController@index')->middleware('can:resp_paid_section');
        Route::post('/paid/{section}', 'section\PaidController@save')->middleware('can:resp_paid_section');

        Route::get('/paid', 'website\PaidController@index')->middleware('can:resp_paid_section');
        Route::get('/paidv/{uuid}', 'website\PaidController@view')->middleware('can:resp_paid_section');

        //List all staff
        Route::get('/tfacheck', 'section\TfaController@index')->middleware('can:resp_tfa_control');
        Route::post('/tfacheck/reset', 'section\TfaController@resetTFA')->middleware('can:resp_tfa_control');
        Route::post('/tfacheck/bypass', 'section\TfaController@bypassTFA')->middleware('can:resp_tfa_control');
        Route::get('/allstaff', 'section\StaffController@index')->middleware('can:resp_staff_list');

        //Check sanctions sans preuves
        Route::get('/preuves', 'section\ModController@preuves')->middleware('can:mod_proof');
        Route::post('/preuves', 'section\ModController@notif')->middleware('can:mod_proof');
        Route::post('/preuves/checked', 'section\ModController@checked')->middleware('can:mod_proof');
        Route::get('/preuves/top', 'section\ModController@top')->middleware('can:mod_proof');

        //Permissions serveur
        Route::get('/permission-serv', 'section\PermissionsController@index')->middleware('can:admin_server_perms');
        Route::get('/permission-serv/create', 'section\PermissionsController@create')->middleware('can:admin_server_perms');
        Route::post('/permission-serv/create', 'section\PermissionsController@create_perm')->middleware('can:admin_server_perms');
        Route::get('/permission-serv/{id}', 'section\PermissionsController@edit')->middleware('can:admin_server_perms');
        Route::post('/permission-serv/{id}', 'section\PermissionsController@save')->middleware('can:admin_server_perms');

        Route::get('/notifications', 'section\NotificationsController@index')->middleware('can:tools_notifs');
        Route::post('/notifications', 'section\NotificationsController@send')->middleware('can:tools_notifs');


        Route::get('/avertissement-list', 'section\WarningController@list')->middleware('can:tools_warn');
        Route::get('/avertissement', 'section\WarningController@index')->middleware('can:tools_warn');
        Route::post('/avertissement', 'section\WarningController@send')->middleware('can:tools_warn');
        Route::get('/avertissement/delete/{id}', 'section\WarningController@delete')->middleware('can:tools_warn');

        //Todo-list
        Route::get('/todo-management', 'section\TodoListController@index')->middleware('can:todo_list_all');
        Route::post('/todo-management', 'section\TodoListController@createOrModifyTodo')->middleware('can:todo_list_all');

        //URL Shortener Management
        Route::get('/url-shortener', 'section\URLShortenerManagerController@index')->middleware('can:tools_url_shorter');
        Route::post('/url-shortener', 'section\URLShortenerManagerController@post')->middleware('can:tools_url_shorter');

        //Youtubers management
        Route::get('/youtubers', 'section\YoutubersManagementController@index')->middleware('can:resp_youtubers_list');
        Route::post('/youtubers', 'section\YoutubersManagementController@post')->middleware('can:resp_youtubers_list');

        // Voir ses propres avertissements
        Route::get('/avertissement/{id}', 'section\WarningController@display')->middleware('can:tools_warn');

        Route::get('/grades', 'section\GradeController@index')->middleware('can:grade_view');

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
    ], function () {
        //Website
        Route::get('/', 'website\IndexController@index');

        Route::get('/shop', 'website\crud\ProductController@gestion')->middleware('can:admin_manage_website');

        Route::get('/achat/{uuid}', 'website\AchatController@index')->middleware('can:admin_manage_website');

        Route::get('/vote-download', 'website\VoteController@down')->middleware('can:admin_manage_website');
        Route::get('/vote', 'website\VoteController@index')->middleware('can:admin_manage_website');
        Route::post('/vote', 'website\VoteController@save')->middleware('can:admin_manage_website');

        Route::get('/prefix', 'website\PrefixController@index')->middleware('can:resp_validate_prefix');
        Route::post('/prefix', 'website\PrefixController@save')->middleware('can:resp_validate_prefix');

        Route::get('/registre', 'website\IndexController@registre')->middleware('can:admin_manage_website');

        Route::get('/compta', 'website\IndexController@compta')->middleware('can:show_compta');
        Route::get('/compta/{date}', 'website\IndexController@compta')->middleware('can:show_compta');
        Route::resource('/crud/server', 'website\crud\ServerController')->middleware('can:admin_manage_website');
        Route::resource('/crud/category', 'website\crud\CategoryController')->middleware('can:admin_manage_website');
        Route::resource('/crud/product', 'website\crud\ProductController')->middleware('can:admin_manage_website');

        Route::resource('/crud/items', 'website\crud\ItemsController')->middleware('can:admin_manage_website');

    });


    Route::group([
        'prefix'     => "infra"
    ], function () {
        Route::get('/vrack', 'Infra\VrackController@index')->middleware('can:network_ddns');
        Route::get('/vrack-update/{dns}', 'Infra\VrackController@update')->middleware('can:network_ddns');
        Route::get('/vrack-down/{dns}', 'Infra\VrackController@disable')->middleware('can:network_ddns');
        Route::get('/vrack-bat/{dns}', 'Infra\VrackController@bat')->middleware('can:network_ddns');


        Route::get('/docker', 'Infra\DockerController@index')->middleware('can:network_docker');
        Route::get('/docker/{ajax}', 'Infra\DockerController@index')->middleware('can:network_docker');

        Route::get('/docker-send', 'Infra\DockerController@send')->middleware('can:network_docker');

        Route::post('/docker/ajax/open', 'Infra\DockerController@openInstance')->middleware('can:network_docker');
        Route::post('/docker/ajax/close', 'Infra\DockerController@closeInstance')->middleware('can:network_docker');

        Route::get('/console', 'Infra\ConsoleController@index')->middleware('can:network_console');


        Route::get('/mongodb', 'Infra\MongoDBController@index')->middleware('can:network_mongodb');
        Route::get('/mongodb-ajax', 'Infra\MongoDBController@mongoStat')->middleware('can:network_mongodb');

        Route::get('/cloudflare', 'Infra\CloudFlareController@index')->middleware('can:network_cloudflare');

    });

    Route::group([
        'prefix'     => "server",
        'middleware' => ['auth','can:admin_server_manage']
    ], function () {

        Route::get('/', 'Infra\ServerManageController@index')->name('server.manage');
        Route::post('/motd', 'Infra\ServerManageController@motd');

    });

    Route::group([
        'prefix'     => "build",
        'middleware' => ['auth','can:build_project_view']
    ], function () {

        Route::get('/project', 'build\ProjectController@index')->name('build.index');
        Route::get('/project/delete/{id}', 'build\ProjectController@delete');
        Route::get('/project/check/{id}', 'build\ProjectController@check');
        Route::get('/project/new', 'build\ProjectController@create')->middleware('can:build_project_create');
        Route::post('/project/new', 'build\ProjectController@newProject')->middleware('can:build_project_create');

    });

});
