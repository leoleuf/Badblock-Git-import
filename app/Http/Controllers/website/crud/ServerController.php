<?php

namespace App\Http\Controllers\website\crud;

use App\Models\Server;
use Illuminate\Http\Request;
use Illuminate\Http\Redirect;
use Illuminate\Routing\Redirector;


class ServerController extends \App\Http\Controllers\Controller {
    //Test for git



    /**
     * Display a listing of users
     *
     * @return Redirector
     */
    public function index()
    {
        return redirect("/website/shop");
    }
    /**
     * Show the form for creating a new user
     *
     * @return Response
     */
    public function create()
    {
        return view('website.server.server-create');
    }
    /**
     * Store a newly created user in storage.
     *
     * @return Redirector
     */
    public function store(Request $request)
    {

        $server = new Server;
        $server->name = $request->input('name');
        $server->realname = $request->input('realname');
        $server->icon = $request->input('icon');
        $server->power = $request->input('power');

        $server->ig_material = $request->input('ig_material');
        $server->ig_data = $request->input('ig_data');

        if ($request->input('visibility') == "on"){
            $server->visibility = true;
        }else{
            $server->visibility = false;
        }

        $server->save();

        return redirect("website/shop/");
    }
    /**
     * Display the specified user.
     *
     * @param  int  $id
     * @return Response
     */
    public function show($id)
    {
        $user = Server::findOrFail($id);
        return view('server.show', compact('user'));
    }
    /**
     * Show the form for editing the specified user.
     *
     * @param  int  $id
     * @return Response
     */
    public function edit($id)
    {
        $server = Server::find($id);
        return view('website.server.server-edit', compact('server'));
    }
    /**
     * Update the specified resource in storage.
     *
     * @param  int  $id
     * @return Redirector
     */
    public function update($id,Request $request)
    {

        $server = Server::findOrFail($id);

        $server->name = $request->input('name');
        $server->realname = $request->input('realname');
        $server->icon = $request->input('icon');
        $server->power = $request->input('power');

        if ($request->input('visibility') == "on"){
            $server->visibility = true;
        }else{
            $server->visibility = false;
        }

        $server->save();

        return redirect("/website/shop/");
    }
    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return Redirector
     */
    public function destroy($id)
    {
        Server::destroy($id);

       return redirect("/website/shop");

    }
}