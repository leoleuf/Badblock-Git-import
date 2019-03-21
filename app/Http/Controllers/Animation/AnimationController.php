<?php
/**
 * Created by PhpStorm.
 * User: Fragan
 * Date: 20/03/2019
 * Time: 22:35
 */

namespace App\Http\Controllers\Animation;

use App\Models\Funds;
use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class AnimationController extends Controller
{
    public function index()
    {
        return view('section.animation.msg', ['list' => DB::connection('mongodb')->collection('automessages')->get()[0]['message']]);
    }

    public function setIgMsg(Request $request)
    {
        $list = DB::connection('mongodb')->collection('automessages')->get()[0]['message'];

        array_push($list, $request->input('msg'));

        DB::connection('mongodb')->collection('automessages')->update(['message' => $list]);
        return redirect('/animation/msg-anim');
    }

}