<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 17/03/2018
 * Time: 18:49
 */

namespace App\Http\Controllers\section;


use App\Models\Funds;
use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class StaffController extends Controller
{


    public function index(){
        //Alternate group
        $alt = ['$or' =>
            [
                ['permissions.alternateGroups.superviseur' => ['$exists' => true]],
                ['permissions.alternateGroups.helper' => ['$exists' => true]],
                ['permissions.alternateGroups.admin' => ['$exists' => true]],
                ['permissions.alternateGroups.modo' => ['$exists' => true]],
                ['permissions.alternateGroups.supermodo' => ['$exists' => true]],
                ['permissions.alternateGroups.responsable' => ['$exists' => true]],
                ['permissions.alternateGroups.builder' => ['$exists' => true]],
                ['permissions.alternateGroups.animateur' => ['$exists' => true]],
                ['permissions.alternateGroups.modoforum' => ['$exists' => true]],
                ['permissions.alternateGroups.graphiste' => ['$exists' => true]],
                ['permissions.alternateGroups.redacteur' => ['$exists' => true]],
                ['permissions.alternateGroups.staff' => ['$exists' => true]]
            ]
        ];

        $group = ['$or' =>
            [
                ['permissions.group' => 'superviseur'],
                ['permissions.group' => 'helper'],
                ['permissions.group' => 'admin'],
                ['permissions.group' => 'modo'],
                ['permissions.group' => 'supermodo'],
                ['permissions.group' => 'responsable'],
                ['permissions.group' => 'builder'],
                ['permissions.group' => 'animateur'],
                ['permissions.group' => 'modoforum'],
                ['permissions.group' => 'graphiste'],
                ['permissions.group' => 'redacteur'],
                ['permissions.group' => 'staff']
            ]
        ];

        $data = DB::connection('mongodb_server')->collection('players')->where($alt)->orWhere($group)->orderby('permissions.group')->get();

        return view('section.staff')->with('user', $data);
    }

}