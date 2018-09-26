<?php

namespace Backpack\PermissionManager\app\Models;

use Backpack\CRUD\CrudTrait;
use Spatie\Permission\Models\Role as OriginalRole;

class Role extends OriginalRole
{
    use CrudTrait;

    protected $fillable = ['name', 'forum_role_id', 'teamspeak_role_id','server_group','updated_at', 'created_at'];
}
