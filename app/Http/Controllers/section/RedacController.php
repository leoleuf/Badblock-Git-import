<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 17/03/2018
 * Time: 18:49
 */

namespace App\Http\Controllers\section;


use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Http\Request;


class RedacController extends Controller
{


    public function blog(){

        $Blog = DB::connection('mongodb')->collection('blog')->get();

        return view("section.redac.blogview")->with("Blog", $Blog);

    }

    public function correct()
    {
        return view('section.redac.correction', ['data' => DB::table('correction_text')->orderBy('date_post', 'DESC')->get()]);
    }

    public function correct_text($id)
    {
        return view('section.redac.correct_text', ['data' => DB::table('correction_text')->where('id', $id)->get()[0]]);
    }

    public function validate_text(Request $request)
    {
       DB::table('correction_text')->where('id', $request->input('id'))->update([

           'correct_by' => Auth::user()->name,
           'text' => $request->input('text'),
           'is_correct' => 1

       ]);

       return redirect('/section/correction');

    }

}