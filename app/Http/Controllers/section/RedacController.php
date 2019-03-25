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

    public function add_text(Request $request)
    {

        if(!empty($request->input('title')) && !empty($request->input('text')))
        {
            DB::table('correction_text')->insert([

                'title' => $request->input('title'),
                'send_by' => Auth::user()->name,
                'correct_by' => "",
                'text' => $request->input('text'),
                'is_correct' => 0,
                'date_post' => date('Y-m-d H:m:s')

            ]);
        }

        return redirect('/section/correction');
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

    public function view_corrected_text($id)
    {
        return view('section.redac.view_text', ['data' => DB::table('correction_text')->where('id', $id)->where('is_correct', 1)->get()[0]]);
    }

    public function suppr_text($id)
    {
        DB::table('correction_text')->delete($id);
        return redirect('/section/correction');
    }

}