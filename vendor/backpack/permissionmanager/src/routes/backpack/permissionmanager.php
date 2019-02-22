<?php

/*
|--------------------------------------------------------------------------
| Backpack\PermissionManager Routes
|--------------------------------------------------------------------------
|
| This file is where you may define all of the routes that are
| handled by the Backpack\PermissionManager package.
|
*/

Route::group([
            'namespace'  => 'Backpack\PermissionManager\app\Http\Controllers',
            'prefix'     => config('backpack.base.route_prefix', 'admin'),
            'middleware' => ['web', 'admin'],
    ], function () {
        CRUD::resource('permission', 'PermissionCrudController')->middleware("can:gestion_index");
        CRUD::resource('role', 'RoleCrudController')->middleware("can:gestion_index");
        CRUD::resource('user', 'UserCrudController')->middleware("can:gestion_index");
    });
