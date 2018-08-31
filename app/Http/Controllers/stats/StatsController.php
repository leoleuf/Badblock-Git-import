<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 14/01/2018
 * Time: 09:38
 */

namespace App\Http\Controllers\stats;
use App\Players;
use Illuminate\Support\Facades\Redis;
use MongoDB\Database;
use Illuminate\Support\Facades\DB;


class StatsController
{

    public function  playersStats()
    {
        return view('stats.statsPlayers');
    }


    public function search($text)
    {

        $data = Players::where('name', 'like', "%" . strtolower($text) . "%")->take(10)->get();

        if (count($data) == 0){
            return '';
        }
        $rep = [];

            echo '
            <table class="table table-striped">
                                            <thead>
                                            <tr>
                                                <th>Pseudo</th>
                                                <th>Grade</th>
                                                <th></th>
                                            </tr>
                                            </thead>
                                            <tbody>';

            foreach ($data as $row){
                echo '<tr>
                                                    <td>'. $row['name'] .'</td>
                                                    <td>'. $row['permissions']['group'] .'</td>
                                                    <td><a href="/players/edit/'. $row['_id'] .'" class="btn btn-icon waves-effect waves-light btn-danger m-b-5"> <i class="fa fa-edit"></i> </a></td>
                                                
                                                </tr>';
            }

            echo '
                                                
                                            
                                            </tbody>
                                        </table>
           
            ';



    }

    public function editPlayer($id)
    {
        $user = Players::find($id);


        return view('players.edit', compact('user'));


    }




}