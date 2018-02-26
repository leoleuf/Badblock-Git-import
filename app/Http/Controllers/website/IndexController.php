<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 21/01/2018
 * Time: 15:10
 */

namespace App\Http\Controllers\website;


use App\Funds;
use App\Http\Controllers\Controller;


class IndexController extends Controller
{
    public function index(){
        $period = new \DatePeriod(
            new \DateTime(date("y-m-d", strtotime("-30 days"))),
            new \DateInterval('P1D'),
            new \DateTime(date("y-m-d"))
        );

        foreach ($period as $row){
            $mongoDateObject = new MongoDate(strtotime($row));

            $data = Funds::where('date', '=', $mongoDateObject)->get();
            var_dump($data);
        }


        return view('website.index')->with('date', $period);
    }

}