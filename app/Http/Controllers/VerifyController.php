<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;



class VerifyController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('auth');
    }

    public function pleaseverify($id)
    {
        $server = DB::select('select * from server_list where id = ? LIMIT 1', [$id]);

        if (empty($server))
        {
            return redirect('/dashboard');
        }

        if ($server[0]->user_id == Auth::user()->id){
            return view('panel.verify', ['data' => $server[0]]);
        }else{
            return redirect("/dashboard");
        }
    }

    public function verify(Request $request, $id)
    {
        $id = intval($id);
        $server = DB::select('select * from server_list where id = ? LIMIT 1', [$id]);

        if (empty($server))
        {
            return redirect('/dashboard');
        }

        if ($server[0]->user_id != Auth::user()->id) {
            return redirect("/dashboard");
        }

        DB::table('server_list')
            ->where('id', '=', $id)
            ->update(
                [
                    'verified' => 0
                ]
            );

        if (filter_var($server[0]->website, FILTER_VALIDATE_URL) === FALSE)
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Le site Internet doit être valide.',
                    'important' => true
                )
            ]);

            return view('panel.verify', ['data' => $server[0]]);
        }

        $t = @file_get_contents($server[0]->website);

        if (strpos($t, '<a title="Serveur Minecraft" href="https://serveur-multigames.net/minecraft">Serveur Minecraft</a>') !== false)
        {
            DB::table('server_list')
                ->where('id', '=', $id)
                ->update(
                    [
                        'verified' => 1
                    ]
                );
            return view('panel.verify', ['data' => $server[0]]);
        }

        $request->session()->flash('flash', [
            array(
                'level' => 'danger',
                'message' => 'Code introuvable. Veuillez bien mettre le code exactement comme demandé sur la page.',
                'important' => true
            )
        ]);

        return view('panel.verify', ['data' => $server[0]]);
    }

}
