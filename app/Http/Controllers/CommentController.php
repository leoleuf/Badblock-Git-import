<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Redis;




class CommentController extends Controller
{
    public function save($cat, $id)
    {
        $catName = encname($cat);
        $l = array();
        foreach (config('tag.cat') as $k)
        {
            $l[enctag($k)] = 0;
        }

        if (!in_array($catName, $l))
        {
            exit('invalid category');
            return;
        }

        $id = encname($id);
        $data = json_decode(Redis::get('server:' . $id));
        if (Auth::user()){
            if(isset($_POST['text'])){
                if(!empty($_POST['text'])){
                    if(strlen($_POST['text']) > 9){
                        if (DB::table('comments')->where('server_id', '=', $data->id)->where('user_id', '=', Auth::user()->id)->count() > 0){
                            DB::table('comments')->where('server_id', '=', $data->id)->where('user_id', '=', Auth::user()->id)->delete();
                        }
                        if ($_POST['rate2'] == 0 || $_POST['rate2'] < 6 || $_POST['rate2'] > 0){

                            $id = encname($id);
                            DB::table('comments')->insert(
                                [
                                    'user_id' => Auth::user()->id,
                                    'date' => date("Y-m-d H:i:s"),
                                    'ip' => $_SERVER['REMOTE_ADDR'],
                                    'username' => Auth::user()->name,
                                    'server_id' => $data->id,
                                    'comment' => $_POST['text'],
                                    'note' => $_POST['rate2']
                                ]
                            );

                            $comment = [
                                'user_id' => Auth::user()->id,
                                'date' => date("Y-m-d H:i:s"),
                                'ip' => $_SERVER['REMOTE_ADDR'],
                                'server_id' => $data->id,
                                'comment' => $_POST['text'],
                                'username' => Auth::user()->name,
                                'note' => $_POST['rate2']
                            ];


                            //Re-calcul de la note
                            $count = DB::table('comments')->where('server_id', "=", $data->id)->count();
                            $sum = DB::table('comments')->where('server_id', "=", $data->id)->sum('note');
                            if ($count > 0){
                                $note = round(($sum / $count), 0);
                            }else{
                                $note = 0;
                            }

                            $comment = DB::select("select * from comments WHERE server_id =" . $data->id. " ORDER by date LIMIT 10");
                            $data->comment = $comment;
                            $data->note = $note;
                            $data->reviews = $data->reviews + 1;
                            Redis::set('server:' . $id, json_encode($data));

                            // Update in the vote count in the database
                            DB::table('server_list')
                                ->where('id', '=', $data->id)
                                ->update(['note' => $note,'reviews' => $count]);

                            return InfoController::info($cat, $id);
                        }else{
                            return InfoController::info($cat, $id, 'Veuillez saisir une note valide !');
                        }
                    }else{
                        //Erreur manque de caractère
                        return InfoController::info($cat, $id, 'Veuillez écrire un commentaire d\'au moins 10 caractères !');
                    }
                }else{
                    //Erreur
                    return InfoController::info($cat, $id, 'Veuillez écrire un commentaire !');
                }
            }else{
                return InfoController::info($cat, $id, 'Veuillez écrire un commentaire !');
            }
        }else{
            //Erreur
            return InfoController::info($cat, $id, 'Vous devez être connecté pour poster un commentaire !');
        }

    }




}
