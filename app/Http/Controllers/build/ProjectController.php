<?php
/**
 * Created by PhpStorm.
 * User: Fragan
 * Date: 14/04/2019
 * Time: 01:06
 */

namespace App\Http\Controllers\build;

use App\Http\Controllers\Controller;
use App\Http\Controllers\ConverterController;
use App\Http\Controllers\section\NotificationsController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Auth;

class ProjectController extends Controller
{
    //Liste des projets
    public function index()
    {
        return view('build.index', ['data' => DB::table('project_build')->get()]);
    }

    //Suppression d'un projet
    public function delete($id)
    {
        DB::table('project_build')->where('id', $id)->delete();
        return back();
    }

    public function check($id)
    {
        DB::table('project_build')->where('id', $id)->update(['isFinished' => true]);
        return back();
    }

    //Création d'un projet
    public function create()
    {

        $team = [];

        foreach (DB::table('role_users')->where('role_id', 46)->get() as $builders)
        {
            array_push($team, ConverterController::convertIDPseudo($builders->user_id));
        }

        return view('build.new', ['builders' => $team, 'dependancy' => DB::table('project_build')->get()]);
    }

    public function newProject(Request $request)
    {
        $team = "";

        foreach ($request->team as $teams)
        {
            NotificationsController::system_send([
                'title' => 'Projet de Build',
                'link' => '/build/project',
                'text' => Auth::user()->name." vous à ajouté à un nouveau projet de build."
            ], $teams);
            $team .= $teams." ";
        }

        $team = substr($team, 0, -1);

        DB::table('project_build')->insert([

            'name' => $request->name,
            'dateStart' => $request->dateStart,
            'dateEnd' => $request->dateEnd,
            'desc_project' => $request->text,
            'team' => $team,
            'category' => $request->category,
            'price' => $request->price,
            'priority' => $request->priority,
            'dependancy' => $request->dependancy,
            'created_by' => Auth::user()->name

        ]);

        return redirect('/build/project');
    }

    public static function convertTeam($team)
    {
        return explode(" ", $team);
    }

}