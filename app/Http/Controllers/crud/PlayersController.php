<?php

namespace App\Http\Controllers\crud;

use App\Players;


class PlayersController extends BaseController {
    //Test for git



    /**
     * Display a listing of users
     *
     * @return Response
     */
    public function index()
    {
        $users = Players::skip(10)->take(10)->get();
        return view('users.index', compact('users'));
    }
    /**
     * Show the form for creating a new user
     *
     * @return Response
     */
    public function create()
    {
        return View::make('users.create');
    }
    /**
     * Store a newly created user in storage.
     *
     * @return Response
     */
    public function store()
    {
        // Fetch network and hostname count
        $networkCount = Input::get('networkCount');
        $hostnameCount = Input::get('hostnameCount');
        $rules = Players::$rules;
        $nameAttributes = Players::$nameAttributes;

        $validator = Validator::make($data = Input::all(), $rules);
        $validator->setAttributeNames($nameAttributes);
        if ($validator->fails())
        {
            return Redirect::back()->withErrors($validator)->withInput();
        }
        $newUser = new Players;
        $newUser->uid = Input::get('uid');

        $networks = array();
        for ($i=1; $i<=$networkCount; $i++) {
            if (Input::get('network_check_'.$i) || $i==1) {
                array_push($networks, array(
                    'nid'=>Input::get('nid_'.$i),
                    'n_name'=>Input::get('n_name_'.$i),
                    'n_ip'=>Input::get('n_ip_'.$i),
                    'n_status'=>Input::get('n_status_'.$i)
                ));
            }
        }
        $hostnames = array();
        for ($i=1;$i<=$hostnameCount;$i++) {
            if (Input::get('hostname_check_'.$i) || $i==1) {
                array_push($hostnames, array(
                    'hostname'=>Input::get('hostname_'.$i),
                    'block'=>Input::get('block_'.$i)
                ));
            }
        }

        $newUser->networks = $networks;
        $newUser->hostnames = $hostnames;
        $newUser->save();
        return Redirect::route('users.index');
    }
    /**
     * Display the specified user.
     *
     * @param  int  $id
     * @return Response
     */
    public function show($id)
    {
        $user = Players::findOrFail($id);
        return View::make('users.show', compact('user'));
    }
    /**
     * Show the form for editing the specified user.
     *
     * @param  int  $id
     * @return Response
     */
    public function edit($id)
    {
        $user = Players::find($id);
        return View::make('users.edit', compact('user'));
    }
    /**
     * Update the specified resource in storage.
     *
     * @param  int  $id
     * @return Response
     */
    public function update($id)
    {

        $user = Players::findOrFail($id);

        $user->save();
        return Redirect::route('users.index');
    }
    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return Response
     */
    public function destroy($id)
    {
        Players::destroy($id);
        return Redirect::route('users.index');
    }
}