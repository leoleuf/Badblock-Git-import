<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 17/03/2018
 * Time: 18:49
 */

namespace App\Http\Controllers\moderation;


use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class CasierController extends Controller
{
    public function case($pseudo){
        $Sanctions = DB::connection('mysql_casier')->table('sanctions')
            ->where('pseudo', '=', $pseudo)
            ->orderBy('timestamp', 'DESC')
            ->take(10)
            ->get();

        return view('section.mod.casier', ['pseudo' => $pseudo, 'sanction' => $Sanctions]);
    }

    public function minicase($pseudo){
        $Sanctions = DB::connection('mysql_casier')->table('sanctions')
            ->where('pseudo', '=', $pseudo)
            ->orderBy('timestamp', 'DESC')
            ->take(100)
            ->get();

        return view('section.mod.minicasier', ['pseudo' => $pseudo, 'sanction' => $Sanctions]);
    }

}